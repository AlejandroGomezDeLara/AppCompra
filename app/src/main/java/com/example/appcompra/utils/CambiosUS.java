package com.example.appcompra.utils;

public class CambiosUS {
    private String nombreUsuario;
    private String operacion;
    private int idLista;

    public CambiosUS(String nombreUsuario, String operacion, int idLista) {
        this.nombreUsuario = nombreUsuario;
        this.operacion = operacion;
        this.idLista = idLista;
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
