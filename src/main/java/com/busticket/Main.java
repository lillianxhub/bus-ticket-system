package com.busticket;

import com.busticket.utils.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent root = loadFXML();
        Scene scene = new Scene(root);

        // Load CSS with error handling
        loadCSS(scene);

        stage.setTitle("Bus Ticketing System");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.show();

        // Initialize the scene manager
        SceneManager.initialize(primaryStage);
    }

    private Parent loadFXML() throws IOException {
        // Try multiple resource loading approaches
        URL fxmlLocation = null;

        // Attempt 1: Direct resource loading
        fxmlLocation = Main.class.getResource("/fxml/login.fxml");

        // Attempt 2: Class loader
        if (fxmlLocation == null) {
            fxmlLocation = getClass().getClassLoader().getResource("fxml/login.fxml");
        }

        // Attempt 3: Relative path
        if (fxmlLocation == null) {
            fxmlLocation = getClass().getResource("login.fxml");
        }

        if (fxmlLocation == null) {
            throw new IOException("Cannot find login.fxml. Tried multiple locations. " +
                    "Please ensure the file exists in the resources directory.");
        }

        try {
            return FXMLLoader.load(fxmlLocation);
        } catch (IOException e) {
            throw new IOException("Error loading login.fxml: " + e.getMessage(), e);
        }
    }

    private void loadCSS(Scene scene) {
        URL cssLocation = Main.class.getResource("/css/styles.css");
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        } else {
            System.out.println("Warning: styles.css not found");
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
