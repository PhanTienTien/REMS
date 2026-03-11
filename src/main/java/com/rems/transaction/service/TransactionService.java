package com.rems.transaction.service;

import com.rems.transaction.model.Transaction;

import java.util.List;

public interface TransactionService {
    Long completeTransaction(Long bookingId, Long staffId);

    List<Transaction> findAll();
}
