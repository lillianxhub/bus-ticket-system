package com.busticket.models;

public class Seat {
    private Integer seatId;
    private Integer bookingId;
    private String seatNumber;

    // Default constructor
    public Seat() {
    }

    // Parameterized constructor
    public Seat(Integer seatId, Integer bookingId, String seatNumber) {
        this.seatId = seatId;
        this.bookingId = bookingId;
        this.seatNumber = seatNumber;
    }

    // Getters and Setters
    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", bookingId=" + bookingId +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}