package com.busticket.controllers;

import java.sql.*;

import com.busticket.config.DatabaseConnection;

import com.busticket.dao.BookingDAO;
import com.busticket.dao.SeatDAO;
import com.busticket.models.Booking;
import com.busticket.models.Schedule;
import com.busticket.models.User;
import com.busticket.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;  // Changed from java.awt.Label
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SeatSelectionController {

    @FXML
    public AnchorPane SeatSelectionPane;

    @FXML
    public GridPane seatingGrid;

    @FXML
    private Label busNameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ListView<String> selectedSeatsListView;

    @FXML
    private Label totalAmountLabel;

    private BookingDAO bookingDAO;
    private SeatDAO seatDAO;

    private final ObservableList<String> selectedSeats = FXCollections.observableArrayList();
    private final Set<String> selectedSeatsSet = new HashSet<>();


    public SeatSelectionController() {
        this.bookingDAO = new BookingDAO();
        this.seatDAO = new SeatDAO();
    }

    @FXML
    public void initialize() {
        try {
            // Your existing initialization code
            Integer selectedSchedule = (Integer) SceneManager.getSessionData("selectedSchedule");
            String busName = (String) SceneManager.getSessionData("selectedBus");


            User loggedInUser = (User) SceneManager.getSessionData("loggedInUser");
            Integer userID = loggedInUser != null ? loggedInUser.getId() : null;
            Integer scheduleID = (Integer) SceneManager.getSessionData("scheduleID");
            String dateStr = (String) SceneManager.getSessionData("selectedDate");

            BigDecimal farePerSeat = (BigDecimal) SceneManager.getSessionData("fare");

            totalAmountLabel.setText("฿0.00");

            // Debug logging
            System.out.println("Selected seats: " + selectedSeatsSet + "\nFare per seat: " + farePerSeat +
                    "\nDate: " + dateStr + "\nscheduleID: " + scheduleID + "\nUserID: " + userID);

            // Safely set bus name
            if (busNameLabel != null) {
                busNameLabel.setText(busName != null ? busName : "Not selected");
            }

            // Safely set date
            if (dateLabel != null) {
                dateLabel.setText(dateStr != null ? dateStr : "Not selected");
            }

            // Set up listener for selected seats to update total fare automatically
            selectedSeats.addListener((ListChangeListener<String>) change -> {
                BigDecimal totalFare = farePerSeat.multiply(BigDecimal.valueOf(selectedSeats.size()));
                totalAmountLabel.setText("$" + totalFare);
            });

            // Initialize ListView
            if (selectedSeatsListView != null) {
                selectedSeatsListView.setItems(selectedSeats);
            } else {
                System.err.println("Warning: selectedSeatsListView not properly initialized from FXML");
            }

            // Validate required data
            if (selectedSchedule == null || busName == null || dateStr == null || loggedInUser == null) {
                showAlert("Error", "Missing required booking information. Please try again.");
                handleBack(null);
                return;
            }

            // Update seat colors based on availability
            LocalDate travelDate = Date.valueOf(dateStr).toLocalDate();
            updateSeatAvailability(scheduleID, travelDate);

        } catch (Exception e) {
            System.err.println("Error initializing seat selection: " + e.getMessage());
            showAlert("Error", "Failed to initialize seat selection. Please try again.");
            handleBack(null);
        }
    }

    private void updateSeatAvailability(Integer scheduleID, LocalDate travelDate) {
        if (seatingGrid == null) {
            System.err.println("Warning: seatingGrid not properly initialized from FXML");
            return;
        }

        seatingGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                String seatCode = seatButton.getText();

                try {
                    int seatID = convertseatCodeToID(seatCode);
                    boolean isAvailable = seatDAO.isSeatAvailable(scheduleID, seatID, travelDate);

                    if (!isAvailable) {
                        // Set style for unavailable seats
                        seatButton.setStyle("-fx-background-color: #ff6b6b;"); // Light red
                        seatButton.setDisable(true);
                    } else {
                        // Set style for available seats
                        seatButton.setStyle("-fx-background-color: #ffffff;"); // White
                        seatButton.setDisable(false);
                    }
                } catch (SQLException e) {
                    System.err.println("Error checking seat availability for seat " + seatCode + ": " + e.getMessage());
                    // Set to disabled state if we can't determine availability
                    seatButton.setStyle("-fx-background-color: #cccccc;"); // Gray
                    seatButton.setDisable(true);
                }
            }
        });
    }


    public void handleBack(ActionEvent actionEvent) {
        System.out.println("Back button clicked");
        SceneManager.switchScene("/fxml/booking.fxml");
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent) {
        if (selectedSeatsSet.isEmpty()) {
            showAlert("Error", "Please select at least one seat.");
            return;
        } else {
            System.out.println("selectedSeatsSet: " + selectedSeatsSet + "\nselectedSeats: " + selectedSeats);
        }

        Connection connection = null;
        boolean autoCommit = true;

        try {
            // Get necessary data from session
            User loggedInUser = (User) SceneManager.getSessionData("loggedInUser");
            Integer userID = loggedInUser != null ? loggedInUser.getId() : null;
            Integer scheduleID = (Integer) SceneManager.getSessionData("scheduleID");
            String dateStr = (String) SceneManager.getSessionData("selectedDate");
            BigDecimal farePerSeat = (BigDecimal) SceneManager.getSessionData("fare");

            // Validate required data
            if (userID == null || scheduleID == null || dateStr == null || farePerSeat == null) {
                showAlert("Error", "Missing required booking information.");
                return;
            }

            // Validate seat IDs before starting transaction
            Map<String, Integer> seatCodeToIdMap = new HashMap<>(); // Store mapping of seat numbers to IDs
            for (String seatCode : selectedSeatsSet) {
                try {
                    int seatID = convertseatCodeToID(seatCode);
                    seatCodeToIdMap.put(seatCode, seatID);
                } catch (SQLException e) {
                    showAlert("Error", "Invalid seat number: " + seatCode);
                    return;
                }
            }

            Date travelDate = Date.valueOf(dateStr);
//            BigDecimal totalFare = farePerSeat.multiply(new BigDecimal(selectedSeatsSet.size()));;
            BigDecimal totalFare = farePerSeat;

            // Get database connection and start transaction
            connection = DatabaseConnection.getConnection();
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            // Check seat availability
            for (Map.Entry<String, Integer> entry : seatCodeToIdMap.entrySet()) {
                if (!seatDAO.isSeatAvailable(scheduleID, entry.getValue(), travelDate.toLocalDate())) {
                    showAlert("Error", "Seat " + entry.getKey() + " is already booked. Please select different seats.");
                    return;
                }
            }

            // Create bookings
            Set<Integer> bookingIds = new HashSet<>();
            for (Map.Entry<String, Integer> entry : seatCodeToIdMap.entrySet()) {
                Schedule schedule = new Schedule();
                schedule.setScheduleId(scheduleID);

                Booking booking = new Booking();
                booking.setUserId(userID);
                booking.setSchedule(schedule);
                booking.setSeatId(entry.getValue());
                booking.setTravelDate(travelDate.toLocalDate());
                booking.setTotalFare(totalFare);
                booking.setStatus("PENDING");

                int bookingId = bookingDAO.createBooking(booking);
                bookingIds.add(bookingId);
            }

            // Commit transaction
            connection.commit();

            // Store booking information in session
            SceneManager.setSessionData("bookingIds", bookingIds);
            SceneManager.setSessionData("selectedSeats", new HashSet<>(selectedSeatsSet));
            SceneManager.setSessionData("totalFare", totalFare);

            // Switch to ticket view
            SceneManager.switchScene("/fxml/ticket_view.fxml");

        } catch (SQLException e) {
            // Rollback transaction on error
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            showAlert("Error", "Failed to create booking: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Restore original auto-commit setting
            if (connection != null) {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (SQLException e) {
                    System.err.println("Error restoring auto-commit setting: " + e.getMessage());
                }
            }
        }
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private int convertseatCodeToID(String seatCode) throws SQLException {
        String sql = "SELECT seatID FROM seats WHERE seatCode = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, seatCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("seatID");
                } else {
                    throw new SQLException("Invalid seat number: " + seatCode);
                }
            }
        }
    }



    @FXML
    public void HandleSelect(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Button) {
            Button seatButton = (Button) mouseEvent.getSource();
            String seatCode = seatButton.getText();

            // Only handle selection if the seat is not disabled (available)
            if (!seatButton.isDisabled()) {
                if (selectedSeatsSet.contains(seatCode)) {
                    // Deselect the seat
                    selectedSeatsSet.remove(seatCode);
                    selectedSeats.remove(seatCode);
                    seatButton.setStyle(""); // Reset to default style
                } else {
                    // Select the seat
                    selectedSeatsSet.add(seatCode);
                    selectedSeats.add(seatCode);
                    seatButton.setStyle("-fx-background-color: #00ff00;"); // Green for selected
                }
            }
        }
    }


}