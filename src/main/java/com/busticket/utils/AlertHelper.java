package com.busticket.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;

public class AlertHelper {

    /**
     * Shows an error alert dialog
     * @param title The title of the alert
     * @param content The content/message of the alert
     */
    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        configureAlert(alert, title, content);
        alert.showAndWait();
    }

    /**
     * Shows an information alert dialog
     * @param title The title of the alert
     * @param content The content/message of the alert
     */
    public static void showInformationAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        configureAlert(alert, title, content);
        alert.showAndWait();
    }

    /**
     * Shows a warning alert dialog
     * @param title The title of the alert
     * @param content The content/message of the alert
     */
    public static void showWarningAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        configureAlert(alert, title, content);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog and returns the user's choice
     * @param title The title of the confirmation dialog
     * @param content The content/message of the confirmation dialog
     * @return true if OK was clicked, false otherwise
     */
    public static boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        configureAlert(alert, title, content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Shows a custom confirmation dialog with specified button types
     * @param title The title of the confirmation dialog
     * @param content The content/message of the confirmation dialog
     * @param buttonTypes The button types to display
     * @return The ButtonType that was clicked
     */
    public static Optional<ButtonType> showCustomConfirmation(String title, String content, ButtonType... buttonTypes) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(buttonTypes);
        configureAlert(alert, title, content);

        return alert.showAndWait();
    }

    /**
     * Private helper method to configure common alert properties
     * @param alert The alert to configure
     * @param title The title of the alert
     * @param content The content/message of the alert
     */
    private static void configureAlert(Alert alert, String title, String content) {
        alert.setTitle(title);
        alert.setHeaderText(null); // We don't need a header text
        alert.setContentText(content);

        // Get the DialogPane and add custom styling if needed
        DialogPane dialogPane = alert.getDialogPane();

        // Add custom CSS if needed
        // dialogPane.getStylesheets().add("/css/alerts.css");

        // Make the alert window resizable
        alert.setResizable(true);

        // Get the Stage and set its properties
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.setAlwaysOnTop(true); // Make sure alert is always visible
    }

    /**
     * Shows a success alert dialog
     * @param title The title of the alert
     * @param content The content/message of the alert
     */
    public static void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        configureAlert(alert, title, content);

        // You might want to customize the icon for success alerts
        // DialogPane dialogPane = alert.getDialogPane();
        // dialogPane.setGraphic(new ImageView(new Image("/images/success.png")));

        alert.showAndWait();
    }

    /**
     * Shows an error alert with exception details
     * @param title The title of the alert
     * @param content The main error message
     * @param exception The exception that occurred
     */
    public static void showExceptionAlert(String title, String content, Exception exception) {
        Alert alert = new Alert(AlertType.ERROR);
        configureAlert(alert, title, content + "\n\nError details: " + exception.getMessage());

        // You might want to add a "Show Details" button that expands to show the stack trace
        // TextArea textArea = new TextArea(ExceptionUtils.getStackTrace(exception));
        // alert.getDialogPane().setExpandableContent(textArea);

        alert.showAndWait();
    }
}