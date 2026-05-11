package com.rems.property.model;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Property {

    private Long id;
    private String title;
    private String address;
    private String description;
    private PropertyType type;
    private BigDecimal price;
    private PropertyStatus status;
    private Integer viewCount;

    private Long approvedBy;
    private LocalDateTime approvedAt;

    private Long createdBy;
    private LocalDateTime createdAt;

    private Long updatedBy;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Property() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public PropertyType getType() {
        return type;
    }
    public void setType(PropertyType type) {
        this.type = type;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public PropertyStatus getStatus() {
        return status;
    }
    public void setStatus(PropertyStatus status) {
        this.status = status;
    }
    public Integer getViewCount() {
        return viewCount;
    }
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    public Long getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    public Long getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }
    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Long getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
