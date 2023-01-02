package com.example.testvideo.negocio;

import com.example.testvideo.DAO;
import com.example.testvideo.conectar;
import com.example.testvideo.servicio;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class gestionarServiciosNegocio {

    private static String propiedad = "";
    private static String idRenta = "";

    //guardar()
    public static void guardar(String idusuario, Label varCiudad, Label varDireccion, Label varHabitacion, Label varTipoPropiedad, ObservableList<servicio> list) throws IOException, SQLException {
        list.clear();

        Connection conn = conectar.getConnection();
        String sql = "select * from propiedad where idusuarioinquilino = %";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", idusuario);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs.next();

        String ubicacion = rs.getString(11);
        varDireccion.setText(rs.getString(2));
        varHabitacion.setText(rs.getString(4));
        varTipoPropiedad.setText(rs.getString(10));

        if(varTipoPropiedad.getText() == "1"){
            varTipoPropiedad.setText("Apartamento");
        }
        else{
            varTipoPropiedad.setText("Casa");
        }

        propiedad = rs.getString(1);

        String sql3 = "select max(idRenta) from renta where idpropiedad = %";

        Statement st3 = null;
        ResultSet rs3 = null;

        try {
            sql3 = sql3.replaceFirst("%", propiedad);
            st3 = conn.createStatement();
            rs3 = st3.executeQuery(sql3);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs3.next();
        idRenta = rs3.getString(1);

        String sql1 = "select * from ubicacion where idubicacion = %";

        Statement st1 = null;
        ResultSet rs1 = null;

        try {
            sql1 = sql1.replaceFirst("%", ubicacion);

            st1 = conn.createStatement();
            rs1 = st1.executeQuery(sql1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs1.next();

        String ciudad = rs1.getString(3);
        ciudad +=", " + rs1.getString(4);
        varCiudad.setText(ciudad);

        String sql2 = "select tiposervicio.nombreservicio, servicios.numeroinquilinos  from servicios, tiposervicio where servicios.idrenta = % and servicios.idtiposervicio = tiposervicio.idtiposervicio";

        Statement st2 = null;
        ResultSet rs2 = null;

        try {
            sql2 = sql2.replaceFirst("%", idRenta);

            st2 = conn.createStatement();
            rs2 = st2.executeQuery(sql2);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        while (rs2.next()) {
            servicio service = new servicio(rs2.getString(1), Integer.parseInt(rs2.getString(2)));
            list.add(service);
        }
    }

    //getUsuario()
    public static ResultSet getUsuario(String usuario, String contrasena) throws SQLException {
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);

        return cuenta;
    };

    public static String getIdUsuario(ResultSet cuenta) throws SQLException {
        String idusuario;

        idusuario = DAO.getValueFromResultset(cuenta,1);

        return idusuario;
    };

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

        String idusuario = rs.getString(3);
        idusuario +=", " + rs.getString(4);

        return idusuario;
    }

    //GetServicio() que se usa para mostrar listar los servicios
    public static String[] getServicios() throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select nombreServicio from tipoServicio";

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

    public static void actualizarServicios(ObservableList<servicio> list) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "DELETE FROM servicios WHERE idrenta = %";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", idRenta);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < list.size(); i++) {
            String cantidad = Integer.toString(list.get(i).getCantidad());
            String nombre = list.get(i).getName();

            String sql2 = "select idtiposervicio from tiposervicio where nombreservicio = '%'";

            Statement st2 = null;
            ResultSet rs2 = null;

            try {
                sql2 = sql2.replaceFirst("%", nombre);
                st2 = conn.createStatement();
                rs2 = st2.executeQuery(sql2);
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            rs2.next();
            String idNombre = rs2.getString(1);

            String sql1 = "insert into servicios (idRenta,numeroinquilinos,idtiposervicio) values (%,%,%)";

            Statement st1 = null;
            ResultSet rs1 = null;

            try {
                sql1 = sql1.replaceFirst("%", idRenta);
                sql1 = sql1.replaceFirst("%", cantidad);
                sql1 = sql1.replaceFirst("%", idNombre);
                st1 = conn.createStatement();
                rs1 = st1.executeQuery(sql1);
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void removerDeLista(servicio service, ObservableList<servicio> list) {
        for (int i = 0; i < list.size(); i++) {
            if (service.getName().equals(list.get(i).getName())) {
                list.remove(i);
            }
        }
    }

    public static void abc(Label resultado, ObservableList<servicio> list, servicio service, TableView<servicio> tablePersona, Label totalServicios) {
        if (service.getName().equals("") || service.getCantidad() == 0) {
            resultado.setText("Llene todos los campos para añadir el servicio");
        } else {
            gestionarServiciosNegocio.removerDeLista(service,list);
            list.add(service);
            resultado.setText("servicio agregado");
            resultado.setVisible(true);
            calcularTotal(tablePersona, totalServicios);
        }
    }

    public static void calcularTotal(TableView<servicio> tablePersona, Label totalServicios){
        int inquilinos = 0;
        float valorTotalServicios = 0;
        String nombre;


        TableColumn col = tablePersona.getColumns().get(1);

        for(int i=0; i<tablePersona.getItems().size(); i++){
            servicio s = tablePersona.getItems().get(i);
            inquilinos += (int) col.getCellObservableValue(s).getValue();
        }

        valorTotalServicios = 20*inquilinos;

        totalServicios.setText("$ " + valorTotalServicios);
    }

    public static void eliminarLineaServicio(String nombre, ObservableList<servicio> list, Label resultado) {
        for (int i = 0; i < list.size(); i++) {
            if (nombre.equals(list.get(i).getName())) {
                list.remove(i);
            }
        }
        resultado.setText("Servicio eliminado");
        resultado.setVisible(true);
    }
}
