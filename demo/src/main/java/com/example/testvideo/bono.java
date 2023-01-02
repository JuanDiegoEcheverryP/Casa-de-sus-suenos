package com.example.testvideo;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class bono {

    private  int idBono;
    private  float valorBono;

    public bono(int idBono, float valorBono) {
        this.idBono = idBono;
        this.valorBono = valorBono;
    }

    //Getetrs y setters
    public int getIdBono() {
        return idBono;
    }

    public void setIdBono(int idBono) {
        this.idBono = idBono;
    }

    public float getValorBono() {
        return valorBono;
    }

    public void setValorBono(float valorBono) {
        this.valorBono = valorBono;
    }
}
