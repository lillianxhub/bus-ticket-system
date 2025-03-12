package com.busticket.dao;

import com.busticket.config.DatabaseConnection;
import com.busticket.models.Booking;
import com.busticket.models.Bus;
import com.busticket.models.Schedule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    private Connection connection;

    public ScheduleDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> searchSchedules(String from, String to, LocalDate date, String busType) throws SQLException {
        List<Booking> schedules = new ArrayList<>();
        String sql = "SELECT s.*, b.* FROM schedules s " +
                "JOIN bus b ON s.busID = b.busID " +
                "WHERE s.origin = ? AND s.destination = ?";

        if (busType != null) {
            sql += " AND b.busType = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, from);
            stmt.setString(2, to);
            if (busType != null) {
                stmt.setString(3, busType);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapScheduleSearchResults(rs));
                }
            }
        }
        return schedules;
    }

    public List<String> getLocations() {
        // Move the implementation from BookingDAO.getLocations here
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT origin FROM schedules " +
                "UNION " +
                "SELECT DISTINCT destination FROM schedules";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                locations.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching locations: " + e.getMessage());
        }
        return locations;
    }

    private Booking mapScheduleSearchResults(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        Schedule schedule = new Schedule();
        Bus bus = new Bus();

        // Map Schedule fields
        schedule.setScheduleId(rs.getInt("scheduleID"));
        schedule.setOrigin(rs.getString("origin"));
        schedule.setDestination(rs.getString("destination"));
        schedule.setDepartureTime(rs.getTime("departureTime").toLocalTime());
        schedule.setArrivalTime(rs.getTime("arrivalTime").toLocalTime());
        schedule.setFare(rs.getBigDecimal("fare"));

        // Map Bus fields
        bus.setBusId(rs.getInt("busID"));
        bus.setBusName(rs.getString("busName"));
        bus.setBusType(Bus.BusType.valueOf(rs.getString("busType")));

        // Set the schedule and bus in booking
        booking.setSchedule(schedule);
        booking.setBus(bus);

        return booking;
    }
}