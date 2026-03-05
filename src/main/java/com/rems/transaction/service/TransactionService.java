package com.rems.transaction.service;

public interface TransactionService {
    Long completeTransaction(Long bookingId, Long staffId);
}
