package com.example.testvideo.negocio;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AlertWindowNegocio {
    public static String labelValue = "";

    public static String getLabelValue() {
        return labelValue;
    }
    public static void setLabelValue(String labelValue) {
        AlertWindowNegocio.labelValue = labelValue;
    }
}
