package com.example.testvideo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conectar {
    //kfjdkhjsdfjkfjshh
    public static Connection conn = null;
    private static final String login = "is1270206";
    private static final String password  ="mfCY6FoB7Q";
    private static final String url = "jdbc:oracle:thin:@orion.javeriana.edu.co:1521/LAB";

    public static Connection getConnection() {
        if (conn == null)
        {
            try {
                conn = DriverManager.getConnection(url, login, password);
                //conn.setAutoCommit(false);
                if(conn != null) {
                    System.out.println("Conexion exitosa");
                }
                else {
                    System.out.println("Conexion erronea");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void desconectar() {
        try {
            conn.close();
        }
        catch (Exception e) {
            System.out.println("Error al desconectar");
        }
    }

    public static boolean isConnected()
    {
        return  (conn != null);
    }

    public static void commit() throws SQLException {
        conn.commit();
    }
}
