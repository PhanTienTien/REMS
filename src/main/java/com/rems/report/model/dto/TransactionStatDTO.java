package com.rems.report.model.dto;

public class TransactionStatDTO {

    private String month;
    private long totalTransactions;

    public TransactionStatDTO() {}

    public TransactionStatDTO(String month, long totalTransactions) {
        this.month = month;
        this.totalTransactions = totalTransactions;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}
