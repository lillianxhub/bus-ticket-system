package com.busticket.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static Stage primaryStage;
    private static final Map<String, Object> sessionData = new HashMap<>();

    private SceneManager() {
//         Private constructor to prevent instantiation
    }

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) {
        switchScene(fxmlPath, null);
    }

    public static void switchScene(String fxmlPath, Object controller) {
        try {
            String normalizedPath = fxmlPath.startsWith("/") ? fxmlPath : "/" + fxmlPath;

            URL location = SceneManager.class.getResource(normalizedPath);
            if (location == null) {
                throw new IOException("Cannot find resource: " + normalizedPath);
            }

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            if (controller != null) {
                loader.setController(controller);
            }

            Parent root = loader.load();
            Scene scene = new Scene(root);
            URL cssUrl = SceneManager.class.getResource("/css/application.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }


            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            AlertHelper.showErrorAlert("Navigation Error", "Could not load page: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void loadScene(String fxmlPath, ActionEvent event) {
        try {
            URL location = SceneManager.class.getResource(fxmlPath);
            if (location == null) {
                throw new IOException("Cannot find " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Add CSS if needed
            URL cssLocation = SceneManager.class.getResource("/css/styles.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }

            Stage stage = event != null ?
                    (Stage) ((Node) event.getSource()).getScene().getWindow() :
                    primaryStage;

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            AlertHelper.showErrorAlert("Navigation Error",
                    "Could not load page: " + e.getMessage());
        }
    }

    public static void setSessionData(String key, Object value) {
        sessionData.put(key, value);
    }

    public static Object getSessionData(String key) {
        return sessionData.get(key);
    }

    public static void removeSessionData(String key) {
        sessionData.remove(key);
    }

    public static void clearSession() {
        sessionData.clear();
    }
}