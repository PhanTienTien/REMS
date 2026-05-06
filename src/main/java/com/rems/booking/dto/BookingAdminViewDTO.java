package com.rems.booking.dto;

import com.rems.common.constant.BookingStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingAdminViewDTO {

    private Long bookingId;

    private String propertyTitle;

    private String customerName;

    private BookingStatus status;
    private LocalDateTime scheduledAt;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtFormatted() {
        return createdAt.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
    }
}
