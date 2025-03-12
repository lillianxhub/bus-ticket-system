package com.busticket.controllers;

import com.busticket.dao.BookingDAO;
import com.busticket.models.Booking;
import com.busticket.models.Schedule;
import com.busticket.models.User;
import com.busticket.services.BookingService;
import com.busticket.utils.SceneManager;
import com.busticket.utils.AlertHelper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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

    private AlertHelper alertHelper;

    public TicketViewController() {
        this.alertHelper = new AlertHelper();
    }

    @FXML
    public void initialize() {
        try {
            Object bookingIdsObj = SceneManager.getSessionData("bookingIds");
            Object selectedSeatsObj = SceneManager.getSessionData("selectedSeats");
            Object userObj = SceneManager.getSessionData("loggedInUser");
            BigDecimal farePerSeat = (BigDecimal) SceneManager.getSessionData("fare");

            // Proper type checking and casting
            Set<Booking> bookings = null;
            Set<String> selectedSeats = null;
            User loggedInUser = null;

            if (bookingIdsObj instanceof Set<?>) {
                bookings = (Set<Booking>) bookingIdsObj;
            }
            if (selectedSeatsObj instanceof Set<?>) {
                selectedSeats = (Set<String>) selectedSeatsObj;
            }
            if (userObj instanceof User) {
                loggedInUser = (User) userObj;
            }

            if (bookings == null || bookings.isEmpty()) {
                alertHelper.showErrorAlert("Error:","No booking information found");
                return;
            }

            if (loggedInUser == null) {
                alertHelper.showErrorAlert("Error:","No user information found");
                return;
            }

            if (selectedSeats == null || selectedSeats.isEmpty()) {
                alertHelper.showErrorAlert("Error:","No seat information found");
                return;
            }

            // Get the first booking (assuming all bookings are related)
            Booking booking = bookings.iterator().next();

            // Format the booking details
            Schedule schedule = booking.getSchedule();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            BigDecimal totalFare = farePerSeat.multiply(new BigDecimal(selectedSeats.size()));

            // Populate the labels
            bookingIdLabel.setText(String.join(", ",
                    bookings.stream().map(b -> String.valueOf(b.getBookingId())).toList()));
            passengerLabel.setText(loggedInUser.getFullName());
            fromLabel.setText(schedule.getOrigin());
            toLabel.setText(schedule.getDestination());
            dateLabel.setText(booking.getTravelDate().format(dateFormatter));
            busLabel.setText(booking.getBus().getBusName() + " (" +
                    booking.getBus().getBusType().toString() + ")");
            seatLabel.setText(String.join(", ", selectedSeats));
            amountLabel.setText(String.format("%.2f", totalFare));
        } catch (ClassCastException e) {
            alertHelper.showErrorAlert("Error: ","Error processing session data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        SceneManager.switchScene("/fxml/booking.fxml");
    }
}