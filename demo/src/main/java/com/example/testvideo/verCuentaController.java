package com.example.testvideo;

import com.example.testvideo.negocio.AlertWindowNegocio;
import com.example.testvideo.negocio.cache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;



public class verCuentaController {

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellido;

    @FXML
    private TextField campoDocumento;

    @FXML
    private TextField campoCorreo;

    @FXML
    private Label campoTipoCuenta;

    @FXML
    private Label campoFecha;

    @FXML
    private Label campoNombreUsuario;

    @FXML
    private Label campoTipoDocumento;

    @FXML
    private Label contraIncorrecta;

    @FXML
    private Label contraNoCoincide;

    @FXML
    private PasswordField campoContrasena;

    @FXML
    private PasswordField campoContrasenaNueva;

    @FXML
    private PasswordField campoContrasenaNueva1;


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

    @FXML
    void initialize() {
        try {
            //Lee cache
            String usuario = cache.getUserName();
            String contrasena = cache.getContrasena();

            //Obtiene usuario
            ResultSet cuenta = DAO.getCuenta(usuario, contrasena);
            campoNombre.setText(DAO.getValueFromResultset(cuenta, 4));
            campoApellido.setText(DAO.getValueFromResultset(cuenta, 5));
            campoDocumento.setText(DAO.getValueFromResultset(cuenta, 8));
            campoCorreo.setText(DAO.getValueFromResultset(cuenta, 6));
            campoTipoCuenta.setText(DAO.getValueFromResultset(cuenta, 9));
            campoFecha.setText(DAO.getValueFromResultset(cuenta, 7));
            campoNombreUsuario.setText(DAO.getValueFromResultset(cuenta, 2));
            if (DAO.getValueFromResultset(cuenta,11).equals("1"))
            {
                campoTipoDocumento.setText("CC");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void menuPrincipal(MouseEvent event) throws IOException {
        //Siguiente pantalla
        initialize();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal/menuPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void actualizarCuenta(ActionEvent actionEvent) {
        //CONTRASEÑA
        if(actualizarContra(actionEvent)) {
            //Actualizar informacion personal
            actualizarInfoPersonal();
        }
    }

    public void actualizarInfoPersonal(){

        String nombre = this.campoNombre.getText();
        String apellido = this.campoApellido.getText();
        String documento = this.campoDocumento.getText();
        String correo = this.campoCorreo.getText();

        boolean validar;
        try {
            String username = this.campoNombreUsuario.getText();
            validar = DAO.updateCuenta(username, nombre, apellido, documento, correo);
            if (validar) {
                AlertWindowNegocio.setLabelValue("Información personal actualizada correctamente");
                Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                Scene scene1 = new Scene(root1);
                Stage stage1 = new Stage();
                stage1.initStyle(StageStyle.TRANSPARENT);
                stage1.setScene(scene1);
                stage1.show();
            } else if (!validar) {
                AlertWindowNegocio.setLabelValue("Error, no se actualizó la información personal");
                Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                Scene scene1 = new Scene(root1);
                Stage stage1 = new Stage();
                stage1.initStyle(StageStyle.TRANSPARENT);
                stage1.setScene(scene1);
                stage1.show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean actualizarContra(ActionEvent actionEvent){
        if (!this.campoContrasena.getText().equals(""))
        {
            try {
                String password;
                String username = this.campoNombreUsuario.getText();
                String campoContra = this.campoContrasena.getText();

                ResultSet rs = DAO.getContra(username);

                password = DAO.getValueFromResultset(rs, 1);

                if (password.equals(campoContra)){
                    contraIncorrecta.setVisible(false);
                    if ((!this.campoContrasenaNueva.getText().equals(""))&&(!this.campoContrasenaNueva1.getText().equals("")))
                    {
                        contraNoCoincide.setVisible(false);
                        String contraNueva = this.campoContrasenaNueva.getText();
                        String contraNueva1 = this.campoContrasenaNueva1.getText();
                        if (contraNueva.equals(contraNueva1)){
                            DAO.updateContra(contraNueva,username);

                            try {
                                DAO.deslogear();

                                initialize();
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("login/inicioSesion.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                                AlertWindowNegocio.setLabelValue("Contraseña actualizada, vuelva a iniciar sesion");
                                Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
                                Scene scene1 = new Scene(root1);
                                Stage stage1 = new Stage();
                                stage1.initStyle(StageStyle.TRANSPARENT);
                                stage1.setScene(scene1);
                                stage1.show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            contraNoCoincide.setText("Las contraseñas no coinciden");
                            contraNoCoincide.setVisible(true);
                        }
                    }
                }else{
                    contraIncorrecta.setText("Contraseña ingresada no coincide con la contraseña actual");
                    contraIncorrecta.setVisible(true);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return  false;
        }
        return true;
    }


    public void eliminarCuenta(ActionEvent actionEvent) throws SQLException {
        String username = this.campoNombreUsuario.getText();
        DAO.updateEstadoCuenta(username);
        try {
            initialize();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login/inicioSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AlertWindowNegocio.setLabelValue("Cuenta Eliminada");
            Parent root1 = FXMLLoader.load(getClass().getResource("AlertWindow/AlertWindow.fxml"));
            Scene scene1 = new Scene(root1);
            Stage stage1 = new Stage();
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setScene(scene1);
            stage1.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
