package com.rems.dashboard.dto;

public class StaffDashboardDTO {

    private long myDraftProperties;
    private long myActiveProperties;
    private long myTransactions;

    public long getMyDraftProperties() {
        return myDraftProperties;
    }

    public void setMyDraftProperties(long myDraftProperties) {
        this.myDraftProperties = myDraftProperties;
    }

    public long getMyActiveProperties() {
        return myActiveProperties;
    }

    public void setMyActiveProperties(long myActiveProperties) {
        this.myActiveProperties = myActiveProperties;
    }

    public long getMyTransactions() {
        return myTransactions;
    }

    public void setMyTransactions(long myTransactions) {
        this.myTransactions = myTransactions;
    }
}