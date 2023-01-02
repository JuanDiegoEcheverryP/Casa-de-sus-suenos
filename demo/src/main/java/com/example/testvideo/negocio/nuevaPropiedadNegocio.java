package com.example.testvideo.negocio;

import com.example.testvideo.conectar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class nuevaPropiedadNegocio {
    public static void addPropiedad(String direccion,String fecha,String habitaciones,String precio,String usuario,String agencia,String tipoPropiedad,String ubicacion) {

        Connection conn = conectar.getConnection();
        String aquery = "insert into propiedad (direccion, fechapublicacion, \n" +
                "habitaciones, vei, \n" +
                "idestadoocupacion, idestadoeliminado, \n" +
                "idusuariodueno, nombreagencia, \n" +
                "idtipopropiedad, idubicacion, \n" +
                "tipoUso) \n" +
                "values ('%',date '%', %, %, 1, 1, %, '%', %, %, 1)";

        aquery = aquery.replaceFirst("%", direccion);
        aquery = aquery.replaceFirst("%", fecha);
        aquery = aquery.replaceFirst("%", habitaciones);
        aquery = aquery.replaceFirst("%", precio);
        aquery = aquery.replaceFirst("%", usuario);
        aquery = aquery.replaceFirst("%", agencia);
        aquery = aquery.replaceFirst("%", tipoPropiedad);
        aquery = aquery.replaceFirst("%", ubicacion);

        System.out.println(aquery);

        conn = conectar.getConnection();
        try {
            conn.createStatement().executeQuery(aquery);
            conectar.commit();
        } catch (SQLException e) {
        }
    }

    public static String idUbicacion(String ubicacion) {
        Connection conn = conectar.getConnection();
        String mensaje = "";

        String sql = "select idubicacion\n" +
                "from ubicacion\n" +
                "where municipio = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", ubicacion);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            mensaje = rs.getString(1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return mensaje;
    }

    public static String idTipo(String tipo) {
        Connection conn = conectar.getConnection();
        String mensaje = "";

        String sql = "select idtipopropiedad\n" +
                "from tipopropiedad\n" +
                "where tipo = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", tipo);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            mensaje = rs.getString(1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return mensaje;
    }
}
