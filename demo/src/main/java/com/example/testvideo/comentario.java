package com.example.testvideo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class comentario {

    private final IntegerProperty idComentario = new SimpleIntegerProperty();
    private final StringProperty Usuario = new SimpleStringProperty();
    private final StringProperty texto = new SimpleStringProperty();

    public int getIdComentario() {
        return idComentario.get();
    }

    //Getetrs y setters
    public IntegerProperty idComentarioProperty() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario.set(idComentario);
    }

    public String getUsuario() {
        return Usuario.get();
    }

    public StringProperty usuarioProperty() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        this.Usuario.set(usuario);
    }

    public String getTexto() {
        return texto.get();
    }

    public StringProperty textoProperty() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto.set(texto);
    }
}
