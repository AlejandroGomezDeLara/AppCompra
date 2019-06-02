package com.example.appcompra.utils;

public class CambiosUS {
    private String nombreUsuario;
    private String operacion;
    private String rol;
    private int idLista;

    public CambiosUS(String nombreUsuario, String operacion,String rol, int idLista) {
        this.nombreUsuario = nombreUsuario;
        this.rol=rol;
        this.operacion = operacion;
        this.idLista = idLista;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }
}
