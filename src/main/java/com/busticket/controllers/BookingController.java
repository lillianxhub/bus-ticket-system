package com.busticket.controllers;

import com.busticket.models.Booking;
import com.busticket.models.User;
import com.busticket.services.BookingService;
import com.busticket.services.ScheduleService;
import com.busticket.utils.SceneManager;
import com.fasterxml.jackson.databind.introspect.Annotated;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.math.BigDecimal;

public class BookingController {
    @FXML
    private AnchorPane bookingPane;
    @FXML
    private ComboBox<String> fromLocation;
    @FXML
    private ComboBox<String> toLocation;
    @FXML
    private DatePicker travelDate;
    @FXML
    private ComboBox<String> busType;
    @FXML
    private TableView<Booking> busScheduleTable;
    @FXML
    private TableColumn<Booking, Integer> scheduleColumn;
    @FXML
    private TableColumn<Booking, String> busNameColumn;
    @FXML
    private TableColumn<Booking, String> departureColumn;
    @FXML
    private TableColumn<Booking, String> arrivalColumn;
    @FXML
    private TableColumn<Booking, String> typeColumn;
    @FXML
    private TableColumn<Booking, BigDecimal> fareColumn;

    private BookingService bookingService;
    private ScheduleService scheduleService;

    @FXML
    public void initialize() {
        User loggedInUser = (User) SceneManager.getSessionData("loggedInUser");
//        String username = (String) SceneManager.getSessionData("username");
//        String userRole = (String) SceneManager.getSessionData("userRole");

        // Use the session data as needed
        if (loggedInUser != null) {
            // Update UI or perform role-based actions
            bookingService = new BookingService();
            scheduleService = new ScheduleService();
            setupComboBoxes();
            setupTable();
            System.out.println("Booking pane initialized");
            System.out.println("Logged in user: " + loggedInUser);
            System.out.println("UserID: " + loggedInUser.getId() + "\n");
        }
    }

    private void setupComboBoxes() {
        // Populate locations and bus types
        ObservableList<String> locations = FXCollections.observableArrayList(scheduleService.getAllLocations());
        fromLocation.setItems(locations);
        toLocation.setItems(locations);

        ObservableList<String> types = FXCollections.observableArrayList(
                "GOLD_CLASS", "FIRST_CLASS"
        );
        busType.setItems(types);
    }

    private void setupTable() {
        // Configure cell value factories for each column
        scheduleColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getSchedule().getScheduleId()));

        busNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBus().getBusName()));

        departureColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSchedule().getDepartureTime().toString()));

        arrivalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSchedule().getArrivalTime().toString()));

        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBus().getBusType().toString()));

        fareColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getSchedule().getFare()));

        // Initialize empty observable list
        busScheduleTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void handleSearch() {
        String from = fromLocation.getValue();
        String to = toLocation.getValue();
        LocalDate date = travelDate.getValue();
        String type = busType.getValue();

        if (from == null || to == null || date == null) {
            showAlert("Please fill in all search criteria");
            return;
        }

        try {
            ObservableList<Booking> schedules = FXCollections.observableArrayList(
                    scheduleService.searchAvailableSchedules(from, to, date, type)
            );
            busScheduleTable.setItems(schedules);
        } catch (RuntimeException e) {
            showAlert("Error searching schedules: " + e.getMessage());
        }
    }


    @FXML
    private void handleSeatSelection() {
        Booking selectedBooking = busScheduleTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Please select a schedule first");
            return;
        }
//        System.out.println("Selected booking: " + selectedBooking);
//        System.out.println("Selected bus name: " + busNameColumn.getCellData(selectedBooking));
//        System.out.println("Selected date: " + travelDate.getValue());
//        System.out.println("Selected bus type: " + busType.getValue());
//        System.out.println("Selected fare: " + fareColumn.getCellData(selectedBooking));
//        System.out.println("Selected departure time: " + departureColumn.getCellData(selectedBooking));
//        System.out.println("Selected arrival time: " + arrivalColumn.getCellData(selectedBooking));
//        System.out.println("Selected bus type: " + typeColumn.getCellData(selectedBooking));
//        System.out.println("Selected departure location: " + fromLocation.getValue());
//        System.out.println("Selected arrival location: " + toLocation.getValue());
//        System.out.println("User ID: " + SceneManager.getSessionData("userID") + "\n");
//        System.out.println("User role: " + SceneManager.getSessionData("role") + "\n");
//        System.out.println("User email: " + SceneManager.getSessionData("email") + "\n");
//        System.out.println("User name: " + SceneManager.getSessionData("name") + "\n");
        System.out.println("Selected schedule ID: " + scheduleColumn.getCellData(selectedBooking) + "\n");
//
//        // Navigate to seat selection view
//        SceneManager.setSessionData("fare", fareColumn.getCellData(selectedBooking));
//        SceneManager.setSessionData("userId", SceneManager.getSessionData("userID"));
//        SceneManager.setSessionData("role", SceneManager.getSessionData("role"));
//        SceneManager.setSessionData("email", SceneManager.getSessionData("email"));
//        SceneManager.setSessionData("name", SceneManager.getSessionData("name"));
        SceneManager.setSessionData("selectedSchedule", scheduleColumn.getCellData(selectedBooking));
        SceneManager.setSessionData("selectedBus", busNameColumn.getCellData(selectedBooking));
        SceneManager.setSessionData("selectedDate", travelDate.getValue().toString());
        SceneManager.setSessionData("scheduleID", scheduleColumn.getCellData(selectedBooking));
        SceneManager.setSessionData("fare", fareColumn.getCellData(selectedBooking));
        SceneManager.switchScene("/fxml/seat_selection.fxml");
    }

    @FXML
    private void handleBack() {
        // Navigate back to previous view
        SceneManager.switchScene("/fxml/login.fxml");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public AnchorPane getBookingPane() {
        return bookingPane;
    }

    public void setBookingPane(AnchorPane bookingPane) {
        this.bookingPane = bookingPane;
    }
}