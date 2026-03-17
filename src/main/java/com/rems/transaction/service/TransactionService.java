package com.rems.transaction.service;

import com.rems.transaction.model.Transaction;

import java.sql.Connection;
import java.util.List;

public interface TransactionService {

    public Long createTransaction(Connection conn, Long bookingId);

    Long completeTransaction(Long bookingId, Long staffId);

    List<Transaction> findAll();

    List<Transaction> findByStatus(String status);

    Transaction findById(Long id);

    List<Transaction> getByCustomer(Long customerId);
}
