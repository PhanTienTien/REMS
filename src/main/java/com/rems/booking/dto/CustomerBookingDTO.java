package com.rems.booking.dto;

import java.time.LocalDateTime;

public class CustomerBookingDTO {

    private Long bookingId;

    private String propertyTitle;

    private String status;

    private String note;

    private LocalDateTime createdAt;

    public Long getBookingId() {
        return bookingId;
    }
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    public String getPropertyTitle() {
        return propertyTitle;
    }
    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
