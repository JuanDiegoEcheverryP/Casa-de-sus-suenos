package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import com.example.testvideo.negocio.nuevaPropiedadNegocio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class nuevaPropiedadController {
    private Connection conn;
    private String[] tipoP = {"Seleccionar","Casa", "Apartamento"};
    @FXML
    private ChoiceBox<String> campoAgencia;

    @FXML
    private PasswordField campoContrasena;

    @FXML
    private TextField campoPrecio;

    @FXML
    private TextField campoUsuario;

    @FXML
    private TextField campodireccion;

    @FXML
    private ChoiceBox<String> choiceFiltroPropiedad;

    @FXML
    private ChoiceBox<String> choiceFiltroUbicacion;

    @FXML
    private ImageView minimize;

    @FXML
    private Spinner<Integer> spinnerHabitaciones;


    @FXML
    void initialize() throws IOException, SQLException {

        actualizarFiltroUbicaciones();
        actualizarFiltroAgencias();
        actualizarFiltroTipoPropiedad();
        actualizarFiltroHabitaciones();
    }

    private void actualizarFiltroUbicaciones(){
        choiceFiltroUbicacion.getItems().clear();

        List<String> ubicaciones = new ArrayList<String>();

        ubicaciones.add("Seleccionar");

        String query = "Select municipio from ubicacion";

        conn = conectar.getConnection();
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery(query);

            while(rs.next()){
                ubicaciones.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Pone el valor por defecto
        choiceFiltroUbicacion.setValue("Seleccionar");

        choiceFiltroUbicacion.getItems().addAll(ubicaciones);
    }

    private void actualizarFiltroAgencias(){
        campoAgencia.getItems().clear();
        List<String> agencias = new ArrayList<String>();

        agencias.add("Seleccionar");
        String query = "select nombresede from sedeagencia";

        conn = conectar.getConnection();
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery(query);

            while(rs.next()){
                agencias.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Pone el valor por defecto
        campoAgencia.setValue("Seleccionar");
        campoAgencia.getItems().addAll(agencias);
    }

    public void actualizarFiltroTipoPropiedad(){
        choiceFiltroPropiedad.getItems().clear();

        //Pone el valor por defecto
        choiceFiltroPropiedad.setValue("Seleccionar");

        choiceFiltroPropiedad.getItems().addAll(tipoP);
    }

    public void actualizarFiltroHabitaciones(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,15);

        valueFactory.setValue(0);

        spinnerHabitaciones.setValueFactory(valueFactory);
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

    public void menuPrincipal(MouseEvent event) throws IOException {
        //Siguiente pantalla
        FXMLLoader loader = new FXMLLoader(getClass().getResource("misPropiedades/misPropiedades.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addPropiedad(ActionEvent event) throws IOException, SQLException {
        String direccion = campodireccion.getText();
        String ubicacion = choiceFiltroUbicacion.getValue();
        String tipoPropiedad = choiceFiltroPropiedad.getValue();
        String habitaciones = String.valueOf(spinnerHabitaciones.getValue());
        String agencia = campoAgencia.getValue();
        String usuario = campoUsuario.getText();
        String contrasena = campoContrasena.getText();
        String fecha = String.valueOf(java.time.LocalDate.now());

        if (direccion.equals("")                    ||
                ubicacion.equals("Seleccionar")     ||
                tipoPropiedad.equals("Seleccionar") ||
                habitaciones.equals("0")            ||
                agencia.equals("Seleccionar")       ||
                usuario.equals("")                  ||
                contrasena.equals("")
        ) {
            AlertWindowNegocio.setLabelValue("Llene todos los campos para continuar");
            Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
            Scene scene1 = new Scene(root1);
            Stage stage1 = new Stage();
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setScene(scene1);
            stage1.show();
        }
        else {

            boolean seguir = true;

            try {
                if (Integer.parseInt(campoPrecio.getText()) <= 0) {
                    seguir = false;
                    throw new Exception("negativo");
                }
            }
            catch (Exception e) {
                AlertWindowNegocio.setLabelValue("Revise el formato del precio de la propiedad");
                Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                Scene scene1 = new Scene(root1);
                Stage stage1 = new Stage();
                stage1.initStyle(StageStyle.TRANSPARENT);
                stage1.setScene(scene1);
                stage1.show();
            }
            if (seguir) {
                int iduser = DAO.validarUser(usuario,contrasena);
                if ( iduser != 0) {
                    String user = String.valueOf(iduser);
                    String precio = campoPrecio.getText();
                    String idUbicacion = nuevaPropiedadNegocio.idUbicacion(ubicacion);
                    String tipo = nuevaPropiedadNegocio.idTipo(tipoPropiedad);
                    //Armar el query
                    nuevaPropiedadNegocio.addPropiedad(direccion,fecha,habitaciones,precio,user,agencia,tipo,idUbicacion);

                    //Siguiente pantalla
                    initialize();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal/menuPrincipal.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    AlertWindowNegocio.setLabelValue("Propiedad añadida a la base de datos");
                    Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                    Scene scene1 = new Scene(root1);
                    Stage stage1 = new Stage();
                    stage1.initStyle(StageStyle.TRANSPARENT);
                    stage1.setScene(scene1);
                    stage1.show();
                }
                else {
                    AlertWindowNegocio.setLabelValue("No se ha podido confirmar su identidad");
                    Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                    Scene scene1 = new Scene(root1);
                    Stage stage1 = new Stage();
                    stage1.initStyle(StageStyle.TRANSPARENT);
                    stage1.setScene(scene1);
                    stage1.show();
                }
            }
        }
    }
}