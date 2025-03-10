package com.busticket.models;

public class Bus {
    private Integer busId;
    private String busName;
    private BusType busType;
    private int totalSeats;

    // Enum for bus types
    public enum BusType {
        GOLD_CLASS,
        FIRST_CLASS
    }

    // Default constructor
    public Bus() {
    }

    // Parameterized constructor
    public Bus(Integer busId, String busName, BusType busType, int totalSeats) {
        this.busId = busId;
        this.busName = busName;
        this.busType = busType;
        this.totalSeats = totalSeats;
    }

    // Getters and Setters
    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busId=" + busId +
                ", busName='" + busName + '\'' +
                ", busType=" + busType +
                ", totalSeats=" + totalSeats +
                '}';
    }
}