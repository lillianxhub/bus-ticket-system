package com.busticket.services;

import com.busticket.dao.ScheduleDAO;
import com.busticket.models.Booking;
import com.busticket.models.Bus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ScheduleService {
    private ScheduleDAO scheduleDAO;

    public ScheduleService() {
        this.scheduleDAO = new ScheduleDAO();
    }

    public List<Booking> searchAvailableSchedules(String from, String to, LocalDate date, String busType) {
        try {
            // Input validation
            if (from == null || from.trim().isEmpty()) {
                throw new IllegalArgumentException("Origin location cannot be empty");
            }
            if (to == null || to.trim().isEmpty()) {
                throw new IllegalArgumentException("Destination location cannot be empty");
            }
            if (date == null) {
                throw new IllegalArgumentException("Travel date cannot be null");
            }
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Travel date cannot be in the past");
            }

            // Convert busType to proper format if needed
            String normalizedBusType = null;
            if (busType != null && !busType.trim().isEmpty()) {
                try {
                    Bus.BusType.valueOf(busType.toUpperCase());
                    normalizedBusType = busType.toUpperCase();
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid bus type: " + busType);
                }
            }

            // Search for available schedules
            return scheduleDAO.searchSchedules(
                    from.trim(),
                    to.trim(),
                    date,
                    normalizedBusType
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for available schedules: " + e.getMessage());
        }
    }

    public List<String> getAllLocations() {
        return scheduleDAO.getLocations();
    }
}