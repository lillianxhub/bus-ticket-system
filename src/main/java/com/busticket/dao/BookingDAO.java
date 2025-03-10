package com.busticket.dao;

import com.busticket.models.Booking;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class BookingDAO {
    private Connection connection; // Assume this is initialized through constructor

    public BookingDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Booking> searchAvailableSchedules(String from, String to, LocalDate date, String busType) throws SQLException {
        List<Booking> availableSchedules = new ArrayList<>();

        String sql = """
        SELECT b.*, s.*, bus.* 
        FROM schedules s
        JOIN buses bus ON s.busID = bus.busID
        LEFT JOIN bookings b ON s.scheduleID = b.scheduleID AND b.travelDate = ?
        WHERE s.origin = ? 
        AND s.destination = ?
        AND (?::text IS NULL OR bus.busType = ?::text)
        AND (
            b.bookingID IS NULL 
            OR (
                SELECT COUNT(*) 
                FROM seats seat 
                WHERE seat.bookingID = b.bookingID
            ) < bus.totalSeats
        )
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, from);
            stmt.setString(3, to);
            stmt.setString(4, busType);
            stmt.setString(5, busType);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    availableSchedules.add(booking);
                }
            }
        }

        return availableSchedules;
    }

    public Booking save(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (id, scheduleID, travelDate, totalFare) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getScheduleId());
            stmt.setDate(3, Date.valueOf(booking.getTravelDate()));
            stmt.setBigDecimal(4, booking.getTotalFare());

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
        }
        return booking;
    }

    public Booking findById(Integer bookingId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE bookingID = ?";

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
        String sql = "SELECT * FROM bookings WHERE id = ?";

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
        return new Booking(
                rs.getInt("bookingID"),
                rs.getInt("id"),
                rs.getInt("scheduleID"),
                rs.getDate("travelDate").toLocalDate(),
                rs.getTimestamp("bookingDate").toLocalDateTime(),
                rs.getBigDecimal("totalFare")
        );
    }
}