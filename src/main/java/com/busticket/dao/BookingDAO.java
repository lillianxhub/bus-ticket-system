package com.busticket.dao;

import com.busticket.config.DatabaseConnection;
import com.busticket.models.Booking;
import com.busticket.models.Bus;
import com.busticket.models.Schedule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import java.math.BigDecimal;

public class BookingDAO {
    private Connection connection;

    public BookingDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Booking createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (userID, scheduleID, seatID, travelDate, totalFare, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getSchedule().getScheduleId());
            stmt.setInt(3, booking.getSeatId());
            stmt.setDate(4, Date.valueOf(booking.getTravelDate()));
            stmt.setBigDecimal(5, booking.getTotalFare());
            stmt.setString(6, booking.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }

            return findById(booking.getBookingId());
        }
    }

    public Booking findById(Integer bookingId) throws SQLException {
        String sql = """
        SELECT b.*, s.origin, s.destination, s.departureTime, s.arrivalTime, s.fare,
               bus.busID, bus.busName, bus.busType
        FROM bookings b
        JOIN schedules s ON b.scheduleID = s.scheduleID
        JOIN bus bus ON s.busID = bus.busID
        WHERE b.bookingID = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        }
        return null;
    }


    public List<Booking> findByUserId(Integer userId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE userID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {


        // Create and set Schedule object
        Schedule schedule = new Schedule(
                rs.getInt("scheduleID"),
                rs.getInt("busID"),
                rs.getString("origin"),
                rs.getString("destination"),
                rs.getTime("departureTime").toLocalTime(),
                rs.getTime("arrivalTime").toLocalTime(),
                rs.getBigDecimal("fare")
        );

        // Create and set Bus object
        Bus bus = new Bus(
                rs.getInt("busID"),
                rs.getString("busName"),
                Bus.BusType.valueOf(rs.getString("busType"))
        );

        // Create Booking with the correct constructor parameters
        Booking booking = new Booking();

        // Set the booking ID if it exists (will be 0 for new schedules)
        booking.setBookingId(rs.getInt("bookingID"));

        // Set the schedule and bus objects
        booking.setSchedule(schedule);
        booking.setBus(bus);

        // Try to set optional booking-related fields if they exist
        try {
            if (rs.getObject("userID") != null) {
                booking.setUserId(rs.getInt("userID"));
            }
            if (rs.getObject("seatID") != null) {
                booking.setSeatId(rs.getInt("seatID"));
            }
            if (rs.getObject("travelDate") != null) {
                booking.setTravelDate(rs.getDate("travelDate").toLocalDate());
            }
            if (rs.getObject("totolFare") != null) {
                booking.setTotalFare(rs.getBigDecimal("totolFare"));
            }
            if (rs.getObject("bookingDate") != null) {
                booking.setBookingDate(rs.getTimestamp("bookingDate").toLocalDateTime());
            }
            if (rs.getObject("status") != null) {
                booking.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            // Ignore missing columns for search results
        }

        return booking;
    }

}