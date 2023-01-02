package com.example.testvideo;

public class servicio {

    private String name;
    private int cantidad;

    public servicio(String name, int cantidad) {
        this.name = name;
        this.cantidad = cantidad;
    }

    //Getetrs y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
