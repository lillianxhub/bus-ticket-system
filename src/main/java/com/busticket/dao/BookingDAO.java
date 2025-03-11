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
        List<Booking> schedules = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT s.scheduleID, s.origin, s.destination, s.departureTime,
               s.arrivalTime, s.fare, bus.busID, bus.busName, bus.busType
        FROM schedules s
        JOIN bus bus ON s.busID = bus.busID
        WHERE s.origin = ?
        AND s.destination = ?
    """);

        // Add busType condition only if busType is not null
        if (busType != null) {
            sql.append(" AND bus.busType = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            stmt.setString(1, from);
            stmt.setString(2, to);

            // Set parameters based on whether busType is provided
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
            stmt.setInt(2, booking.getSchedule().getScheduleId());
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

    public int createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (id, scheduleID, seatID, travelDate, totalFare, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, booking.getUserId());
            pstmt.setInt(2, booking.getSchedule().getScheduleId());
            pstmt.setInt(3, booking.getSeatId());
            pstmt.setDate(4, Date.valueOf(booking.getTravelDate()));
            pstmt.setBigDecimal(5, booking.getTotalFare());
            pstmt.setString(6, booking.getStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
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
//        Booking booking = new Booking(
//                rs.getInt("id"),             // userId
//                rs.getInt("scheduleID"),     // scheduleId
//                rs.getInt("seatID"),         // seatId
//                rs.getDate("travelDate") != null ? rs.getDate("travelDate").toLocalDate() : null,
//                rs.getBigDecimal("totolFare") // correct column name from DB
//        );
        Booking booking = new Booking();

        // Set the booking ID if it exists (will be 0 for new schedules)
        booking.setBookingId(rs.getInt("bookingID"));

        // Set the schedule and bus objects
        booking.setSchedule(schedule);
        booking.setBus(bus);

        // Try to set optional booking-related fields if they exist
        try {
            if (rs.getObject("id") != null) {
                booking.setUserId(rs.getInt("id"));
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