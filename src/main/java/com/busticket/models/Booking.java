package com.busticket.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private Integer bookingId;
    private Integer userId;
    private Integer scheduleId;
    private Integer seatId;
    private LocalDate travelDate;
    private LocalDateTime bookingDate;
    private BigDecimal totalFare;
    private String status;
    private Bus bus;
    private Schedule schedule;
    private int bookedSeats;

    // Default constructor
    public Booking() {
    }

    // Parameterized constructor
    public Booking(Integer userId, Integer scheduleId, Integer seatId, LocalDate travelDate, BigDecimal totalFare
    ) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.seatId = seatId;
        this.travelDate = travelDate;
        this.totalFare = totalFare;
        this.status = "Confirmed";
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

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
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

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    // New getter and setter for bookedSeats
    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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