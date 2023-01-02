package com.example.testvideo;

import com.example.testvideo.negocio.cache;
import com.example.testvideo.negocio.gestionarServiciosNegocio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class gestionarServiciosController {
    @FXML
    private Label varCiudad;

    @FXML
    private Label varDireccion;

    @FXML
    private Label varHabitacion;

    @FXML
    private Label varTipoPropiedad;
    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private Label totalServicios;

    @FXML
    private Label resultado;
    @FXML
    private ChoiceBox<String> varTipoTarjeta;
    @FXML
    private TableView<servicio> tablePersona;
    @FXML
    private TableColumn<servicio, Integer> cantidad;
    @FXML
    private TableColumn<servicio, String> name;
    @FXML
    private TableColumn colEdit;
    @FXML
    private ImageView minimize;

    ObservableList<servicio> list = FXCollections.observableArrayList();

    //Listar Servicios()

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal/menuPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void agregarServicio(ActionEvent event) {
        try {
            //Por medio de este get value
            servicio service = new servicio(varTipoTarjeta.getValue(), spinner.getValue());

            gestionarServiciosNegocio.abc(resultado,list,service, tablePersona, totalServicios);

        } catch (Exception e) {
            resultado.setText("No se pudo agregar el servicio");
        }
        tablePersona.setItems(list);

        resultado.setVisible(true);
        populate();
    }
    public void modificarLineaServicio(ActionEvent event) throws SQLException {
        gestionarServiciosNegocio.actualizarServicios(list);
        resultado.setText("Actualizado");

        //Prueba
        gestionarServiciosNegocio.calcularTotal(tablePersona, totalServicios);
    }

    @FXML
    void initialize() throws IOException, SQLException {
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();

        //getUsuario() Este se llama porque lo tiene negocio
        ResultSet cuenta = gestionarServiciosNegocio.getUsuario(usuario, contrasena);
        String idusuario = gestionarServiciosNegocio.getIdUsuario(cuenta);

        //Listar Servicios() (Se envia usuario porque este va asociado a la propiedad actual)
        listarServicios(idusuario);

        //Calcular el total de los servicios
        gestionarServiciosNegocio.calcularTotal(tablePersona,totalServicios);
    }
    public void populate() {
        Callback<TableColumn<servicio, String>, TableCell<servicio, String>> cellFactory = (param) -> {
            //make the tablecell containing button
            final TableCell<servicio, String> cell = new TableCell<servicio, String>() {

                //override updateItem method
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button editButton = new Button("quitar");
                        editButton.setOnAction(event -> {
                            servicio p = getTableView().getItems().get(getIndex());
                            String id = String.valueOf(p.getName());
                            gestionarServiciosNegocio.eliminarLineaServicio(id, list, resultado);
                            populate();
                        });
                        setGraphic(editButton);
                        setText(null);
                    }
                }
            };
            //Return the cell created
            return cell;
        };
        colEdit.setCellFactory(cellFactory);
        tablePersona.setItems(list);
    }
    public void listarServicios(String idusuario) throws SQLException, IOException {
        gestionarServiciosNegocio.guardar(idusuario, varCiudad, varDireccion,  varHabitacion,  varTipoPropiedad, list);
        tablePersona.setItems(list);

        name.setCellValueFactory(new PropertyValueFactory<servicio, String>("name"));
        cantidad.setCellValueFactory(new PropertyValueFactory<servicio, Integer>("cantidad"));
        tablePersona.setItems(list);
        populate();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20);
        valueFactory.setValue(1);
        spinner.setValueFactory(valueFactory);
        varTipoTarjeta.getItems().addAll(gestionarServiciosNegocio.getServicios());
    }
}