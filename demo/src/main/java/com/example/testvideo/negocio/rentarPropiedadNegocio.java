package com.example.testvideo.negocio;

import com.example.testvideo.DAO;
import com.example.testvideo.bono;
import com.example.testvideo.conectar;
import com.example.testvideo.totalizarRenta;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class rentarPropiedadNegocio {
    public static String ciudad(String ubicacion) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select * from ubicacion where idubicacion = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", ubicacion);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs.next();

        String ciudad = rs.getString(3);
        ciudad +=", " + rs.getString(4);

        return ciudad;
    }

    public static String[] tipoTarjeta() throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select tipo from tipotarjeta";

        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        List<String> list = new ArrayList<String>();
        while (rs.next()) {
            list.add(rs.getString(1));
        }

        String lista[] = new String[list.size()];

        int contador = 0;

        for(String a: list) {
            lista[contador] = a;
            contador++;
        }
        return  lista;
    }

    //validarFormulario()
    public static boolean bonoExist(String idBono) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select numerobono from bonos";

        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        while (rs.next()) {
            if (idBono.equals(rs.getString(1)))
            {
                return true;
            }
        }
        return false;
    }

    public static void actualizarEstadoPropiedad(String idPropiedad) throws SQLException {
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);
        String idusuario = DAO.getValueFromResultset(cuenta, 1);

        Connection conn = conectar.getConnection();
        String sql = "UPDATE propiedad SET idestadoocupacion = 2, idusuarioinquilino = % WHERE idpropiedad = %";

        Statement st = null;

        try {
            sql = sql.replaceFirst("%", idusuario);
            sql = sql.replaceFirst("%", idPropiedad);

            st = conn.createStatement();
            st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static int isDeleted() {
        Connection conn = conectar.getConnection();
        String propiedad = cache.getPropiedad();

        String sql = "select idestadoeliminado, idestadoocupacion\n" +
                "from propiedad\n" +
                "where propiedad.idpropiedad = %";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", propiedad);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();

            //Se ha eliminado la propiedad
            if(rs.getString(1).equals("2"))
            {
                return 1;
            }
            //Se ha ocupado la propiedad
            else if(rs.getString(2).equals("2"))
            {
                return 2;
            }
            //Se puede rentar
            else
            {
                return 3;
            }
        }
        catch (SQLException e)
        {
            return 4;
        }
    }

    public static String tipoPropiedad(String text) {
        if(text.equals("1")){
            return "Apartamento";
        }
        else{
            return "Casa";
        }
    }

    public static int addBono(ObservableList<bono> list, TableView<bono> varTablaBonos, int numeroBono, float valorBono) throws SQLException {
        boolean add = true;
        for (int i = 0; i < list.size(); i++) {
            if(numeroBono == list.get(i).getIdBono())
            {
                add = false;
            }
        }

        if (add) {
            if(! rentarPropiedadNegocio.bonoExist(Integer.toString(numeroBono))) {
                bono bonno = new bono(numeroBono,valorBono);
                varTablaBonos.getItems().add(bonno);
                return 1;
            }
            else {
                return 2;
            }
        }
        else {
            return 3;
        }
    }

    public static String obtenerInfo(ResultSet cuenta, int i) throws SQLException {
        return DAO.getValueFromResultset(cuenta,i);
    }

    public static ResultSet obtenerPropiedad(String id) throws SQLException {
        return DAO.getPropiedad(id);
    }

    public static float valorBonos(ObservableList<bono> list) {
        float sumabonos = 0;
        for (bono test : list) {
            sumabonos += test.getValorBono();
        }
        return sumabonos;
    }

    public static boolean bonossinRepetir(ObservableList<bono> list) throws SQLException {
        boolean valid = true;
        for (int i = 0; i < list.size(); i++) {
            String idBono = String.valueOf(list.get(i).getIdBono());
            if(rentarPropiedadNegocio.bonoExist(idBono)) {
                valid = false;
                i = list.size();
            }
        }
        return valid;
    }

    public static void realizarRenta(float VTR, float VTP, String idPropiedad, CheckBox varInputTarjeta, String numTarjeta, DatePicker varFecha, TextField varTitular, ChoiceBox<String> varTipoTarjeta, CheckBox varInputBono, ObservableList<bono> list) throws IOException, SQLException {
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();

        //Obtiene usuario
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);

        //Pone el nombre de usuario
        String correo = DAO.getValueFromResultset(cuenta, 6);

        //Insert renta BDD
        String idRenta =totalizarRenta.insertRenta(VTR,idPropiedad);

        //Insert Comisiones
        totalizarRenta.insertComisiones(idRenta);

        //Insert Pago
        String idPago = totalizarRenta.insertPago(correo,VTP,idRenta);

        //Insert Comisiones
        totalizarRenta.insertImpuestos(idPago);

        //Insert Tarjeta
        if (varInputTarjeta.isSelected()) {
            String mes = Integer.toString(varFecha.getValue().getMonth().getValue());
            String anno = Integer.toString(varFecha.getValue().getYear());

            totalizarRenta.insertTarjeta(numTarjeta, varTitular.getText(), mes, anno, varTipoTarjeta.getValue());
            totalizarRenta.vincularTarjetaPago(idPago,numTarjeta);
        }
        if (varInputBono.isSelected()) {
            for (bono test : list) {
                String numero = Integer.toString(test.getIdBono());
                float valor = test.getValorBono();
                totalizarRenta.insertarBonoPago(numero, valor, idPago);
            }
        }
        rentarPropiedadNegocio.actualizarEstadoPropiedad(idPropiedad);

        DAO.ocuparPropiedad();
        DAO.desbloquearPropiedad();
    }
}