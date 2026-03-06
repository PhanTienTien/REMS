package com.rems.dashboard.dto;

import java.util.List;

public class StaffDashboardDTO {

    private List<DashboardCardDTO> cards;
    private List<RecentTransactionDTO> recentTransactions;

    public List<DashboardCardDTO> getCards() {
        return cards;
    }

    public void setCards(List<DashboardCardDTO> cards) {
        this.cards = cards;
    }

    public List<RecentTransactionDTO> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<RecentTransactionDTO> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }
}
