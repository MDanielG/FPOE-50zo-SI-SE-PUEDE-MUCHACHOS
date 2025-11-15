package com.example.fpoe_50zo;

import com.example.fpoe_50zo.view.ViewSwitcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for the "Cincuentazo" game.
 * This class is responsible for loading the FXML view and launching the UI.
 */
public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications.
     *
     * @param stage The primary stage for this application, onto which
     * the application scene is set.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        ViewSwitcher viewSwitcher = new ViewSwitcher(stage);

        viewSwitcher.showStartScreen();
    }

    /**
     * Main method to launch the application.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}