package com.busticket.controllers;

import com.busticket.models.User;
import com.busticket.services.AuthService;
import com.busticket.utils.AlertHelper;
import com.busticket.utils.SceneManager;
import com.busticket.utils.ValidationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    @FXML private AnchorPane loginPane;
    @FXML private AnchorPane registerPane;

    // Login form fields
    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;

    // Register form fields
    @FXML private TextField regUsername;
    @FXML private PasswordField regPassword;
    @FXML private PasswordField regConfirmPassword;
    @FXML private TextField regEmail;
    @FXML private TextField regFullName;
    @FXML private TextField regPhone;
    @FXML private TextArea regAddress;
    @FXML private Button registerButton;
    @FXML private Hyperlink loginLink;

    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void showLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginView = loader.load();

            // Get the current stage from any control (e.g., registerPane if we're in register view)
            Stage stage = (Stage) (registerPane != null ? registerPane.getScene().getWindow() : loginPane.getScene().getWindow());
            stage.setScene(new Scene(loginView));
        } catch (IOException e) {
            AlertHelper.showErrorAlert("Navigation Error", "Could not load login form.");
        }
    }

    @FXML
    public void showRegisterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent registerView = loader.load();

            // Get the current stage from loginPane
            Stage stage = (Stage) loginPane.getScene().getWindow();
            stage.setScene(new Scene(registerView));
        } catch (IOException e) {
            AlertHelper.showErrorAlert("Navigation Error", "Could not load registration form.");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertHelper.showErrorAlert("Login Error", "Please enter both username and password.");
            return;
        }

        Optional<User> userOpt = authService.login(username, password);
        System.out.println("User: " + userOpt);
        System.out.println("User role: " + userOpt.map(User::getRole));
        System.out.println("User role: " + userOpt.map(User::getRole).orElse(null));
        System.out.println("User ID: " + userOpt.map(User::getId).orElse(null));
        System.out.println("User email: " + userOpt.map(User::getEmail).orElse(null));
        
        SceneManager.setSessionData("username", username);

        try {
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                SceneManager.setSessionData("currentUser", user);
                System.out.println("Current user: " + user);

                // Store user data in session
                SceneManager.setSessionData("loggedInUser", user);
                SceneManager.setSessionData("username", user.getUsername());
                SceneManager.setSessionData("userRole", user.getRole());


                SceneManager.switchScene("/fxml/booking.fxml");

            } else {
                AlertHelper.showErrorAlert("Login Failed", "Invalid username or password.");
            }
        } catch (Exception e){
        AlertHelper.showErrorAlert("Login Error", "An error occurred during login. Please try again later.");
    }

    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = regUsername.getText().trim();
        String password = regPassword.getText();
        String confirmPassword = regConfirmPassword.getText();
        String email = regEmail.getText().trim();
        String fullName = regFullName.getText().trim();
        String phone = regPhone.getText().trim();
        String address = regAddress.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                email.isEmpty() || fullName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            AlertHelper.showErrorAlert("Registration Error", "All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertHelper.showErrorAlert("Registration Error", "Passwords do not match.");
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            AlertHelper.showErrorAlert("Registration Error", "Please enter a valid email address.");
            return;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            AlertHelper.showErrorAlert("Registration Error", "Please enter a valid phone number.");
            return;
        }

        try {
            boolean success = authService.register(username, password, email, fullName,
                    phone, address, User.UserRole.CUSTOMER.toString());
            if (success) {
                AlertHelper.showInformationAlert("Registration Successful",
                        "Your account has been created successfully. Please login.");
                showLoginForm();
            } else {
                AlertHelper.showErrorAlert("Registration Failed",
                        "Username or email already exists. Please try different credentials.");
            }
        } catch (Exception e) {
            AlertHelper.showErrorAlert("Registration Error",
                    "An error occurred during registration. Please try again later.");
        }
    }
}