package com.rems.dashboard.dto;

import java.util.List;
import java.util.Map;

public class AdminDashboardDTO {

    private List<DashboardCardDTO> cards;
    private RevenueChartDTO revenueChart;
    private List<RecentTransactionDTO> recentTransactions;
    private Map<String, Long> propertyStats;
    private Map<String, Long> transactionStats;
    private long draftProperties;
    private long pendingTransactions;
    private long reservedTooLong;

    public long getReservedTooLong() {
        return reservedTooLong;
    }

    public void setReservedTooLong(long reservedTooLong) {
        this.reservedTooLong = reservedTooLong;
    }

    public long getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(long pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public long getDraftProperties() {
        return draftProperties;
    }

    public void setDraftProperties(long draftProperties) {
        this.draftProperties = draftProperties;
    }

    public Map<String, Long> getTransactionStats() {
        return transactionStats;
    }

    public void setTransactionStats(Map<String, Long> transactionStats) {
        this.transactionStats = transactionStats;
    }

    public Map<String, Long> getPropertyStats() {
        return propertyStats;
    }

    public void setPropertyStats(Map<String, Long> propertyStats) {
        this.propertyStats = propertyStats;
    }

    public List<DashboardCardDTO> getCards() {
        return cards;
    }

    public void setCards(List<DashboardCardDTO> cards) {
        this.cards = cards;
    }

    public RevenueChartDTO getRevenueChart() {
        return revenueChart;
    }

    public void setRevenueChart(RevenueChartDTO revenueChart) {
        this.revenueChart = revenueChart;
    }

    public List<RecentTransactionDTO> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<RecentTransactionDTO> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }
}