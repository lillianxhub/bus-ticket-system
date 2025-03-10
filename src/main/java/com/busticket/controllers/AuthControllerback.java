package com.busticket.controllers;

import com.busticket.models.User;
import com.busticket.services.AuthService;
import com.busticket.utils.AlertHelper;
import com.busticket.utils.SceneManager;
import com.busticket.utils.ValidationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AuthControllerback implements Initializable {

//    @FXML private StackPane mainContainer;
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

    public AuthControllerback() {
        this.authService = new AuthService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (loginPane == null || registerPane == null) {
            AlertHelper.showErrorAlert("Initialization Error", "Failed to initialize UI components. Please check FXML configuration.");
            return;
        }

        // Initially show login form
        showLoginForm();

        // Set up form navigation links
        registerLink.setOnAction(_ -> showRegisterForm());
        loginLink.setOnAction(_ -> showLoginForm());

        // Set up button actions
        loginButton.setOnAction(this::handleLogin);
        registerButton.setOnAction(this::handleRegister);
    }

    public void showLoginForm() {
        if (loginPane != null && registerPane != null) {
            loginPane.setVisible(true);
            registerPane.setVisible(false);

            // Clear login form
            if (loginUsername != null) loginUsername.clear();
            if (loginPassword != null) loginPassword.clear();
        }
    }


    public void showRegisterForm() {
        if (loginPane != null && registerPane != null) {
            loginPane.setVisible(false);
            registerPane.setVisible(true);

            // Clear register form
            if (regUsername != null) regUsername.clear();
            if (regPassword != null) regPassword.clear();
            if (regConfirmPassword != null) regConfirmPassword.clear();
            if (regEmail != null) regEmail.clear();
            if (regFullName != null) regFullName.clear();
            if (regPhone != null) regPhone.clear();
            if (regAddress != null) regAddress.clear();
        }
    }


    @FXML
    private void handleLogin(ActionEvent event) {
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText();

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            AlertHelper.showErrorAlert("Login Error", "Please enter both username and password.");
            return;
        }

        // Attempt login
        Optional<User> userOpt = authService.login(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Store user in session
            SceneManager.setSessionData("currentUser", user);

            // Redirect based on user type
            if ("ADMIN".equals(user.getRole().toString())) {
                SceneManager.switchScene("/fxml/admin_dashboard.fxml");
            } else {
                SceneManager.switchScene("/fxml/dashboard.fxml");
            }
        } else {
            AlertHelper.showErrorAlert("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        // Get all form values and trim whitespace
        String username = regUsername.getText().trim();
        String password = regPassword.getText();
        String confirmPassword = regConfirmPassword.getText();
        String email = regEmail.getText().trim();
        String fullName = regFullName.getText().trim();
        String phone = regPhone.getText().trim();
        String address = regAddress.getText().trim();
        String role = "user";

        // Validate all required fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                email.isEmpty() || fullName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            AlertHelper.showErrorAlert("Registration Error", "All fields are required.");
            return;
        }

//         Validate password match
        if (!password.equals(confirmPassword)) {
            AlertHelper.showErrorAlert("Registration Error", "Passwords do not match.");
            return;
        }

        // Validate email format
        if (!ValidationUtils.isValidEmail(email)) {
            AlertHelper.showErrorAlert("Registration Error", "Please enter a valid email address.");
            return;
        }

//         Validate phone number
        if (!ValidationUtils.isValidPhone(phone)) {
            AlertHelper.showErrorAlert("Registration Error", "Please enter a valid phone number.");
            return;
        }

//         Attempt registration
        try {
            boolean success = authService.register(username, password, email, fullName,
                    phone, address, role);
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