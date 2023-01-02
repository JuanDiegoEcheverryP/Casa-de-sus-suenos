package com.example.testvideo;

import javafx.beans.property.*;

public class Propiedad {

    private  final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty direccion = new SimpleStringProperty();
    private final StringProperty agencia = new SimpleStringProperty();
    private final StringProperty fecha = new SimpleStringProperty();
    private final IntegerProperty habitaciones = new SimpleIntegerProperty();
    private final FloatProperty VEI = new SimpleFloatProperty();

    private final StringProperty tipo = new SimpleStringProperty();

    private final StringProperty idestadoOcupado = new SimpleStringProperty();

    private final IntegerProperty idInquilino = new SimpleIntegerProperty();

    private final IntegerProperty idIVisita = new SimpleIntegerProperty();



    //Getetrs y setters
    public String getTipo() {
        return tipo.get();
    }


    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public int getIdIVisita() {
        return idIVisita.get();
    }

    public IntegerProperty idIVisitaProperty() {
        return idIVisita;
    }

    public void setIdIVisita(int idIVisita) {
        this.idIVisita.set(idIVisita);
    }

    public String getIdestadoOcupado() {
        return idestadoOcupado.get();
    }

    public StringProperty idestadoOcupadoProperty() {
        return idestadoOcupado;
    }

    public void setIdestadoOcupado(String idestadoOcupado) {
        this.idestadoOcupado.set(idestadoOcupado);
    }

    public int getIdInquilino() {
        return idInquilino.get();
    }

    public IntegerProperty idInquilinoProperty() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino.set(idInquilino);
    }

    public int getHabitaciones() {
        return habitaciones.get();
    }

    public IntegerProperty habitacionesProperty() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        this.habitaciones.set(habitaciones);
    }

    public float getVEI() {
        return VEI.get();
    }

    public FloatProperty VEIProperty() {
        return VEI;
    }

    public void setVEI(float VEI) {
        this.VEI.set(VEI);
    }

    public String getFecha() {
        return fecha.get();
    }

    public StringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDireccion() {
        return direccion.get();
    }

    public StringProperty direccionProperty() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public String getAgencia() {
        return agencia.get();
    }

    public StringProperty agenciaProperty() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia.set(agencia);
    }
}