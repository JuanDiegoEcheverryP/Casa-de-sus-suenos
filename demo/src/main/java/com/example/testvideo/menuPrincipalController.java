package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import com.example.testvideo.negocio.cache;
import com.example.testvideo.negocio.menuPrincipalNegocio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class menuPrincipalController {

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    Label userHello;

    @FXML
    private Button misPropiedadesButton;

    @FXML
    Button userName;

    @FXML
    Button gestionarServiciosButton;
    @FXML
    Button verCuentaButton;

    @FXML
    private ImageView minimize;

    public void setUsuario(String nombre) {
        userName.setText(nombre);
    }
    public  void Bienvenida(String nombre) {
        String aux1 = nombre.substring(0,1).toUpperCase();
        String aux2 = nombre.substring(1);

        String nom = aux1+aux2;

        userHello.setText("Bienvenido, " + nom);
    }

    public void minimizarVentana(MouseEvent mouseEvent) {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void cerrarVentana(MouseEvent mouseEvent) throws IOException {
        //Se desconecta con el servidor
        DAO.deslogear();
        conectar.desconectar();
        javafx.application.Platform.exit();
    }

    public void irVerCuenta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("miCuenta/verCuenta.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void irBuscarPropiedad(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("buscarPropiedad/buscarPropiedad.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void irhistorialVisitas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("historialVisitas/historialVisitas.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void irgestionarServicios(ActionEvent event) throws IOException, SQLException {
        if(menuPrincipalNegocio.haveProperty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestionarServicios/gestionarServicios.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            AlertWindowNegocio.setLabelValue("Debe rentar una propiedad para acceder a esta opcion");
            Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
            Scene scene1 = new Scene(root1);
            Stage stage1 = new Stage();
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setScene(scene1);
            stage1.show();
        }
    }

    public void irVerPropiedades(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("misPropiedades/misPropiedades.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        try {
            //Lee cache
            String usuario = cache.getUserName();
            String contrasena = cache.getContrasena();

            //Obtiene usuario
            ResultSet cuenta = DAO.getCuenta(usuario, contrasena);

            //Pone el nombre de usuario
            Bienvenida(DAO.getValueFromResultset(cuenta, 4));
            setUsuario(usuario);

            //Despliega botones dependiendo los permisos
            if (DAO.getValueFromResultset(cuenta, 9).equals("2")) {
                misPropiedadesButton.setVisible(false);
                misPropiedadesButton.setManaged(false);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}