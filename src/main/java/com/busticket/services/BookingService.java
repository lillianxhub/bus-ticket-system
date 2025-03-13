package com.busticket.services;

import com.busticket.dao.BookingDAO;
import com.busticket.models.Booking;
import com.busticket.models.Bus;
import com.busticket.utils.ValidationUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class BookingService {
    private BookingDAO bookingDAO;

    public BookingService() {
        // Initialize DAO
        this.bookingDAO = new BookingDAO();
    }

    public Booking createBooking(Booking booking) {
        try {
            // Validate booking data
            ValidationUtils.validateBooking(booking);

            // Set default status if not set
            if (booking.getStatus() == null) {
                booking.setStatus("pending");
            }

            // Create booking in database
            Booking createdBooking = bookingDAO.createBooking(booking);

            if (createdBooking == null) {
                throw new RuntimeException("Failed to create booking");
            }

            return createdBooking;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating booking: " + e.getMessage());
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
}