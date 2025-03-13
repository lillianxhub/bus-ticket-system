package com.busticket.controllers;

import com.busticket.models.Booking;
import com.busticket.models.Bus;
import com.busticket.models.User;
import com.busticket.services.AdminService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private AnchorPane AdminDashboardPane;

    @FXML
    private Button logoutButton;

    @FXML
    private Label totalUsersCount;

    @FXML
    private Label activeBusesCount;

    @FXML
    private Label todayBookingsCount;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private TableView<Booking> recentBookingsTable;

    @FXML
    private TableColumn<Booking, String> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> userColumn;

    @FXML
    private TableColumn<Booking, String> routeColumn;

    @FXML
    private TableColumn<Booking, String> dateColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    private ObservableList<Booking> recentBookings = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the logout button action
        logoutButton.setOnAction(event -> handleLogout());

        // Initialize table columns
        setupTableColumns();

        // Load dashboard data
        loadDashboardData();
    }

    private void setupTableColumns() {
        // Setup booking ID column
        bookingIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookingId().toString()));

        // Setup user column (assuming you have a method to get user name from userId)
        userColumn.setCellValueFactory(cellData -> {
            // In a real app, you would fetch the user's name based on userId
            return new SimpleStringProperty("User " + cellData.getValue().getUserId());
        });

        // Setup route column
        routeColumn.setCellValueFactory(cellData -> {
            // Get route information from the schedule
            if (cellData.getValue().getSchedule() != null) {
                return new SimpleStringProperty(cellData.getValue().getSchedule().toString());
            }
            return new SimpleStringProperty("N/A");
        });

        // Setup date column
        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTravelDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return new SimpleStringProperty(cellData.getValue().getTravelDate().format(formatter));
            }
            return new SimpleStringProperty("N/A");
        });

        // Setup status column
        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));
    }

    private void loadDashboardData() {
        // In a real application, these would come from your database or service layer

        // Load user count
        int userCount = AdminService.fetchTotalUsers();
        totalUsersCount.setText(String.valueOf(userCount));

        // Load active buses count
        int busCount = AdminService.fetchActiveBuses();
        activeBusesCount.setText(String.valueOf(busCount));

        // Load today's bookings count
        int todayBookings = AdminService.fetchTodayBookings();
        todayBookingsCount.setText(String.valueOf(todayBookings));

        // Load total revenue
        BigDecimal revenue = AdminService.fetchTotalRevenue();
        totalRevenueLabel.setText("$" + revenue.toString());

        // Load recent bookings into table
        List<Booking> bookings = AdminService.fetchRecentBookings();
        recentBookings.setAll(bookings);
        recentBookingsTable.setItems(recentBookings);
    }

    @FXML
    public void handleManageBuses(ActionEvent actionEvent) {
        try {
            // Load the Manage Buses view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/busticket/views/ManageBuses.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error dialog)
        }
    }

    @FXML
    public void handleManageRoutes(ActionEvent actionEvent) {
        try {
            // Load the Manage Routes view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/busticket/views/ManageRoutes.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    public void handleManageUsers(ActionEvent actionEvent) {
        try {
            // Load the Manage Users view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/busticket/views/ManageUsers.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    public void handleViewBookings(ActionEvent actionEvent) {
        try {
            // Load the View Bookings view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/busticket/views/ViewBookings.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    public void handleReports(ActionEvent actionEvent) {
        try {
            // Load the Reports view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/busticket/views/Reports.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private void handleLogout() {
        try {
            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/busticket/views/Login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
}