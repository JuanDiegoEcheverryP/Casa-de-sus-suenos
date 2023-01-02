package com.example.testvideo.negocio;

import com.example.testvideo.DAO;
import com.example.testvideo.Propiedad;
import com.example.testvideo.conectar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class historialVisitasNegocio {
    public static void escribirCache(String idpropiedad) throws IOException {
        //Escribe el login en cache
        cache.setPropiedad(idpropiedad);
    }

    public static void escribirCacheVisita(String idVisita) throws IOException {
        cache.setComentarios(idVisita);
    }

    public static boolean haveProperty() throws IOException, SQLException {
        //Lee cache
        String usuario = cache.getUserName();
        String contrasena = cache.getContrasena();

        //Obtiene usuario
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);
        String idusuario = DAO.getValueFromResultset(cuenta,1);

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
        if(rs.next()) {
            return true;
        }
        else {
            return false;
        }
    }

    public static ResultSet populateQuery(Connection conn, String idusuario) throws SQLException {
        String query = "select propiedad.*,visita.*, tipopropiedad.tipo as tipopropiedad\n" +
                "from propiedad, tipopropiedad, visita\n" +
                "where visita.idpropiedad = propiedad.idpropiedad and propiedad.idestadoeliminado = 1 and propiedad.idestadoocupacion = 1 and propiedad.idpropiedad in\n" +
                "(select visita.idpropiedad from visita where visita.idusuario = % ) and visita.idusuario = % and propiedad.idtipopropiedad = tipopropiedad.idtipopropiedad";

        query = query.replaceFirst("%", idusuario);
        query = query.replaceFirst("%", idusuario);

        conn = conectar.getConnection();
        return conn.createStatement().executeQuery(query);
    }

    public static String getcuenta(String usuario, String contrasena) throws SQLException {
        ResultSet cuenta = DAO.getCuenta(usuario, contrasena);
        return DAO.getValueFromResultset(cuenta, 1);
    }

    public static ObservableList<Propiedad> BDDToPropiedad(ResultSet set) throws SQLException {
        ObservableList<Propiedad> tempList = FXCollections.observableArrayList();

        while (set.next()) {
            //Nombre de la columna en la BDD
            Propiedad propiedad = new Propiedad();
            propiedad.setId((set.getInt("idpropiedad")));
            propiedad.setDireccion(set.getString("direccion"));
            propiedad.setFecha(set.getString("fechapublicacion"));
            propiedad.setHabitaciones(set.getInt("habitaciones"));
            propiedad.setVEI(set.getFloat("VEI"));
            propiedad.setTipo(set.getString("tipopropiedad"));
            propiedad.setIdIVisita(set.getInt("idvvisita"));

            tempList.add(propiedad);
        }
        return tempList;
    }
}
