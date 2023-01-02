package com.example.testvideo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class totalizarRenta {

    public static ArrayList<Integer> comision_Id = new ArrayList<Integer>();
    public static ArrayList<Float> comision_Valor = new ArrayList<Float>();

    public static ArrayList<Integer> Impuesto_Id = new ArrayList<Integer>();
    public static ArrayList<Float> Impuesto_Valor = new ArrayList<Float>();

    //calcularVTR
    public static float calcularVTR(String id) throws SQLException {
        comision_Id.clear();
        comision_Valor.clear();


        Connection conn = conectar.getConnection();
        float VEI = getVei(id);

        String sql1 = "select * from tipocomision_impuesto";

        Statement st1 = null;
        ResultSet rs1 = null;

        try {
            st1 = conn.createStatement();
            rs1 = st1.executeQuery(sql1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        float valorComisiones = 0;
        while (rs1.next()) {
            float porcentaje = Float.parseFloat(rs1.getString(3));

            comision_Id.add(Integer.parseInt(rs1.getString(1)));
            comision_Valor.add(VEI * porcentaje);

            valorComisiones += VEI * porcentaje;
        }
        float VTR = VEI + valorComisiones;
        return  VTR;
    }

    //calcularVei()
    public static float getVei(String id) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select VEI from propiedad where idpropiedad = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", id);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs.next();

        float VEI = Float.parseFloat(rs.getString(1));

        return VEI;
    }


    public static float getVTP(float VTR) throws SQLException {
        Impuesto_Valor.clear();
        Impuesto_Id.clear();

        Connection conn = conectar.getConnection();
        String sql = "select * from impuestos";

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
        float temporal = 0;
        while (rs.next()) {
            float porcentaje = Float.parseFloat(rs.getString(3));
            Impuesto_Id.add(Integer.parseInt(rs.getString(1)));
            Impuesto_Valor.add(VTR * porcentaje);
            temporal += VTR * porcentaje;
        }
        float VTP = VTR + temporal;
        return  VTP;
    }

    public static String insertRenta(float VTR, String idPropiedad) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "insert into renta (VTR,idpropiedad) values ('%','%')";

        Statement st = null;

        try {
            String a = String.format("%.2f", VTR);
            a.replaceFirst(",",".");

            sql = sql.replaceFirst("%", a);
            sql = sql.replaceFirst("%", idPropiedad);

            st = conn.createStatement();
            st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        String sql1 = "Select * from renta where idrenta=(select max(idrenta) from renta)";

        Statement st1 = null;
        ResultSet rs1 = null;

        try {
            st1 = conn.createStatement();
            rs1 = st1.executeQuery(sql1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs1.next();
        return rs1.getString(1);
    }

    public static void insertComisiones(String idRenta) {
        Connection conn = conectar.getConnection();

        for (int i = 0; i < comision_Id.size(); i++) {
            String sql = "insert into comision_impuesto(valor,idTipoComision_impuesto, idRenta) values (%,%,%)";

            Statement st = null;

            try {
                String a = String.format("%.2f", comision_Valor.get(i));
                a = a.replaceAll(",",".");

                sql = sql.replaceFirst("%", a);
                sql = sql.replaceFirst("%", Integer.toString(comision_Id.get(i)));
                sql = sql.replaceFirst("%", idRenta);

                st = conn.createStatement();
                st.executeQuery(sql);
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String insertPago(String Correo, float VTP,String IdRenta) throws SQLException {
        //Fecha
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fecha = currentLocalDateTime.format(dateTimeFormatter);

        Connection conn = conectar.getConnection();
        String sql = "insert into pago (correo, vtp,fechatransaccion,idrenta) values ('%',%, date '%', %)";

        Statement st = null;

        try {
            sql = sql.replaceFirst("%", Correo);

            String a = String.format("%.2f", VTP);
            a = a.replaceAll(",",".");

            sql = sql.replaceFirst("%", a);
            sql = sql.replaceFirst("%", fecha);
            sql = sql.replaceFirst("%", IdRenta);

            st = conn.createStatement();
            st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        String sql1 = "Select * from pago where idpago=(select max(idpago) from pago)";

        Statement st1 = null;
        ResultSet rs1 = null;

        try {
            st1 = conn.createStatement();
            rs1 = st1.executeQuery(sql1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs1.next();
        return rs1.getString(1);
    }

    public static String insertTarjeta(String numTarjeta, String poseedor, String mes, String anno, String tipo) throws SQLException {
        Connection conn = conectar.getConnection();

        String sql1 = "select * from tipotarjeta where tipo = '%'";

        Statement st1 = null;
        ResultSet rs1 = null;

        try {
            sql1 = sql1.replaceFirst("%", tipo);

            st1 = conn.createStatement();
            rs1 = st1.executeQuery(sql1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs1.next();
        String tipoId = rs1.getString(1);


        String sql = "insert into tarjeta (numerotarjeta,nombreposeedorprimario,mesfechavencimiento,anofechavencimiento,idtipotarjeta) values (%,'%',%,%,%)";

        Statement st = null;

        try {
            sql = sql.replaceFirst("%", numTarjeta);
            sql = sql.replaceFirst("%", poseedor);
            sql = sql.replaceFirst("%", mes);
            sql = sql.replaceFirst("%", anno);
            sql = sql.replaceFirst("%", tipoId);

            st = conn.createStatement();
            st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return numTarjeta;
    }

    public static void vincularTarjetaPago(String idPago, String tarjeta) {
        Connection conn = conectar.getConnection();
        String sql = "update pago set numeroTarjeta = % where idPago = %";

        Statement st = null;

        try {
            sql = sql.replaceFirst("%", tarjeta);
            sql = sql.replaceFirst("%", idPago);

            st = conn.createStatement();
            st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void insertarBonoPago(String numero, float valor, String idPago) {
        Connection conn = conectar.getConnection();
        String sql = "insert into bonos (numerobono,valorbono,idpago) values (%,%,%)";

        Statement st = null;

        try {

            String a = String.format("%.2f", valor);
            a = a.replaceAll(",",".");

            sql = sql.replaceFirst("%", numero);
            sql = sql.replaceFirst("%", a);
            sql = sql.replaceFirst("%", idPago);

            st = conn.createStatement();
            st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void insertImpuestos(String idPago) {
        Connection conn = conectar.getConnection();

        for (int i = 0; i < Impuesto_Id.size(); i++) {
            String sql = "insert into impuestos_pago (valor,idTipoimpuesto, idpago) values (%,%,%)";

            Statement st = null;

            try {
                String a = String.format("%.2f", Impuesto_Valor.get(i));
                a = a.replaceAll(",",".");

                sql = sql.replaceFirst("%", a);
                sql = sql.replaceFirst("%", Integer.toString(Impuesto_Id.get(i)));
                sql = sql.replaceFirst("%", idPago);

                st = conn.createStatement();
                st.executeQuery(sql);
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}