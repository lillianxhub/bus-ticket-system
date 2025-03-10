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

    public List<Booking> searchSchedules(String from, String to, LocalDate date, String busType) throws SQLException {
        List<Booking> Schedules = new ArrayList<>();

        String sql = """
        SELECT
            b.*, s.*, bus.*,
            COALESCE((
                SELECT COUNT(*)
                FROM seats seat
                WHERE seat.bookingID = b.bookingID
                ), 0) as bookedSeats
            FROM schedules s
            JOIN bus bus ON s.busID = bus.busID
            LEFT JOIN bookings b ON s.scheduleID = b.scheduleID AND b.travelDate = ?
            WHERE s.origin = ?
            AND s.destination = ?
            AND (bus.busType = ?)
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

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    Schedules.add(booking);
                }
            }
        }

        return Schedules;
    }

    public List<String> getLocations() {
        List<String> locations = new ArrayList<>();
        String sql = """
        SELECT DISTINCT location 
        FROM (
            SELECT origin as location FROM schedules
            UNION
            SELECT destination as location FROM schedules
        ) all_locations 
        ORDER BY location""";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching locations", e);
        }

        return locations;
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
        Booking booking = new Booking(
                rs.getInt("bookingID"),
                rs.getInt("id"),
                rs.getInt("scheduleID"),
                rs.getDate("travelDate") != null ? rs.getDate("travelDate").toLocalDate() : null,
                rs.getTimestamp("bookingDate") != null ? rs.getTimestamp("bookingDate").toLocalDateTime() : null,
                rs.getBigDecimal("totalFare")
        );

        // Create and set Bus object
        Bus bus = new Bus(
                rs.getInt("busID"),
                rs.getString("busName"),
                Bus.BusType.valueOf(rs.getString("busType")),
                rs.getInt("totalSeats")
        );
        booking.setBus(bus);

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
        booking.setSchedule(schedule);

        // Set booked seats count
        booking.setBookedSeats(rs.getInt("bookedSeats"));

        return booking;
    }

}