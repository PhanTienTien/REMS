package com.rems.transaction.model;

import com.rems.common.constant.PropertyType;
import com.rems.common.constant.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private Long id;
    private Long bookingId;
    private Long propertyId;
    private Long customerId;
    private PropertyType type;
    private BigDecimal amount;
    private BigDecimal propertyPriceSnapshot;
    private String propertyTitleSnapshot;
    private String customerNameSnapshot;
    private TransactionStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private Long processedBy;
    private String processedByName;

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
    public PropertyType getType() {
        return type;
    }
    public void setType(PropertyType type) {
        this.type = type;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
    public Long getProcessedBy() {
        return processedBy;
    }
    public void setProcessedBy(Long processedBy) {
        this.processedBy = processedBy;
    }
    public String getProcessedByName() {
        return processedByName;
    }
    public void setProcessedByName(String processedByName) {
        this.processedByName = processedByName;
    }

    public String getCreatedAtFormatted() {
        return createdAt.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
    }
}
