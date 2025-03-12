package com.busticket.models;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Schedule {
    private Integer scheduleId;
    private Integer busId;
    private String origin;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private BigDecimal fare;

    // Default constructor
    public Schedule() {
    }

    // Parameterized constructor
    public Schedule(Integer scheduleId, Integer busId, String origin,
                    String destination, LocalTime departureTime,
                    LocalTime arrivalTime, BigDecimal fare) {
        this.scheduleId = scheduleId;
        this.busId = busId;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
    }

    // Getters and Setters
    public Integer getScheduleId() {
        System.out.println("Schedule Id: " + scheduleId);
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", busId=" + busId +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", fare=" + fare +
                '}';
    }
}