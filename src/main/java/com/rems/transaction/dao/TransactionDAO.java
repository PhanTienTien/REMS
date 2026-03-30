package com.rems.transaction.dao;

import com.rems.common.constant.TransactionStatus;
import com.rems.transaction.model.Transaction;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface TransactionDAO {

    Optional<Transaction> findById(Connection conn, Long id);

    Long insert(Connection conn, Transaction tx);

    Optional<Transaction> findByBookingId(Connection conn, Long bookingId);

    List<Transaction> findAll(Connection conn);

    List<Transaction> findByStatus(Connection conn,
                                   TransactionStatus status);

    Optional<Transaction> findByIdForUpdate(Connection conn, Long id);

    void updateStatus(Connection conn,
                      Long id,
                      TransactionStatus status,
                      Long staffId);

    List<Transaction> findByCustomer(Connection conn,
                                     Long customerId);

    List<Transaction> search(Connection conn,
                             String keyword,
                             TransactionStatus status,
                             String sortBy,
                             String sortDir,
                             int offset,
                             int limit);

    int count(Connection conn,
              String keyword,
              TransactionStatus status);

    Optional<Transaction> findByIdForStaff(Connection conn,
                                           Long id,
                                           Long staffId);

    List<Transaction> searchByStaff(Connection conn,
                                    Long staffId,
                                    String keyword,
                                    TransactionStatus status,
                                    String sortBy,
                                    String sortDir,
                                    int offset,
                                    int limit);

    int countByStaff(Connection conn,
                     Long staffId,
                     String keyword,
                     TransactionStatus status);

}
