package com.busticket.utils;

import org.mindrot.jbcrypt.*;

public class PasswordUtils {
    // Work factor for BCrypt (higher = more secure but slower)
    private static final int WORKLOAD = 12;

    /**
     * Hash a password using BCrypt
     * @param plainTextPassword The password to hash
     * @return The hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        //Generate a salt and hash the password
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    /**
     * Verify a password against a stored hash
     * @param plainTextPassword The password to check
     * @param hashedPassword The stored hash to check against
     * @return true if the password matches, false otherwise
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Log the error if needed
            return false;
        }
    }

    /**
     * Validate password strength
     * @param password The password to validate
     * @return true if password meets strength requirements, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (isSpecialCharacter(c)) hasSpecialChar = true;
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    /**
     * Check if a character is a special character
     * @param c The character to check
     * @return true if the character is a special character
     */
    private static boolean isSpecialCharacter(char c) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        return specialChars.indexOf(c) != -1;
    }

    /**
     * Get password strength requirements as a formatted string
     * @return String containing password requirements
     */
    public static String getPasswordRequirements() {
        return "Password must contain:\n" +
                "- At least 8 characters\n" +
                "- At least one uppercase letter\n" +
                "- At least one lowercase letter\n" +
                "- At least one number\n" +
                "- At least one special character (!@#$%^&*()_+-=[]{}|;:,.<>?)";
    }
}