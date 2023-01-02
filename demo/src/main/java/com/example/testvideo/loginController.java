package com.example.testvideo;

import com.example.testvideo.negocio.loginNegocio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class loginController {
    @FXML
    private Label test;
    @FXML
    private TextField inicioSesionUsuario;
    @FXML
    private TextField inicioSesionContrasena;
    @FXML
    private ImageView minimize;


    public void submit(ActionEvent event) {
        try {
            //Obtiene campos de login
            String usuario = inicioSesionUsuario.getText();
            String contrasena = inicioSesionContrasena.getText();

            //Comprueba si el usuario existe
            String msg = DAO.userExist(usuario, contrasena);

            //Inicia sesion
            if (msg.equals("Sesion correcta"))
            {
                msg = DAO.isLogged(usuario);

                if(msg.equals("Sesion iniciada"))
                {
                    loginNegocio.escribirCache(usuario,contrasena);

                    //Siguiente pantalla
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal/menuPrincipal.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
            //Resetea los campos
            test.setText(msg);
            test.setVisible(true);
            inicioSesionUsuario.setText("");
            inicioSesionContrasena.setText("");
        }
        catch (Exception e)
        {
            test.setText("error: " + e.getMessage());
        }
    }

    public void minimizarVentana(MouseEvent mouseEvent) {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void cerrarVentana(MouseEvent mouseEvent) {
        //Se desconecta con el servidor
        conectar.desconectar();
        javafx.application.Platform.exit();
    }

    @FXML
    void initialize() {
        conectar.getConnection();//Establece la conexion
        if(! conectar.isConnected()) {
                test.setText("Error al conectar con la base de datos");
                test.setVisible(true);
        }
    }
}