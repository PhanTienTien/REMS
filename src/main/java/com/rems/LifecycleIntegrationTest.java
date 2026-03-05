package com.rems;

import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.service.BookingService;
import com.rems.booking.service.impl.BookingServiceImpl;
import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.DBUtil;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dao.impl.PropertyDAOImpl;
import com.rems.transaction.dao.TransactionDAO;
import com.rems.transaction.dao.impl.TransactionDAOImpl;
import com.rems.transaction.service.TransactionService;
import com.rems.transaction.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.sql.Connection;

public class LifecycleIntegrationTest {

    private static final TransactionManager txManager = new TransactionManager();

    private static final PropertyDAO propertyDAO = new PropertyDAOImpl();
    private static final BookingDAO bookingDAO = new BookingDAOImpl();
    private static final TransactionDAO transactionDAO = new TransactionDAOImpl();

    private static final BookingService bookingService =
            new BookingServiceImpl(txManager);

    private static final TransactionService transactionService =
            new TransactionServiceImpl(txManager);

    public static void main(String[] args) {

        System.out.println("===== START LIFECYCLE TEST =====");

        try {

            System.out.println("[TEST] create property");
            Long propertyId = createTestProperty();

            System.out.println("[TEST] create booking");
            Long customerId = 1L;
            Long bookingId = createTestBooking(propertyId, customerId);

            System.out.println("[TEST] accept booking");
            Long staffId = 2L;
            bookingService.acceptBooking(bookingId, staffId);

            System.out.println("[TEST] complete transaction");
            transactionService.completeTransaction(bookingId, staffId);

            System.out.println("[TEST] verify state");
            verifyFinalState(propertyId, bookingId);

            System.out.println("✅ TEST PASSED");

        } catch (Exception e) {

            System.out.println("❌ ERROR OCCURRED");
            e.printStackTrace();

        }

        System.out.println("===== END TEST =====");
    }

    private static Long createTestProperty() {

        try (Connection conn = DBUtil.getConnection()) {

            Long propertyId = propertyDAO.insertTestProperty(
                    conn,
                    "TEST HOUSE",
                    new BigDecimal("1000000000")
            );

            System.out.println("Property created: " + propertyId);
            return propertyId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Long createTestBooking(Long propertyId, Long customerId) {

        try (Connection conn = DBUtil.getConnection()) {

            Long bookingId = bookingDAO.insertTestBooking(conn, propertyId, customerId);

            System.out.println("Booking created: " + bookingId);
            return bookingId;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void verifyFinalState(Long propertyId, Long bookingId) {

        try (Connection conn = DBUtil.getConnection()) {

            String bookingStatus = bookingDAO.getBookingStatus(conn, bookingId);
            String propertyStatus = propertyDAO.getPropertyStatus(conn, propertyId);
            String txStatus = transactionDAO.getTransactionStatusByBookingId(conn, bookingId);

            assertEquals("COMPLETED", bookingStatus, "Booking status");
            assertEquals("SOLD", propertyStatus, "Property status");
            assertEquals("COMPLETED", txStatus, "Transaction status");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertEquals(String expected, String actual, String field) {

        if (!expected.equals(actual)) {
            throw new RuntimeException(
                    "❌ Assertion failed for " + field +
                            " | expected: " + expected +
                            " but was: " + actual
            );
        }

        System.out.println("✔ " + field + " = " + actual);
    }
}