package com.example.testvideo;

import com.example.testvideo.negocio.cache;
import com.example.testvideo.negocio.verCuentaNegocio;

import java.io.IOException;
import java.sql.*;

public class DAO {
    public static String userExist(String userName, String password) {
        Connection conn = conectar.getConnection();
        String mensaje = "";

        String sql = "select EstadoCuenta.estado from Cuenta join EstadoCuenta on cuenta.idEstadoCuenta = EstadoCuenta.idEstadoCuenta where nombreusuario = '%' and contrasena = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", userName);
            sql = sql.replaceFirst("%", password);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();

            if(rs.getString(1).equals("activo"))
            {
                mensaje = "Sesion correcta";
            }
            else if(rs.getString(1).equals("inactivo"))
            {
                mensaje = "Esta cuenta se encuentra desactivada";
            }
            else
            {
                mensaje = "Login incorrecto";
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();

            if(userName.equals("") || password.equals(""))
            {
                mensaje = "Por favor, llene todos los campos";
            }
            else {
                mensaje = "No se pudo iniciar sesion, pruebe nuevamente";
            }
        }
        return mensaje;
    }

    public static int validarUser(String userName, String password) {
        Connection conn = conectar.getConnection();

        String sql = "select nombreusuario, idusuario\n" +
                "from cuenta\n" +
                "where nombreusuario = '%' and contrasena = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", userName);
            sql = sql.replaceFirst("%", password);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            String usuarioNombre = cache.getUserName();

            try {

                if (rs.getString(1).equals(usuarioNombre)) {
                    int a = Integer.parseInt(rs.getString(2));
                    return a;
                }
            }
            catch (Exception e) {
                return 0;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static String isLogged(String usuario) {
        Connection conn = conectar.getConnection();
        String mensaje = "";

        String sql = "select tipousocuenta.tipo\n" +
                "from cuenta, tipousocuenta\n" +
                "where cuenta.nombreusuario = '%' and cuenta.idusando = tipousocuenta.idestadouso";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", usuario);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();

            if(rs.getString(1).equals("Inactivo"))
            {
                mensaje = "Sesion iniciada";

                String sql1 = "update cuenta\n" +
                        "set cuenta.idusando = 2\n" +
                        "where cuenta.nombreusuario = '%'";

                sql1 = sql1.replaceFirst("%", usuario);

                st = conn.createStatement();
                st.executeQuery(sql1);
                System.out.println("esta inactivo");
            }
            else if(rs.getString(1).equals("Activo"))
            {
                mensaje = "Alguien esta usando su cuenta ahora mismo";
            }
            else
            {
                mensaje = "Login incorrecto";
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();

            mensaje = "No se pudo iniciar sesion, pruebe nuevamente";
        }
        return mensaje;
    }

    public static void deslogear() throws IOException {
        String id = cache.getUserName();

        Connection conn = conectar.getConnection();
        String mensaje = "";

        String sql = "update cuenta\n" +
                "set cuenta.idusando = 1\n" +
                "where cuenta.nombreusuario = '%'";

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

            mensaje = "No se pudo iniciar sesion, pruebe nuevamente";
        }
    }

    public static ResultSet getCuenta(String username, String password) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select * from Cuenta where nombreusuario = '%' and contrasena = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", username);
            sql = sql.replaceFirst("%", password);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs.next();
        return rs;
    }

    public static boolean updateCuenta(String username, String nombre, String apellido, String documento, String correo) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "UPDATE Cuenta SET nombre='%', apellido='%', numerodocumento='%', correo='%' WHERE nombreusuario= '%'";
        String sql2 = "Commit";

        boolean validar = true;
        Statement st = null;
        ResultSet rs = null;

        if (verCuentaNegocio.validarCorreo(correo))
        {
            try {
                sql = sql.replaceFirst("%", nombre);
                sql = sql.replaceFirst("%", apellido);
                sql = sql.replaceFirst("%", documento);
                sql = sql.replaceFirst("%", correo);
                sql = sql.replaceFirst("%", username);

                st = conn.createStatement();
                st.executeQuery(sql);
                st.executeQuery(sql2);

                validar=true;
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }else
        {
            validar=false;
        }
        return validar;
    }

    public static void updateEstadoCuenta(String username) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "UPDATE Cuenta SET idestadocuenta = '%' WHERE nombreusuario= '%'";
        String sql2 = "Commit";

        Statement st = null;
        ResultSet rs = null;
        try {
            sql = sql.replaceFirst("%", "2");
            sql = sql.replaceFirst("%", username);

            st = conn.createStatement();
            st.executeQuery(sql);
            st.executeQuery(sql2);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
    public static ResultSet getContra(String username) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select cuenta.contrasena from Cuenta where nombreusuario = '%' ";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", username);


            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs.next();
        return rs;
    }

    public static void updateContra(String contrasena, String nombreusuario) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "UPDATE Cuenta SET contrasena='%' WHERE nombreusuario= '%'";
        String sql2 = "Commit";

        Statement st = null;
        ResultSet rs = null;
        try {
            sql = sql.replaceFirst("%", contrasena);
            sql = sql.replaceFirst("%", nombreusuario);

            st = conn.createStatement();
            st.executeQuery(sql);
            st.executeQuery(sql2);

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getPropiedadesDueno(String userName, String password) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select * from Cuenta where nombreusuario = '%' and contrasena = '%'";

        Statement st = null;
        ResultSet rs = null;

        try {
            sql = sql.replaceFirst("%", userName);
            sql = sql.replaceFirst("%", password);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rs.next();

        String idusuario = rs.getString(1);

        String sql2 = "select * from propiedad where idusuariodueno = '%'";

        Statement st2 = null;
        ResultSet rs2 = null;

        try {
            sql2 = sql2.replaceFirst("%", idusuario);

            st2 = conn.createStatement();
            rs2 = st.executeQuery(sql2);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        String a = "";
        while (rs2.next())
        {
            a += rs2.getString(2);
        }
        return a;
    }

    public static ResultSet getPropiedad(String id) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select * from propiedad where idpropiedad = '%'";

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

        String idusuario = rs.getString(2);

        return rs;
    }

    public static String getValueFromResultset(ResultSet rs, int columna) throws SQLException {
        return rs.getString(columna);
    }

    public static void setValueFromResultset( ResultSet rs, int columna, String x) throws SQLException {
        rs.updateString(columna,x);
    }
    public static int propiedadIsBlocked(String id) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select tipousopropiedad.tipo, propiedad.idestadoocupacion, propiedad.idestadoeliminado\n" +
                "from propiedad, tipousopropiedad\n" +
                "where propiedad.idpropiedad = % and propiedad.tipouso = tipousopropiedad.idestadouso";

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

        String estado = rs.getString(1);
        String estado1 = rs.getString(2);
        String estado2 = rs.getString(3);

        if(estado.equals("Activo")) {
            return 1;
        }
        else {
            if (rs.getString(2).equals("2")) {
                return 2;
            }
            else {
                if (rs.getString(3).equals("2")) {
                    return 3;
                }
                else {
                    return 4;
                }
            }
        }
    }

    public static void bloquearPropiedad(String id) {
        Connection conn = conectar.getConnection();
        String sql = "update propiedad \n" +
                "set propiedad.tipouso = 2\n" +
                "where propiedad.idpropiedad = %";

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
    }

    public static void desbloquearPropiedad() throws IOException {
        String id = cache.getPropiedad();

        Connection conn = conectar.getConnection();
        String sql = "update propiedad \n" +
                "set propiedad.tipouso = 1\n" +
                "where propiedad.idpropiedad = %";

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
    }

    public static void ocuparPropiedad() throws IOException {
        String id = cache.getPropiedad();

        Connection conn = conectar.getConnection();
        String sql = "update propiedad \n" +
                "set propiedad.IDESTADOOCUPACION = 2\n" +
                "where propiedad.idpropiedad = %";

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
    }

    public static boolean eliminarPropiedad(String id) throws SQLException {
        Connection conn = conectar.getConnection();
        String sql = "select idestadoocupacion\n" +
                "from propiedad\n" +
                "where propiedad.idpropiedad = %";

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

        if (rs.getString(1).equals("1")) {
            String query = "update propiedad set idestadoeliminado  =2 where idpropiedad = %";

            query = query.replaceFirst("%", id);

            conn = conectar.getConnection();
            conn.createStatement().executeQuery(query);
            return true;
        }
        else {
            return false;
        }
    }
}
