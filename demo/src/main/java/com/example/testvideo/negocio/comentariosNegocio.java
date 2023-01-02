package com.example.testvideo.negocio;

import com.example.testvideo.conectar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class comentariosNegocio {

    public static boolean isOwner() {
        Connection conn = conectar.getConnection();
        String mensaje = "";
        String propiedad = cache.getPropiedad();
        String usuario = cache.getUserName();

        String sql = "select cuenta.nombreusuario\n" +
                "from propiedad, cuenta\n" +
                "where propiedad.idpropiedad = % and propiedad.idusuariodueno = cuenta.idusuario";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", propiedad);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();

            if(rs.getString(1).equals(usuario))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
