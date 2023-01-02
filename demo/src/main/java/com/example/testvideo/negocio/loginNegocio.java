package com.example.testvideo.negocio;

import com.example.testvideo.DAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class loginNegocio {
    public static void escribirCache(String usuario, String contrasena) throws IOException {
        //Escribe el login en cache
        cache.userName = usuario;
        cache.contrasena = contrasena;
    }
}
