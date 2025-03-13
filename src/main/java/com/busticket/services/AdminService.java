package com.busticket.services;

import com.busticket.models.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AdminService {

    // Placeholder methods for fetching data (in a real app, these would connect to your data layer)
    public static int fetchTotalUsers() {
        // TODO: Implement this to fetch actual data
        return 125; // Placeholder value
    }

    public static int fetchActiveBuses() {
        // TODO: Implement this to fetch actual data
        return 15; // Placeholder value
    }

    public static int fetchTodayBookings() {
        // TODO: Implement this to fetch actual data
        return 42; // Placeholder value
    }

    public static BigDecimal fetchTotalRevenue() {
        // TODO: Implement this to fetch actual data
        return new BigDecimal("15750.00"); // Placeholder value
    }

    public static List<Booking> fetchRecentBookings() {
        // TODO: Implement this to fetch actual bookings from your data source
        // For demonstration, creating some sample bookings
        Booking booking1 = new Booking();
        booking1.setBookingId(1001);
        booking1.setUserId(101);
        booking1.setTravelDate(LocalDate.now());
        booking1.setStatus("Confirmed");

        Booking booking2 = new Booking();
        booking2.setBookingId(1002);
        booking2.setUserId(102);
        booking2.setTravelDate(LocalDate.now().plusDays(1));
        booking2.setStatus("Pending");

        Booking booking3 = new Booking();
        booking3.setBookingId(1003);
        booking3.setUserId(103);
        booking3.setTravelDate(LocalDate.now().plusDays(2));
        booking3.setStatus("Confirmed");

        return List.of(booking1, booking2, booking3);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
