package com.rems.report.model.dto;

import java.math.BigDecimal;

public class ReportDTO {

    private String month;
    private BigDecimal revenue;
    private long transactions;

    public ReportDTO() {}

    public ReportDTO(String month, BigDecimal revenue, long transactions) {
        this.month = month;
        this.revenue = revenue;
        this.transactions = transactions;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public long getTransactions() {
        return transactions;
    }

    public void setTransactions(long transactions) {
        this.transactions = transactions;
    }
}
