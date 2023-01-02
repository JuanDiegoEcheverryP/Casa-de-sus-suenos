package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import com.example.testvideo.negocio.cache;
import com.example.testvideo.negocio.historialVisitasNegocio;
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

public class historialVisitasController {
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
    private TableColumn<Propiedad, String> colTipoPropiedad;

    @FXML
    private TableColumn<Propiedad, Integer> colVisita;

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
    private void  populateTableView() throws SQLException {
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();

        //Obtiene id del usuario
        String idusuario = historialVisitasNegocio.getcuenta(usuario,contrasena);

        ResultSet set = historialVisitasNegocio.populateQuery(conn, idusuario);

        list = historialVisitasNegocio.BDDToPropiedad(set);

        //Nombre de la variable en la clase propiedad
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHabitacion.setCellValueFactory(new PropertyValueFactory<>("habitaciones"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("VEI"));
        colTipoPropiedad.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colVisita.setCellValueFactory(new PropertyValueFactory<>("idIVisita"));

        //Boton de Renta en cada fila
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
                        final Button editButton = new Button("Rentar");
                        editButton.setOnAction(event ->{
                            Propiedad p = getTableView().getItems().get(getIndex());
                            String id = String.valueOf(p.getId());

                            try {
                                historialVisitasNegocio.escribirCache(id);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            try {
                                if(! historialVisitasNegocio.haveProperty()) {
                                    int resultado = DAO.propiedadIsBlocked(id);
                                    switch (resultado) {
                                        case 1:
                                            alertWindow("Otro usuario esta rentando la propiedad");
                                            break;
                                        case 2:
                                            alertWindow("La propiedad ya esta rentada por otro usuario");
                                            break;
                                        case 3:
                                            alertWindow("La propiedad no esta disponible");
                                            populateTableView();
                                            break;
                                        default:
                                            DAO.bloquearPropiedad(id);
                                            cambiarPantalla("rentarPropiedad/rentarPropiedad.fxml", event);
                                            break;
                                    }
                                }
                                else {
                                    alertWindow("Ya tiene una propiedad rentada");
                                }
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

        //Boton de comentario en cada fila
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
                        final Button editButton = new Button("Comentarios");
                        editButton.setOnAction(event ->{
                            Propiedad p = getTableView().getItems().get(getIndex());
                            String id = String.valueOf(p.getId());
                            String idVisita = String.valueOf(p.getIdIVisita());

                            try {
                                historialVisitasNegocio.escribirCache(id);
                                historialVisitasNegocio.escribirCacheVisita(idVisita);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }

                            try {
                                cambiarPantalla("comentarios/comentarios.fxml", event);
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
    public void refresh(MouseEvent mouseEvent) throws SQLException, IOException {
        list.clear();
        populateTableView();
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