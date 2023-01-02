package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import com.example.testvideo.negocio.cache;
import com.example.testvideo.negocio.historialVisitasNegocio;
import com.example.testvideo.negocio.verPropiedadesNegocio;
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

public class misPropiedadesController {

    @FXML
    private TableView<Propiedad> tablePersona;

    @FXML
    private TableColumn<Propiedad, Integer> colId;

    @FXML
    private TableColumn<Propiedad, String> colDireccion;

    @FXML
    private TableColumn<Propiedad, String> colFecha;

    @FXML
    private TableColumn<Propiedad, Integer> colHabitacion;

    @FXML
    private TableColumn<Propiedad, Float> colValor;

    @FXML
    private TableColumn<Propiedad, Integer> colTipoPropiedad;

    @FXML
    private TableColumn colEdit;

    @FXML
    private TableColumn colEdit1;

    private Connection conn;
    private ObservableList<Propiedad> list;

    @FXML
    void initialize() throws IOException, SQLException {
        populateTableView();
    }
    private void  populateTableView() throws SQLException, IOException {
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();

        //Obtiene usuario
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);

        //Pone el nombre de usuario
        String idusuario = DAO.getValueFromResultset(cuenta, 1);


        list = FXCollections.observableArrayList();
        String query = "select * \n" +
                "from propiedad \n" +
                "where propiedad.idestadoeliminado = 1 and propiedad.idusuariodueno = %";

        query = query.replaceFirst("%", idusuario);

        conn = conectar.getConnection();
        ResultSet set = conn.createStatement().executeQuery(query);

        while (set.next()) {
            //Nombre de la columna en la BDD

            Propiedad propiedad = new Propiedad();
            propiedad.setId((set.getInt("idpropiedad")));
            propiedad.setDireccion(set.getString("direccion"));
            propiedad.setFecha(set.getString("fechapublicacion"));
            propiedad.setAgencia(set.getString("nombreagencia"));
            propiedad.setVEI(set.getFloat("VEI"));

            if(set.getInt("idestadoocupacion") == 1) {
                propiedad.setIdestadoOcupado("No");
            }
            else if(set.getInt("idestadoocupacion") == 2) {
                propiedad.setIdestadoOcupado("Si");
            }

            list.add(propiedad);
        }

        //Nombre de la variable en la clase propiedad
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHabitacion.setCellValueFactory(new PropertyValueFactory<>("agencia"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("VEI"));
        colTipoPropiedad.setCellValueFactory(new PropertyValueFactory<>("idestadoOcupado"));

        //lest create a cell factory ti insert a button
        Callback<TableColumn<Propiedad,String>, TableCell<Propiedad,String>> cellFactory = (param) -> {
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
                        final Button editButton = new Button("Comentarios");
                        editButton.setOnAction(event ->{
                            Propiedad p = getTableView().getItems().get(getIndex());
                            String id = String.valueOf(p.getId());

                            verPropiedadesNegocio.escribirCache(id);
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("comentarios/comentarios.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            }
                            catch (Exception e)
                            {
                                System.out.println("Error al procesar el evento");
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

        Callback<TableColumn<Propiedad,String>, TableCell<Propiedad,String>> cellFactory1 = (param) -> {
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
                        final Button editButton = new Button("Eliminar");
                        editButton.setOnAction(event ->{
                            Propiedad p = getTableView().getItems().get(getIndex());
                            String id = String.valueOf(p.getId());
                            String ocupado = String.valueOf((p.getIdestadoOcupado()));

                            try {
                                historialVisitasNegocio.escribirCache(id);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }

                            try {
                                if(ocupado.equals("No")) {
                                    if (DAO.eliminarPropiedad(id)) {
                                        AlertWindowNegocio.setLabelValue("Propiedad eliminada");
                                        Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                                        Scene scene1 = new Scene(root1);
                                        Stage stage1 = new Stage();
                                        stage1.initStyle(StageStyle.TRANSPARENT);
                                        stage1.setScene(scene1);
                                        stage1.show();
                                    }
                                    else {
                                        AlertWindowNegocio.setLabelValue("La propiedad ha sido recientemente ocupada \n no se puede eliminar");
                                        Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                                        Scene scene1 = new Scene(root1);
                                        Stage stage1 = new Stage();
                                        stage1.initStyle(StageStyle.TRANSPARENT);
                                        stage1.setScene(scene1);
                                        stage1.show();
                                    }
                                    populateTableView();
                                }
                                else {
                                    AlertWindowNegocio.setLabelValue("No puede eliminar una propiedad que esta ocupada");
                                    Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                                    Scene scene1 = new Scene(root1);
                                    Stage stage1 = new Stage();
                                    stage1.initStyle(StageStyle.TRANSPARENT);
                                    stage1.setScene(scene1);
                                    stage1.show();
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage());
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
        colEdit1.setCellFactory(cellFactory1);
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

    public void addProperty(ActionEvent event) throws IOException {
        //Siguiente pantalla
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevaPropiedad/nuevaPropiedad.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}