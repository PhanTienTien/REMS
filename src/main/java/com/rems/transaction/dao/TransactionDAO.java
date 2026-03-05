package com.rems.transaction.dao;

import com.rems.transaction.model.Transaction;

import java.sql.Connection;
import java.util.Optional;

public interface TransactionDAO {

    Long insert(Connection conn, Transaction tx);

    Optional<Transaction> findByBookingId(Connection conn, Long bookingId);

    //test
    String getTransactionStatusByBookingId(Connection conn, Long bookingId);

}