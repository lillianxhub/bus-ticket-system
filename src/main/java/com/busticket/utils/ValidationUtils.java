package com.busticket.utils;

import com.busticket.models.Booking;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationUtils {

    // Regular expressions for validation
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9_]{3,20}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(0\\d{9}|\\+66\\d{9})$");

    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates an email address
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates a username
     *
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Validates a password
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validates a phone number
     *
     * @param phone the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Empty phone can be valid
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validates if a string represents a valid number
     *
     * @param str the string to validate
     * @return true if the string is a valid number, false otherwise
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if a string represents a valid integer
     *
     * @param str the string to validate
     * @return true if the string is a valid integer, false otherwise
     */
    public static boolean isInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if a string represents a positive number
     *
     * @param str the string to validate
     * @return true if the string is a valid positive number, false otherwise
     */
    public static boolean isPositiveNumber(String str) {
        if (!isNumeric(str)) {
            return false;
        }
        return Double.parseDouble(str) > 0;
    }

    /**
     * Validates if a string represents a positive integer
     *
     * @param str the string to validate
     * @return true if the string is a valid positive integer, false otherwise
     */
    public static boolean isPositiveInteger(String str) {
        if (!isInteger(str)) {
            return false;
        }
        return Integer.parseInt(str) > 0;
    }

    public static void validateBooking(Booking booking) {
        if (booking.getUserId() == null) {
            throw new IllegalArgumentException("User ID must be provided");
        }
        if (booking.getSchedule() == null || booking.getSchedule().getScheduleId() == null) {
            throw new IllegalArgumentException("Schedule must be provided");
        }
        if (booking.getSeatId() == null) {
            throw new IllegalArgumentException("Seat must be selected");
        }
        if (booking.getTravelDate() == null) {
            throw new IllegalArgumentException("Travel date must be provided");
        }
        if (booking.getTotalFare() == null) {
            throw new IllegalArgumentException("Total fare must be provided");
        }
        if (booking.getTravelDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Travel date cannot be in the past");
        }
    }
}