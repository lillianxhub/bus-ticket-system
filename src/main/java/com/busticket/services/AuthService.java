package com.busticket.services;

import com.busticket.dao.UserDAO;
import com.busticket.models.User;
import com.busticket.utils.PasswordUtils;
import com.busticket.utils.ValidationUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean register(String username, String password, String email, String fullName,
                            String phone, String address, String role) {
        try {
            // Check if username or email already exists
            if (userDAO.isUsernameExists(username)) {
                throw new IllegalArgumentException("Username already exists");
            }

            if (userDAO.isEmailExists(email)) {
                throw new IllegalArgumentException("Email already exists");
            }
            if (ValidationUtils.isValidPhone(phone)) {
                throw new IllegalArgumentException("Invalid phone number");
            }

            // Hash the password
            String hashedPassword = PasswordUtils.hashPassword(password);

            // Create user object
            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setAddress(address);
            user.setRole(User.UserRole.valueOf(role));
            user.setCreatedAt(LocalDateTime.now());
            user.setActive(true);

            // Save user to database
            return userDAO.save(user);
        } catch (SQLException e) {
            System.err.println("Database error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error during registration: " + e.getMessage());
            throw e; // Re-throw to be caught by UI layer
        }
    }

    public Optional<User> login(String username, String password) {
        try {
            // Hash the password before checking against database
//            String hashedPassword = PasswordUtils.hashPassword(password);

            // Find user by username
            Optional<User> userOpt = userDAO.findByUsername(username);

            // Verify password if user exists
            if (userOpt.isPresent() && userOpt.get().isActive()) {
                User user = userOpt.get();
                // Use checkPassword instead of rehashing
                if (PasswordUtils.checkPassword(password, user.getPassword())) {
                    return userOpt;
                }
            }


            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}