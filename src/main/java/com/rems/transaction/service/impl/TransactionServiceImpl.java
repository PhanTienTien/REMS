package com.rems.transaction.service.impl;

import com.rems.activitylog.dao.impl.ActivityLogDAOImpl;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.activitylog.service.impl.ActivityLogServiceImpl;
import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.model.Booking;
import com.rems.common.constant.ActivityLogAction;
import com.rems.common.constant.BookingStatus;
import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.TransactionStatus;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dao.impl.PropertyDAOImpl;
import com.rems.property.model.Property;
import com.rems.transaction.dao.TransactionDAO;
import com.rems.transaction.dao.impl.TransactionDAOImpl;
import com.rems.transaction.model.Transaction;
import com.rems.transaction.service.TransactionService;
import com.rems.user.dao.UserDAO;
import com.rems.user.dao.impl.UserDAOImpl;
import com.rems.user.model.User;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;
    private final BookingDAO bookingDAO;
    private final PropertyDAO propertyDAO;
    private final UserDAO userDAO;
    private final ActivityLogService activityLogService;
    private final TransactionManager txManager;

    public TransactionServiceImpl() {
        this(
                new TransactionManager(),
                new TransactionDAOImpl(),
                new BookingDAOImpl(),
                new PropertyDAOImpl(),
                new UserDAOImpl(),
                new ActivityLogServiceImpl(new TransactionManager(), new ActivityLogDAOImpl())
        );
    }

    public TransactionServiceImpl(TransactionManager txManager,
                                  ActivityLogService activityLogService) {
        this(
                txManager != null ? txManager : new TransactionManager(),
                new TransactionDAOImpl(),
                new BookingDAOImpl(),
                new PropertyDAOImpl(),
                new UserDAOImpl(),
                activityLogService != null
                        ? activityLogService
                        : new ActivityLogServiceImpl(new TransactionManager(), new ActivityLogDAOImpl())
        );
    }

    public TransactionServiceImpl(TransactionManager txManager,
                                  TransactionDAO transactionDAO,
                                  BookingDAO bookingDAO,
                                  PropertyDAO propertyDAO,
                                  UserDAO userDAO,
                                  ActivityLogService activityLogService) {
        this.txManager = txManager;
        this.transactionDAO = transactionDAO;
        this.bookingDAO = bookingDAO;
        this.propertyDAO = propertyDAO;
        this.userDAO = userDAO;
        this.activityLogService = activityLogService;
    }

    @Override
    public Long createTransaction(Connection conn,
                                  Long bookingId,
                                  Long performedBy,
                                  String ipAddress) {

        Booking booking = bookingDAO.findByIdForUpdate(conn, bookingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new BusinessException(ErrorCode.INVALID_STATE);
        }

        if (transactionDAO.findByBookingId(conn, bookingId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_TRANSACTION);
        }

        Property property = propertyDAO.findByIdForUpdate(conn, booking.getPropertyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));

        if (property.getStatus() != PropertyStatus.RESERVED) {
            throw new BusinessException(ErrorCode.INVALID_STATE);
        }

        User customer = userDAO.findById(conn, booking.getCustomerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Transaction tx = new Transaction();
        tx.setBookingId(booking.getId());
        tx.setPropertyId(property.getId());
        tx.setCustomerId(customer.getId());
        tx.setAmount(property.getPrice());
        tx.setType(property.getType());
        tx.setPropertyTitleSnapshot(property.getTitle());
        tx.setPropertyPriceSnapshot(property.getPrice());
        tx.setCustomerNameSnapshot(customer.getFullName());
        tx.setStatus(TransactionStatus.PENDING);

        Long transactionId = transactionDAO.insert(conn, tx);

        activityLogService.log(
                conn,
                performedBy,
                ActivityLogAction.CREATE_TRANSACTION,
                transactionId,
                ipAddress,
                bookingId
        );

        return transactionId;
    }

    @Override
    public Long completeTransaction(Long transactionId, Long staffId) {

        return txManager.execute(conn -> {
            Transaction tx = transactionDAO.findByIdForUpdate(conn, transactionId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND));

            if (tx.getStatus() != TransactionStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Booking booking = bookingDAO.findByIdForUpdate(conn, tx.getBookingId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

            if (booking.getStatus() != BookingStatus.ACCEPTED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Property property = propertyDAO.findByIdForUpdate(conn, tx.getPropertyId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));

            if (property.getStatus() != PropertyStatus.RESERVED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            if (tx.getType() != property.getType()) {
                throw new BusinessException(ErrorCode.TYPE_MISMATCH);
            }

            tx.setStatus(TransactionStatus.COMPLETED);
            tx.setCompletedAt(LocalDateTime.now(ZoneOffset.UTC));
            tx.setProcessedBy(staffId);

            transactionDAO.updateStatus(conn, tx.getId(), TransactionStatus.COMPLETED, staffId);
            bookingDAO.updateStatus(conn, booking.getId(), BookingStatus.COMPLETED, staffId);

            PropertyStatus finalStatus = property.getType().isSale()
                    ? PropertyStatus.SOLD
                    : PropertyStatus.RENTED;

            propertyDAO.updateStatus(conn, property.getId(), finalStatus);

            activityLogService.log(
                    conn,
                    staffId,
                    ActivityLogAction.COMPLETE_TRANSACTION,
                    tx.getId(),
                    null,
                    property.getId()
            );

            return tx.getId();
        });
    }

    @Override
    public Long completeTransactionByStaff(Long transactionId, Long staffId) {

        return txManager.execute(conn -> {
            Transaction tx = transactionDAO.findByIdForUpdate(conn, transactionId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND));

            if (tx.getStatus() != TransactionStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Booking booking = bookingDAO.findByIdForUpdate(conn, tx.getBookingId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));

            if (booking.getStatus() != BookingStatus.ACCEPTED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            Property property = propertyDAO.findByIdForUpdate(conn, tx.getPropertyId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));

            if (!staffId.equals(property.getCreatedBy())) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }

            if (property.getStatus() != PropertyStatus.RESERVED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            if (tx.getType() != property.getType()) {
                throw new BusinessException(ErrorCode.TYPE_MISMATCH);
            }

            tx.setStatus(TransactionStatus.COMPLETED);
            tx.setCompletedAt(LocalDateTime.now(ZoneOffset.UTC));
            tx.setProcessedBy(staffId);

            transactionDAO.updateStatus(conn, tx.getId(), TransactionStatus.COMPLETED, staffId);
            bookingDAO.updateStatus(conn, booking.getId(), BookingStatus.COMPLETED, staffId);

            PropertyStatus finalStatus = property.getType().isSale()
                    ? PropertyStatus.SOLD
                    : PropertyStatus.RENTED;

            propertyDAO.updateStatus(conn, property.getId(), finalStatus);

            activityLogService.log(
                    conn,
                    staffId,
                    ActivityLogAction.COMPLETE_TRANSACTION,
                    tx.getId(),
                    null,
                    property.getId()
            );

            return tx.getId();
        });
    }

    @Override
    public List<Transaction> findAll() {
        return txManager.execute(conn -> transactionDAO.findAll(conn));
    }

    @Override
    public List<Transaction> findByStatus(String status) {
        return txManager.execute(conn ->
                transactionDAO.findByStatus(conn, TransactionStatus.valueOf(status))
        );
    }

    @Override
    public Transaction findById(Long id) {
        return txManager.execute(conn ->
                transactionDAO.findById(conn, id)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND))
        );
    }

    @Override
    public Transaction findByIdForStaff(Long id, Long staffId) {
        return txManager.execute(conn ->
                transactionDAO.findByIdForStaff(conn, id, staffId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN))
        );
    }

    @Override
    public List<Transaction> getByCustomer(Long customerId) {
        return txManager.execute(conn -> transactionDAO.findByCustomer(conn, customerId));
    }

    @Override
    public List<Transaction> searchTransactions(String keyword,
                                                String status,
                                                String sortBy,
                                                String sortDir,
                                                int page,
                                                int size) {

        return txManager.execute(conn -> {
            TransactionStatus st = null;

            if (status != null && !status.isBlank()) {
                st = TransactionStatus.valueOf(status);
            }

            int offset = (page - 1) * size;
            return transactionDAO.search(conn, keyword, st, sortBy, sortDir, offset, size);
        });
    }

    @Override
    public List<Transaction> searchTransactionsByStaff(Long staffId,
                                                       String keyword,
                                                       String status,
                                                       String sortBy,
                                                       String sortDir,
                                                       int page,
                                                       int size) {

        return txManager.execute(conn -> {
            TransactionStatus st = null;

            if (status != null && !status.isBlank()) {
                st = TransactionStatus.valueOf(status);
            }

            int offset = (page - 1) * size;
            return transactionDAO.searchByStaff(conn, staffId, keyword, st, sortBy, sortDir, offset, size);
        });
    }

    @Override
    public int countTransactions(String keyword,
                                 String status) {

        return txManager.execute(conn -> {
            TransactionStatus st = null;

            if (status != null && !status.isBlank()) {
                st = TransactionStatus.valueOf(status);
            }

            return transactionDAO.count(conn, keyword, st);
        });
    }

    @Override
    public int countTransactionsByStaff(Long staffId,
                                        String keyword,
                                        String status) {

        return txManager.execute(conn -> {
            TransactionStatus st = null;

            if (status != null && !status.isBlank()) {
                st = TransactionStatus.valueOf(status);
            }

            return transactionDAO.countByStaff(conn, staffId, keyword, st);
        });
    }
}
