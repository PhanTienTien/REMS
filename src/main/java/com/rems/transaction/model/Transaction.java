package com.rems.transaction.model;

import com.rems.common.constant.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private Long id;
    private Long bookingId;
    private Long propertyId;
    private Long customerId;
    private BigDecimal propertyPriceSnapshot;
    private String propertyTitleSnapshot;
    private String customerNameSnapshot;
    private TransactionStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    public Transaction() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getBookingId() {
        return bookingId;
    }
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    public Long getPropertyId() {
        return propertyId;
    }
    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public BigDecimal getPropertyPriceSnapshot() {
        return propertyPriceSnapshot;
    }
    public void setPropertyPriceSnapshot(BigDecimal propertyPriceSnapshot) {
        this.propertyPriceSnapshot = propertyPriceSnapshot;
    }
    public String getPropertyTitleSnapshot() {
        return propertyTitleSnapshot;
    }
    public void setPropertyTitleSnapshot(String propertyTitleSnapshot) {
        this.propertyTitleSnapshot = propertyTitleSnapshot;
    }
    public String getCustomerNameSnapshot() {
        return customerNameSnapshot;
    }
    public void setCustomerNameSnapshot(String customerNameSnapshot) {
        this.customerNameSnapshot = customerNameSnapshot;
    }
    public TransactionStatus getStatus() {
        return status;
    }
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
