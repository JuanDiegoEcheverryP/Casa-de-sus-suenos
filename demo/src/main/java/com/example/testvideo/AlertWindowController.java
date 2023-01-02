package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AlertWindowController {

    @FXML
    private Label Texto;

    @FXML
    private Button accept;

    @FXML
    private ImageView close;


    public void cerrarVentana(MouseEvent mouseEvent) {
        Stage stage = (Stage) close.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void acceptOnClick(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void initialize() {
        Texto.setText(AlertWindowNegocio.getLabelValue());
    }
}