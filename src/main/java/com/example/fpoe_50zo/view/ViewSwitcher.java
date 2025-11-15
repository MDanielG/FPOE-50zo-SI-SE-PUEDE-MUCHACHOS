package com.example.fpoe_50zo.view;

import com.example.fpoe_50zo.controller.GameController;
import com.example.fpoe_50zo.controller.SetupScreenController;
import com.example.fpoe_50zo.controller.StartScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase central para manejar la navegación.
 * Carga FXMLs y cambia las escenas en el Stage principal.
 */
public class ViewSwitcher {

    private Stage stage;

    public ViewSwitcher(Stage stage) {
        this.stage = stage;
    }

    /**
     * Carga y muestra la pantalla de inicio.
     */
    public void showStartScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fpoe_50zo/StartScreen.fxml"));
            Parent root = loader.load();

            // Pasa este ViewSwitcher al controlador que se acaba de cargar
            StartScreenController controller = loader.getController();
            controller.setSwitcher(this);

            stage.setScene(new Scene(root));
            stage.setTitle("Cincuentazo - Inicio");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga y muestra la pantalla de configuración.
     */
    public void showSetupScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fpoe_50zo/SetupScreen.fxml"));
            Parent root = loader.load();

            // Pasa este ViewSwitcher al controlador
            SetupScreenController controller = loader.getController();
            controller.setSwitcher(this);

            stage.setScene(new Scene(root));
            stage.setTitle("Cincuentazo - Configuración");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga y muestra la pantalla principal del juego.
     * @param numOponentes El dato que pasamos de la pantalla de setup.
     */
    public void showGameScreen(int numOponentes) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fpoe_50zo/GameView.fxml"));
            Parent root = loader.load();

            // Obtenemos el GameController
            GameController controller = loader.getController();

            // ¡Llamamos al método 'prepararJuego' que ya teníamos!
            controller.prepararJuego(numOponentes);

            stage.setScene(new Scene(root));
            stage.setTitle("Cincuentazo - ¡A Jugar!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}