package com.busticket.controllers;

import com.busticket.dao.BookingDAO;
import com.busticket.models.Booking;
import com.busticket.models.Schedule;
import com.busticket.models.User;
import com.busticket.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class TicketViewController {
    @FXML
    private Label bookingIdLabel;
    @FXML
    private Label passengerLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label busLabel;
    @FXML
    private Label seatLabel;
    @FXML
    private Label amountLabel;

    private BookingDAO bookingDAO;

    public TicketViewController() {
        this.bookingDAO = new BookingDAO();
    }

    @FXML
    public void initialize() {
        try {
            // Retrieve booking IDs and selected seats from session using correct key "bookingIds"
            Object bookingIdsObj = SceneManager.getSessionData("bookingIds");  // Changed from "bookingId" to "bookingIds"
            Object selectedSeatsObj = SceneManager.getSessionData("selectedSeats");
            Object userObj = SceneManager.getSessionData("loggedInUser");

            BigDecimal farePerSeat = (BigDecimal) SceneManager.getSessionData("fare");

            // Proper type checking and casting
            Set<Integer> bookingIds = null;
            Set<String> selectedSeats = null;
            User loggedInUser = null;

            if (bookingIdsObj instanceof Set<?>) {
                bookingIds = (Set<Integer>) bookingIdsObj;
            }
            if (selectedSeatsObj instanceof Set<?>) {
                selectedSeats = (Set<String>) selectedSeatsObj;
            }
            if (userObj instanceof User) {
                loggedInUser = (User) userObj;
            }

            System.out.println("Booking IDs: " + bookingIds + "\nSelected seats: " + selectedSeats + "\nUser: " + loggedInUser);

            if (bookingIds == null || bookingIds.isEmpty()) {
                showError("No booking IDs found");
                return;
            }

            if (loggedInUser == null) {
                showError("No user information found");
                return;
            }

            if (selectedSeats == null || selectedSeats.isEmpty()) {
                showError("No seat information found");
                return;
            }

            // Get the first booking ID (assuming all bookings are related)
            Integer firstBookingId = bookingIds.iterator().next();
            Booking booking = bookingDAO.findById(firstBookingId);

            if (booking == null) {
                showError("Booking not found");
                return;
            }

            // Format the booking details
            Schedule schedule = booking.getSchedule();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            BigDecimal totalFare = farePerSeat.multiply(new BigDecimal(selectedSeats.size()));

            // Populate the labels
            bookingIdLabel.setText(String.join(", ",
                    bookingIds.stream().map(String::valueOf).toList()));
            passengerLabel.setText(loggedInUser.getFullName());
            fromLabel.setText(schedule.getOrigin());
            toLabel.setText(schedule.getDestination());
            dateLabel.setText(booking.getTravelDate().format(dateFormatter));
            busLabel.setText(booking.getBus().getBusName() + " (" +
                    booking.getBus().getBusType().toString() + ")");
            seatLabel.setText(String.join(", ", selectedSeats));
            amountLabel.setText(String.format("%.2f", totalFare));
            System.out.println("Amount Fare: " + totalFare);

        } catch (SQLException e) {
            showError("Error loading booking details: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            showError("Error processing session data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBack() {
        SceneManager.switchScene("/fxml/booking.fxml");
    }
}