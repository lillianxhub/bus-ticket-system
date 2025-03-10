package com.busticket.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private Integer bookingId;
    private Integer userId;
    private Integer scheduleId;
    private LocalDate travelDate;
    private LocalDateTime bookingDate;
    private BigDecimal totalFare;

    // Default constructor
    public Booking() {
    }

    // Parameterized constructor
    public Booking(Integer bookingId, Integer userId, Integer scheduleId,
                   LocalDate travelDate, LocalDateTime bookingDate, BigDecimal totalFare) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.travelDate = travelDate;
        this.bookingDate = bookingDate;
        this.totalFare = totalFare;
    }

    // Getters and Setters
    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", scheduleId=" + scheduleId +
                ", travelDate=" + travelDate +
                ", bookingDate=" + bookingDate +
                ", totalFare=" + totalFare +
                '}';
    }
}