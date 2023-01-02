package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import com.example.testvideo.negocio.cache;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class buscarPropiedadController {

    @FXML
    private TableView<Propiedad> tablePersona;

    @FXML
    private TableColumn<Propiedad, Integer> colId;

    @FXML
    private TableColumn<Propiedad, String> colDireccion;

    @FXML
    private TableColumn<Propiedad, String> colAgencia;

    @FXML
    private TableColumn<Propiedad, String> colFecha;

    @FXML
    private TableColumn<Propiedad, Integer> colHabitacion;

    @FXML
    private TableColumn<Propiedad, Float> colValor;

    @FXML
    private TableColumn<Propiedad, String> colTipoPropiedad;

    @FXML
    private TableColumn colEdit;

    @FXML
    private Label filtroRenta;

    @FXML
    private Slider sliderRenta;

    @FXML
    private ChoiceBox<String> choiceFiltroPropiedad;

    @FXML TextField textUbicacion;

    //Casa = 1, Apartamento = 2
    private String[] tipoP = {"Seleccionar","Casa", "Apartamento"};

    @FXML
    private ChoiceBox<String> choiceFiltroUbicacion;

    @FXML
    private Spinner<Integer> spinnerHabitaciones;
    int currentValue;

    private  Connection conn;
    private  ObservableList<Propiedad> list;
    /*
    @FXML
    private HBox navBar;
*/

    @FXML
    void initialize() throws IOException, SQLException {
        sliderRenta.setMax(2000);
        filtroRenta.setText("$0");

        populateTableView();
        actualizarFiltroRenta();
        actualizarFiltroUbicaciones();
        actualizarFiltroTipoPropiedad();
        actualizarFiltroHabitaciones();
    }

    private void  populateTableView() throws SQLException, IOException {
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();

        //Obtiene usuario
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);

        //Pone el nombre de usuario
        String idusuario = DAO.getValueFromResultset(cuenta, 1);

        list = FXCollections.observableArrayList();
        String query = "select propiedad.idpropiedad as idpropiedad, propiedad.direccion as direccion, propiedad.nombreagencia as nombreagencia, propiedad.fechapublicacion as fechapublicacion, propiedad.habitaciones as habitaciones, propiedad.VEI as VEI, tipopropiedad.tipo as tipo\n" +
                "from propiedad, tipopropiedad\n" +
                "where propiedad.idestadoeliminado = 1 and propiedad.idestadoocupacion = 1 and propiedad.idusuariodueno != '%' and propiedad.idpropiedad not in\n" +
                "(select visita.idpropiedad from visita where visita.idusuario = % ) and propiedad.idtipopropiedad = tipopropiedad.idtipopropiedad";

        query = query.replaceFirst("%", idusuario);
        query = query.replaceFirst("%", idusuario);

        conn = conectar.getConnection();
        ResultSet set = conn.createStatement().executeQuery(query);

        while (set.next()) {
            //Nombre de la columna en la BDD
            Propiedad propiedad = new Propiedad();
            propiedad.setId((set.getInt("idpropiedad")));
            propiedad.setDireccion(set.getString("direccion"));
            propiedad.setAgencia(set.getString("nombreagencia"));
            propiedad.setFecha(set.getString("fechapublicacion"));
            propiedad.setHabitaciones(set.getInt("habitaciones"));
            propiedad.setVEI(set.getFloat("VEI"));
            propiedad.setTipo(set.getString("tipo"));


            list.add(propiedad);
        }

        //Nombre de la variable en la clase propiedad
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAgencia.setCellValueFactory(new PropertyValueFactory<>("agencia"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHabitacion.setCellValueFactory(new PropertyValueFactory<>("habitaciones"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("VEI"));
        colTipoPropiedad.setCellValueFactory(new PropertyValueFactory<>("tipo"));


        //lest create a cell factory ti insert a button
        Callback<TableColumn<Propiedad,String>,TableCell<Propiedad,String>> cellFactory = (param) -> {
            //make the tablecell containing button
            final TableCell<Propiedad,String> cell= new TableCell<Propiedad,String>(){

                //override updateItem method
                @Override
                public void  updateItem(String item, boolean empty){
                    super.updateItem(item, empty);

                    if(empty) {
                        setGraphic(null);
                        setText(null);
                    }
                    else {
                        final Button editButton = new Button("Añadir");
                        editButton.setOnAction(event ->{
                            Propiedad p = getTableView().getItems().get(getIndex());
                            String id = String.valueOf(p.getId());

                            String aquery = "insert into visita(fecha,idusuario,idpropiedad) values (Date '2022-10-10','%','%')";

                            aquery = aquery.replaceFirst("%", idusuario);
                            aquery = aquery.replaceFirst("%", id);

                            conn = conectar.getConnection();
                            try {
                                conn.createStatement().executeQuery(aquery);
                                conectar.commit();
                            } catch (SQLException e) {
                            }

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal/menuPrincipal.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();

                                AlertWindowNegocio.setLabelValue("Propiedad añadida a su lista de visitas");
                                Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                                Scene scene1 = new Scene(root1);
                                Stage stage1 = new Stage();
                                stage1.initStyle(StageStyle.TRANSPARENT);
                                stage1.setScene(scene1);
                                stage1.show();
                            }
                            catch (Exception e)
                            {
                                System.out.println("no se cambio de pantalla");
                            }
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

    public void actualizarFiltroRenta(){
        final int[] filtro = new int[1];

        sliderRenta.setValue(0);

        sliderRenta.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                filtro[0] = (int) sliderRenta.getValue();
                filtroRenta.setText("$ " + Integer.toString(filtro[0]));
            }
        });
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

    private void  populateTableViewFiltro() throws SQLException, IOException {
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();

        //Obtiene usuario
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);

        //Pone el nombre de usuario
        String idusuario = DAO.getValueFromResultset(cuenta, 1);


        list = FXCollections.observableArrayList();
        String query2 = "select propiedad.idpropiedad as idpropiedad, propiedad.direccion as direccion, propiedad.nombreagencia as nombreagencia, propiedad.fechapublicacion as fechapublicacion, propiedad.habitaciones as habitaciones, propiedad.VEI as VEI, tipopropiedad.tipo as tipo from propiedad, tipopropiedad where propiedad.idestadoeliminado = 1 and propiedad.idestadoocupacion = 1 and propiedad.idusuariodueno != '%'";

        //Conseguir el valor del slider filtro renta
        final int[] filtro = new int[1];
        filtro[0] = (int) sliderRenta.getValue();

        //Conseguir el valor del choicebox filtro tipoPropiedad
        String tipoP = choiceFiltroPropiedad.getValue();


        if (tipoP == "Casa"){
            tipoP = "2";
        }
        else if(tipoP == "Apartamento"){
            tipoP = "1";
        }
        else{
            tipoP = "0";
        }


        //Conseguir el valor del spinner filtro Habitaciones
        Integer habitaciones = spinnerHabitaciones.getValue();

        //Conseguir el valor del choicebox filtro Ubicacion
        String ubi = choiceFiltroUbicacion.getValue();

        //System.out.println("Valor choicebox ubicacion: " + ubi);

        if(filtro[0] != 0){
            query2 = query2.concat(" and propiedad.vei <=" + filtro[0]);
        }

        if(tipoP != "0"){
            query2 = query2.concat(" and propiedad.idtipopropiedad =" + tipoP);
        }

        if(habitaciones != 0){
            query2 = query2.concat(" and propiedad.habitaciones =" + habitaciones);
        }

        if(ubi != "Seleccionar"){
            query2 = query2.concat(" and idubicacion in (select idubicacion from ubicacion where municipio ='" + ubi + "')");
        }

        query2 = query2.concat(" and propiedad.idpropiedad not in (select visita.idpropiedad from visita where visita.idusuario = '%') and propiedad.idtipopropiedad = tipopropiedad.idtipopropiedad");

        query2 = query2.replaceFirst("%", idusuario);
        query2 = query2.replaceFirst("%", idusuario);


        conn = conectar.getConnection();
        ResultSet set = conn.createStatement().executeQuery(query2);

        while (set.next()) {
            //Nombre de la columna en la BDD
            Propiedad propiedad = new Propiedad();
            propiedad.setId((set.getInt("idpropiedad")));
            propiedad.setDireccion(set.getString("direccion"));
            propiedad.setAgencia(set.getString("nombreagencia"));
            propiedad.setFecha(set.getString("fechapublicacion"));
            propiedad.setHabitaciones(set.getInt("habitaciones"));
            propiedad.setVEI(set.getFloat("VEI"));
            propiedad.setTipo(set.getString("tipo"));

            list.add(propiedad);
        }

        //Nombre de la variable en la clase propiedad
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAgencia.setCellValueFactory(new PropertyValueFactory<>("agencia"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHabitacion.setCellValueFactory(new PropertyValueFactory<>("habitaciones"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("VEI"));
        colTipoPropiedad.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        //lest create a cell factory ti insert a button
        Callback<TableColumn<Propiedad,String>,TableCell<Propiedad,String>> cellFactory = (param) -> {
            //make the tablecell containing button
            final TableCell<Propiedad,String> cell= new TableCell<Propiedad,String>(){

                //override updateItem method
                @Override
                public void  updateItem(String item, boolean empty){
                    super.updateItem(item, empty);

                    if(empty) {
                        setGraphic(null);
                        setText(null);
                    }
                    else {
                        final Button editButton = new Button("Añadir");
                        editButton.setOnAction(event ->{
                            Propiedad p = getTableView().getItems().get(getIndex());
                            String id = String.valueOf(p.getId());

                            String aquery = "insert into visita(fecha,idusuario,idpropiedad) values (Date '2022-10-10','%','%')";

                            aquery = aquery.replaceFirst("%", idusuario);
                            aquery = aquery.replaceFirst("%", id);

                            conn = conectar.getConnection();
                            try {
                                conn.createStatement().executeQuery(aquery);
                                conectar.commit();
                            } catch (SQLException e) {
                            }

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal/menuPrincipal.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();

                                AlertWindowNegocio.setLabelValue("La propiedad ha sido añadida a su lista de visitas");
                                Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                                Scene scene1 = new Scene(root1);
                                Stage stage1 = new Stage();
                                stage1.initStyle(StageStyle.TRANSPARENT);
                                stage1.setScene(scene1);
                                stage1.show();
                            }
                            catch (Exception e)
                            {
                                System.out.println("no se cambio de pantalla");
                            }
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

    @FXML
    private ImageView minimize;
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
    public void refresh(MouseEvent mouseEvent) throws SQLException, IOException {
        list.clear();
        populateTableView();

        populateTableViewFiltro();
    }

    public void reiniciarFiltros(MouseEvent mouseEvent) throws SQLException, IOException{
        actualizarFiltroHabitaciones();
        actualizarFiltroRenta();
        actualizarFiltroTipoPropiedad();
        actualizarFiltroUbicaciones();
        populateTableView();
    }

    public void refrescar(ActionEvent event) throws SQLException, IOException {
        populateTableView();
    }
}
