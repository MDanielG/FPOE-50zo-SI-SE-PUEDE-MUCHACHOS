package com.example.fpoe_50zo.controller;

import com.example.fpoe_50zo.view.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupScreenController {

    @FXML
    private ChoiceBox<Integer> choiceBoxOponentes;

    @FXML
    private Label errorLabel; // Un label para mostrar errores

    private ViewSwitcher switcher;

    // Método para que el ViewSwitcher se "inyecte"
    public void setSwitcher(ViewSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    public void initialize() {
        // Llenar el ChoiceBox con las opciones
        choiceBoxOponentes.setItems(FXCollections.observableArrayList(1, 2, 3));
        choiceBoxOponentes.setValue(1); // Dejar 1 seleccionado por defecto
        if (errorLabel != null) errorLabel.setText("");
    }

    @FXML
    void onEmpezarClick(ActionEvent event) {
        Integer numOponentes = choiceBoxOponentes.getValue();

        if (numOponentes == null) {
            if (errorLabel != null) errorLabel.setText("Por favor, selecciona un número.");
            return;
        }

        // ¡MIRA QUÉ LIMPIO!
        // Solo llama al gestor y le pasa el dato.
        switcher.showGameScreen(numOponentes);
    }
}
