package com.example.testvideo.negocio;

import com.example.testvideo.DAO;
import com.example.testvideo.conectar;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class menuPrincipalNegocio {
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
}
