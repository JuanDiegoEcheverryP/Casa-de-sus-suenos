package com.example.testvideo.negocio;

public class cache {
    public static String userName = "";
    public static String contrasena = "";
    public static String comentarios = "";
    public static String propiedad ="";

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        cache.userName = userName;
    }

    public static String getContrasena() {
        return contrasena;
    }

    public static void setContrasena(String contrasena) {
        cache.contrasena = contrasena;
    }

    public static String getComentarios() {
        return comentarios;
    }

    public static void setComentarios(String comentarios) {
        cache.comentarios = comentarios;
    }

    public static String getPropiedad() {
        return propiedad;
    }

    public static void setPropiedad(String propiedad) {
        cache.propiedad = propiedad;
    }
}
