package com.example.testvideo;

import com.example.testvideo.negocio.cache;
import com.example.testvideo.negocio.comentariosNegocio;
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
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class comentariosController {
    String regreso = "historialVisitas/historialVisitas.fxml";

    @FXML
    private Button toMenuPrincipal;
    @FXML
    private ImageView close;

    @FXML
    private TextField comentarioCampo;

    @FXML
    private ImageView minimize;

    @FXML
    private Button publicar;

    @FXML
    private TableView<comentario> tablaComentarios;

    @FXML
    private TableColumn<comentario, Integer> idComentario;

    @FXML
    private TableColumn<comentario, String> usuarioComentario;

    @FXML
    private TableColumn<comentario, String> usuarioName;

    @FXML
    private Label varCiudad;

    @FXML
    private Label varDireccion;

    @FXML
    private Label varHabitacion;

    @FXML
    private Label varTipoPropiedad;

    @FXML
    void publicar(ActionEvent event) throws IOException, SQLException {
        if (comentarioCampo.getText().equals("")) {
            //Alerta
        }
        else
        {

            String texto = comentarioCampo.getText();

            String visita = cache.getComentarios();

            Connection conn = conectar.getConnection();
            String sql = "insert into comentario(texto,idvvisita) values ('%', %)";

            Statement st = null;
            ResultSet rs = null;

            try {
                sql = sql.replaceFirst("%", texto);
                sql = sql.replaceFirst("%", visita);

                st = conn.createStatement();
                rs = st.executeQuery(sql);
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }


            System.out.println(visita + " : " + texto);
            comentarioCampo.clear();
        }
        populateTableView();
    }
    private Connection conn;
    private ObservableList<comentario> list;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(regreso));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() throws IOException, SQLException {
        populateTableView();

        try {
            //Lee cache
            String id = cache.getPropiedad();

            //Obtiene cuenta
            ResultSet cuenta = DAO.getPropiedad(id);

            varCiudad.setText(rentarPropiedadNegocio.ciudad(DAO.getValueFromResultset(DAO.getPropiedad(id),11)));
            varDireccion.setText(DAO.getValueFromResultset(cuenta,2));
            varTipoPropiedad.setText(DAO.getValueFromResultset(cuenta,10));
            varHabitacion.setText(DAO.getValueFromResultset(cuenta,4));

            //System.out.println("Tipo de propiedad encontrada: " + varTipoPropiedad.getText());

            if(varTipoPropiedad.getText() == "1"){
                varTipoPropiedad.setText("Apartamento");
            }
            else{
                varTipoPropiedad.setText("Casa");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if(comentariosNegocio.isOwner()){
            regreso = "misPropiedades/misPropiedades.fxml";
            toMenuPrincipal.setText("Volver a mis propiedades");
            comentarioCampo.setDisable(true);
            publicar.setDisable(true);
        }

    }

    private void populateTableView() throws SQLException, IOException {
        String propiedad2 = cache.getPropiedad();

        //Obtiene usuario
        ResultSet propiedad1 = DAO.getPropiedad(propiedad2);

        //Pone el nombre de usuario
        String idpropiedad = DAO.getValueFromResultset(propiedad1, 1);

        list = FXCollections.observableArrayList();
        String query = "select comentario.idcomentario as idcomentario, comentario.texto as texto, cuenta.nombreusuario as usuario \n" +
                "from visita,comentario,cuenta \n" +
                "where cuenta.idusuario = visita.idusuario and \n" +
                "visita.idvvisita = comentario.idvvisita and \n" +
                "visita.idpropiedad = %";

        query = query.replaceFirst("%", idpropiedad);

        conn = conectar.getConnection();
        ResultSet set = conn.createStatement().executeQuery(query);

        while (set.next()) {
            //Nombre de la columna en la BDD
            comentario propiedad = new comentario();
            propiedad.setIdComentario(set.getInt("idcomentario"));
            propiedad.setUsuario((set.getString("usuario")));
            propiedad.setTexto(set.getString("texto"));

            list.add(propiedad);
        }
        idComentario.setCellValueFactory(new PropertyValueFactory<>("idComentario"));
        usuarioName.setCellValueFactory(new PropertyValueFactory<>("Usuario"));
        usuarioComentario.setCellValueFactory(new PropertyValueFactory<>("texto"));

        tablaComentarios.setItems(list);
        tablaComentarios.getSortOrder().add(idComentario);
    }

    public void refrescarComentarios(ActionEvent event) throws SQLException, IOException {
        populateTableView();
    }
}