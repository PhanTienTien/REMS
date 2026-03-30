package com.rems.transaction.service.impl;

import com.rems.activitylog.service.ActivityLogService;
import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.model.Booking;
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

    private final TransactionManager txManager;
    private final TransactionDAO transactionDAO = new TransactionDAOImpl();
    private final ActivityLogService activityLogService;

    public TransactionServiceImpl(TransactionManager txManager, ActivityLogService activityLogService) {
        this.txManager = txManager;
        this.activityLogService = activityLogService;
    }

    @Override
    public Long createTransaction(Connection conn,
                                  Long bookingId,
                                  Long performedBy,
                                  String ipAddress) {


        BookingDAO bookingDAO = new BookingDAOImpl();
        PropertyDAO propertyDAO = new PropertyDAOImpl();
        UserDAO userDAO = new UserDAOImpl();

        Booking booking = bookingDAO
                .findByIdForUpdate(conn, bookingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new BusinessException(ErrorCode.INVALID_STATE);
        }

        if (transactionDAO.findByBookingId(conn, bookingId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_TRANSACTION);
        }

        Property property = propertyDAO
                .findByIdForUpdate(conn, booking.getPropertyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (property.getStatus() != PropertyStatus.RESERVED) {
            throw new BusinessException(ErrorCode.INVALID_STATE);
        }

        User customer = userDAO
                .findById(conn, booking.getCustomerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

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
                "CREATE_TRANSACTION",
                "TRANSACTION",
                transactionId,
                "Created transaction for booking " + bookingId,
                ipAddress
        );

        return transactionId;
    }

    @Override
    public Long completeTransaction(Long transactionId, Long staffId) {

        return txManager.execute(conn -> {
            BookingDAO bookingDAO = new BookingDAOImpl();
            PropertyDAO propertyDAO = new PropertyDAOImpl();

            // 1️⃣ Lock transaction
            Transaction tx = transactionDAO
                    .findByIdForUpdate(conn, transactionId)
                    .orElseThrow(() ->
                            new BusinessException(ErrorCode.NOT_FOUND));

            if (tx.getStatus() != TransactionStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            // 2️⃣ Lock booking
            Booking booking = bookingDAO
                    .findByIdForUpdate(conn, tx.getBookingId())
                    .orElseThrow(() ->
                            new BusinessException(ErrorCode.NOT_FOUND));

            if (booking.getStatus() != BookingStatus.ACCEPTED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            // 3️⃣ Lock property
            Property property = propertyDAO
                    .findByIdForUpdate(conn, tx.getPropertyId())
                    .orElseThrow(() ->
                            new BusinessException(ErrorCode.NOT_FOUND));

            if (property.getStatus() != PropertyStatus.RESERVED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            // 4️⃣ Update transaction
            tx.setStatus(TransactionStatus.COMPLETED);
            tx.setCompletedAt(LocalDateTime.now(ZoneOffset.UTC));
            tx.setProcessedBy(staffId);

            transactionDAO.updateStatus(conn,
                    tx.getId(),
                    TransactionStatus.COMPLETED,
                    staffId);

            // 5️⃣ Update booking
            bookingDAO.updateStatus(conn,
                    booking.getId(),
                    BookingStatus.COMPLETED,
                    staffId);

            // 6️⃣ Update property
            PropertyStatus finalStatus =
                    property.getType().isSale()
                            ? PropertyStatus.SOLD
                            : PropertyStatus.RENTED;

            propertyDAO.updateStatus(conn,
                    property.getId(),
                    finalStatus);

            return tx.getId();
        });
    }

    @Override
    public List<Transaction> findAll() {

        return txManager.execute(conn ->
                transactionDAO.findAll(conn)
        );
    }

    @Override
    public List<Transaction> findByStatus(String status) {

        return txManager.execute(conn ->
                transactionDAO.findByStatus(
                        conn,
                        TransactionStatus.valueOf(status)
                )
        );
    }

    @Override
    public Transaction findById(Long id) {

        return txManager.execute(conn ->
                transactionDAO.findById(conn, id)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.NOT_FOUND))
        );
    }

    @Override
    public List<Transaction> getByCustomer(Long customerId) {

        return txManager.execute(conn ->
                transactionDAO.findByCustomer(conn, customerId)
        );

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

            return transactionDAO.search(
                    conn,
                    keyword,
                    st,
                    sortBy,
                    sortDir,
                    offset,
                    size
            );
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
}
