package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import com.example.testvideo.negocio.cache;
import com.example.testvideo.negocio.rentarPropiedadNegocio;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class rentarPropiedadController {

    @FXML
    private Pane camposBonos;

    @FXML
    private Pane camposCredito;

    @FXML
    private Label varAcumulado;

    @FXML
    private TextField varCantidadTarjeta;

    @FXML
    private Label varCiudad;

    @FXML
    private TableView<bono> varTablaBonos;
    @FXML
    private TableColumn<bono, Integer> varCol1;

    @FXML
    private TableColumn<bono, Float> varCol2;

    @FXML
    private Label varDireccion;

    @FXML
    private DatePicker varFecha;

    @FXML
    private Label varHabitacion;

    @FXML
    private CheckBox varInputBono;

    @FXML
    private CheckBox varInputTarjeta;

    @FXML
    private TextField varNumeroBono;

    @FXML
    private TextField varNumeroTarjeta;

    @FXML
    private Label varTipoPropiedad;

    @FXML
    private  ChoiceBox<String> varTipoTarjeta;

    @FXML
    private TextField varTitular;

    @FXML
    private Label varTotalPagar;

    @FXML
    private Label varTotalRenta;

    @FXML
    private Label varVEI;

    @FXML
    private TextField varValorBono;
    @FXML
    private ImageView minimize;

    ObservableList<bono> list = FXCollections.observableArrayList();
    float VTR;
    float VTP;
    String idPropiedad;

    public void minimizarVentana(MouseEvent mouseEvent) {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }
    public void cerrarVentana(MouseEvent mouseEvent) throws IOException {
        DAO.desbloquearPropiedad();
        DAO.deslogear();
        conectar.desconectar();
        javafx.application.Platform.exit();
    }
    public void menuPrincipal(MouseEvent event) throws IOException {
        DAO.desbloquearPropiedad();
        //Siguiente pantalla
        FXMLLoader loader = new FXMLLoader(getClass().getResource("historialVisitas/historialVisitas.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void addBono(ActionEvent event) throws IOException {
        //Se ejecuta cuando se quiere añadir un bono a la tabla
        try {
            int numeroBono = Integer.parseInt(varNumeroBono.getText());
            float valorBono = Float.parseFloat(varValorBono.getText());

            int resultado = rentarPropiedadNegocio.addBono(list, varTablaBonos,numeroBono,valorBono);

            switch (resultado) {
                case 1:
                    break;
                case 2:
                    alertWindow("El bono ya ha sido usado");
                    break;
                case 3:
                    alertWindow("Ya ingreso este bono, pruebe con otro");
                    break;
                default:
                    alertWindow("Esto no se supone debe aparecer");
                    break;
            }
        }
        catch (Exception e) {
            alertWindow("No ingreso correctamente el bono");
        }
    }
    public void calcular(ActionEvent event) throws SQLException, IOException {

        String numTarjeta = "";
        float tarjeta = 0;
        float sumabonos = 0;
        float total;
        if (varInputTarjeta.isSelected()) { //Comprueba si hay campos vacios si la tarjeta esta seleccionada
            if (varTitular.getText().equals("") || varTipoTarjeta.getValue() == null || varNumeroTarjeta.getText().equals("")   || varFecha.getValue() == null || varCantidadTarjeta.getText().equals(""))
            {
                alertWindow("Para usar una tarjeta, llene todos los campos");
            }
            else {
                try {
                    int numeroTarjeta = Integer.parseInt(varNumeroTarjeta.getText());
                    float cantidadTarjeta = Integer.parseInt(varCantidadTarjeta.getText());

                    tarjeta += cantidadTarjeta;
                    numTarjeta = Integer.toString(numeroTarjeta);
                }
                catch (Exception e) {
                    alertWindow("Revise nuevamente los campos para usar la tarjeta");
                }
            }
        }
        //Suma de los bonos
        if (varInputBono.isSelected()) {
            sumabonos = rentarPropiedadNegocio.valorBonos(list);
        }
        //Total recaudado
        total = tarjeta + sumabonos;

        //Imprime lo que se ha recaudado
        varAcumulado.setText(Float.toString(total));
        varAcumulado.setVisible(true);

        //Si se supera el valor a pagar, se ejecuta
        //Comprueba si se renta la propiedad o no se puede
        if (total > VTP) {
            int estado = rentarPropiedadNegocio.isDeleted();
            switch (estado) {

                case 1:
                    //La propiedad esta eliminada
                    cambiarPantalla("menuPrincipal/menuPrincipal.fxml", event);
                    alertWindow("No se pudo rentar porque el dueño elimino la propiedad");
                    break;

                case 2:
                    //La propiedad ya esta habitada
                    cambiarPantalla("menuPrincipal/menuPrincipal.fxml", event);
                    alertWindow("No se pudo rentar porque la propiedad esta habitada");
                    break;

                case 3:
                    //Es posibe rentar

                    //Si ya se ha usado algun bono en otra renta, retorna false, de lo contrario retorna true
                    boolean valid = rentarPropiedadNegocio.bonossinRepetir(list);

                    if (valid) { //Se procede a rentar
                        rentarPropiedadNegocio.realizarRenta(VTR,VTP,idPropiedad, varInputTarjeta,numTarjeta, varFecha,varTitular,varTipoTarjeta,varInputBono,list);

                        cambiarPantalla("gestionarServicios/gestionarServicios.fxml", event);
                        alertWindow("Transaccion exitosa");
                    }
                    else {
                        //El cliente puso bonos que ya se han usado por alguien mas
                        cambiarPantalla("rentarPropiedad/rentarPropiedad.fxml", event);
                        alertWindow("Algunos bonos ya han sido registrados recientemente. \n" + " diligencie el formulario nuevamente");
                    }
                    break;
            }
        }
    }

    @FXML
    void initialize() { //inicializa variables
        varCol1.setCellValueFactory(new PropertyValueFactory<bono,Integer>("idBono"));
        varCol2.setCellValueFactory(new PropertyValueFactory<bono,Float>("valorBono"));

        varTablaBonos.setItems(list);

        try {
            varTipoTarjeta.getItems().addAll(rentarPropiedadNegocio.tipoTarjeta());
            //Lee cache
            String id = cache.getPropiedad();

            //Establece los valores de la propiedad en pantalla
            ResultSet cuenta = rentarPropiedadNegocio.obtenerPropiedad(id);
            idPropiedad = rentarPropiedadNegocio.obtenerInfo(cuenta,1);
            VTR = totalizarRenta.calcularVTR(id);
            VTP = totalizarRenta.getVTP(VTR);
            varTotalRenta.setText("$" + VTR);
            varTotalPagar.setText("$" + VTP);
            varCiudad.setText(rentarPropiedadNegocio.ciudad(rentarPropiedadNegocio.obtenerInfo(cuenta,11)));
            varVEI.setText("$" + rentarPropiedadNegocio.obtenerInfo(cuenta,5));
            varDireccion.setText(rentarPropiedadNegocio.obtenerInfo(cuenta,2));
            varTipoPropiedad.setText(rentarPropiedadNegocio.obtenerInfo(cuenta,10));
            varHabitacion.setText(rentarPropiedadNegocio.obtenerInfo(cuenta,4));
            varTipoPropiedad.setText(rentarPropiedadNegocio.tipoPropiedad(varTipoPropiedad.getText()));


            //Checkbox de tarjeta y bonos
            varInputTarjeta.setOnAction((event) -> {
                camposCredito.setDisable(!varInputTarjeta.isSelected());
            });
            varInputBono.setOnAction((event) -> {
                camposBonos.setDisable(!varInputBono.isSelected());
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void alertWindow(String texto) throws IOException {
        AlertWindowNegocio.setLabelValue(texto);
        Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1 = new Stage();
        stage1.initStyle(StageStyle.TRANSPARENT);
        stage1.setScene(scene1);
        stage1.show();
    }
    public void cambiarPantalla(String direccion, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(direccion));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}