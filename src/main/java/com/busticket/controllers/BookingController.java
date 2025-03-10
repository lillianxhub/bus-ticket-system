package com.busticket.controllers;

import com.busticket.models.Booking;
import com.busticket.services.BookingService;
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
    private TableColumn<Booking, String> busNameColumn;
    @FXML
    private TableColumn<Booking, String> departureColumn;
    @FXML
    private TableColumn<Booking, String> arrivalColumn;
    @FXML
    private TableColumn<Booking, String> typeColumn;
    @FXML
    private TableColumn<Booking, BigDecimal> fareColumn;
    @FXML
    private TableColumn<Booking, Integer> seatsColumn;

    private BookingService bookingService;

    @FXML
    public void initialize() {
        bookingService = new BookingService();
        setupComboBoxes();
        setupTable();
    }

    private void setupComboBoxes() {
        // Populate locations and bus types
        ObservableList<String> locations = FXCollections.observableArrayList(bookingService.getAllLocations());
        fromLocation.setItems(locations);
        toLocation.setItems(locations);

        ObservableList<String> types = FXCollections.observableArrayList(
                "GOLD_CLASS", "FIRST_CLASS"
        );
        busType.setItems(types);
    }

    private void setupTable() {
        // Configure cell value factories for each column
        busNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBus().getBusName()));

        departureColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSchedule().getDepartureTime().toString()));

        arrivalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSchedule().getArrivalTime().toString()));

        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBus().getBusType().toString()));

        fareColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTotalFare()));

        seatsColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getBus().getTotalSeats() - cellData.getValue().getBookedSeats()));

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

        ObservableList<Booking> schedules = FXCollections.observableArrayList(
                bookingService.searchAvailableSchedules(from, to, date, type)
        );
        busScheduleTable.setItems(schedules);
    }

    @FXML
    private void handleSeatSelection() {
        Booking selectedBooking = busScheduleTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Please select a schedule first");
            return;
        }
        // Navigate to seat selection view
    }

    @FXML
    private void handleBack() {
        // Navigate back to previous view
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}