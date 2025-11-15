package com.example.fpoe_50zo.controller;

import com.example.fpoe_50zo.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScreenController {

    private ViewSwitcher switcher;

    // Método para que el ViewSwitcher se "inyecte" a sí mismo
    public void setSwitcher(ViewSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    void onJugarClick(ActionEvent event) {
        // ¡MIRA QUÉ LIMPIO!
        // Ya no carga FXML, solo le pide al gestor que navegue.
        switcher.showSetupScreen();
    }
}
