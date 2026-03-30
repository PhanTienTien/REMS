package com.rems.transaction.service;

import com.rems.transaction.model.Transaction;

import java.sql.Connection;
import java.util.List;

public interface TransactionService {

    Long createTransaction(Connection conn,
                           Long bookingId,
                           Long performedBy,
                           String ipAddress);

    Long completeTransaction(Long bookingId, Long staffId);

    List<Transaction> findAll();

    List<Transaction> findByStatus(String status);

    Transaction findById(Long id);

    Transaction findByIdForStaff(Long id, Long staffId);

    List<Transaction> getByCustomer(Long customerId);

    List<Transaction> searchTransactions(String keyword,
                                         String status,
                                         String sortBy,
                                         String sortDir,
                                         int page,
                                         int size);

    List<Transaction> searchTransactionsByStaff(Long staffId,
                                                String keyword,
                                                String status,
                                                String sortBy,
                                                String sortDir,
                                                int page,
                                                int size);

    int countTransactions(String keyword,
                          String status);

    int countTransactionsByStaff(Long staffId,
                                 String keyword,
                                 String status);

    Long completeTransactionByStaff(Long transactionId, Long staffId);
}
