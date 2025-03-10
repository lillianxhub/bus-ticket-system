package com.busticket.services;

import com.busticket.config.DatabaseConnection;
import com.busticket.dao.BookingDAO;
import com.busticket.models.Booking;
import com.busticket.models.Bus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class BookingService {
    private BookingDAO bookingDAO;

    public BookingService() {
        // Initialize DAO (assume proper connection handling)
        this.bookingDAO = new BookingDAO();
    }

    public Booking createBooking(Booking booking) {
        try {
            // Validate booking data
            validateBooking(booking);

            // Save booking
            return bookingDAO.save(booking);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating booking: " + e.getMessage());
        }
    }

    public List<String> getAllLocations() {
        // Return list of available locations
        List<String> allLocations = bookingDAO.getLocations();
        return allLocations;
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
            return bookingDAO.searchSchedules(
                    from.trim(),
                    to.trim(),
                    date,
                    normalizedBusType
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error searching for available schedules: " + e.getMessage());
        }
    }

    public Booking getBookingById(Integer bookingId) {
        try {
            return bookingDAO.findById(bookingId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching booking: " + e.getMessage());
        }
    }

    public List<Booking> getUserBookings(Integer userId) {
        try {
            return bookingDAO.findByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user bookings: " + e.getMessage());
        }
    }

    private void validateBooking(Booking booking) {
        if (booking.getUserId() == null || booking.getScheduleId() == null ||
                booking.getTravelDate() == null || booking.getTotalFare() == null) {
            throw new IllegalArgumentException("All required booking fields must be filled");
        }

        if (booking.getTravelDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Travel date cannot be in the past");
        }
    }
}