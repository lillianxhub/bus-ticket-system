package com.busticket.models;

public class Seat {
    private Integer seatId;
    private String seatCode;

    // Default constructor
    public Seat() {
    }

    // Parameterized constructor
    public Seat(Integer seatId, String seatNumber) {
        this.seatId = seatId;
        this.seatCode = seatNumber;
    }

    // Getters and Setters
    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", seatCode='" + seatCode + '\'' +
                '}';
    }
}