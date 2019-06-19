package com.example.appcompra.utils;

public class Notificacion {
    private String autor;
    private String tipoNotificacion;
    private String operacion;
    private String rol;
    private String nombreLista;

    public Notificacion() {
    }

    public Notificacion(String autor, String tipoNotificacion, String operacion) {
        this.autor = autor;
        this.tipoNotificacion = tipoNotificacion;
        this.operacion = operacion;
    }

    public Notificacion(String autor, String tipoNotificacion, String operacion, String rol) {
        this.autor = autor;
        this.tipoNotificacion = tipoNotificacion;
        this.operacion = operacion;
        this.rol = rol;
    }

    public Notificacion(String autor, String tipoNotificacion, String operacion, String rol,String nombreLista) {
        this.autor = autor;
        this.tipoNotificacion = tipoNotificacion;
        this.operacion = operacion;
        this.rol = rol;
        this.nombreLista=nombreLista;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String stringNotificacion() {
        return "autor: "+autor+" tipo de notificacion "+tipoNotificacion+" operacion "+operacion+" rol "+rol+" lista "+nombreLista;
    }
}
