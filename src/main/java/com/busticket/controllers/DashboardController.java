//package com.busticket.controllers;
//
//import com.busticket.services.BookingService;
//import com.busticket.services.BusService;
//import com.busticket.utils.SceneManager;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//
//public class DashboardController {
//    @FXML private Label activeBookingsCount;
//    @FXML private Label availableBusesCount;
//
//    private final BookingService bookingService = new BookingService();
//    private final BusService busService = new BusService();
//
//    @FXML
//    public void initialize() {
//        loadDashboardData();
//    }
//
//    @FXML
//    private void handleBookTicket(ActionEvent event) {
//        SceneManager.loadScene("booking.fxml", event);
//    }
//
//    @FXML
//    private void handleMyBookings(ActionEvent event) {
//        SceneManager.loadScene("my_bookings.fxml", event);
//    }
//
//    @FXML
//    private void handleViewSchedule(ActionEvent event) {
//        SceneManager.loadScene("schedule.fxml", event);
//    }
//
//    @FXML
//    private void handleProfile(ActionEvent event) {
//        SceneManager.loadScene("profile.fxml", event);
//    }
//
//    @FXML
//    private void handleLogout(ActionEvent event) {
//        SceneManager.loadScene("login.fxml", event);
//    }
//
//    private void loadDashboardData() {
//        activeBookingsCount.setText(
//                String.valueOf(bookingService.getActiveBookingsCount()));
//        availableBusesCount.setText(
//                String.valueOf(busService.getAvailableBusesCount()));
//    }
//}