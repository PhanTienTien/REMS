package com.rems.transaction.service.impl;

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

import java.time.LocalDateTime;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionManager txManager;

    public TransactionServiceImpl(TransactionManager txManager) {
        this.txManager = txManager;
    }

    public Long completeTransaction(Long bookingId, Long staffId) {

        return txManager.execute(conn -> {

            BookingDAO bookingDAO = new BookingDAOImpl();
            PropertyDAO propertyDAO = new PropertyDAOImpl();
            TransactionDAO transactionDAO = new TransactionDAOImpl();

            // 1️⃣ Lock booking
            Booking booking = bookingDAO
                    .findByIdForUpdate(conn, bookingId)
                    .orElseThrow(() ->
                            new BusinessException(ErrorCode.NOT_FOUND));

            if (booking.getStatus() != BookingStatus.ACCEPTED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            // 2️⃣ Prevent duplicate transaction
            if (transactionDAO.findByBookingId(conn, bookingId).isPresent()) {
                throw new BusinessException(ErrorCode.DUPLICATE_TRANSACTION);
            }

            // 3️⃣ Lock property
            Property property = propertyDAO
                    .findByIdForUpdate(conn, booking.getPropertyId())
                    .orElseThrow(() ->
                            new BusinessException(ErrorCode.NOT_FOUND));

            if (property.getStatus() != PropertyStatus.RESERVED) {
                throw new BusinessException(ErrorCode.INVALID_STATE);
            }

            // 4️⃣ Create snapshot
            Transaction tx = new Transaction();

            tx.setBookingId(booking.getId());
            tx.setPropertyId(property.getId());
            tx.setCustomerId(booking.getCustomerId());
            tx.setAmount(property.getPrice());
            tx.setType(property.getType());

            tx.setPropertyPriceSnapshot(property.getPrice());
            tx.setPropertyTitleSnapshot(property.getTitle());
            UserDAO userDAO = new UserDAOImpl();

            User customer = userDAO.findById(conn, booking.getCustomerId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

            tx.setCustomerNameSnapshot(customer.getFullName());

            tx.setStatus(TransactionStatus.COMPLETED);
            tx.setCompletedAt(LocalDateTime.now());

            Long txId = transactionDAO.insert(conn, tx);

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

            return txId;
        });
    }
}
