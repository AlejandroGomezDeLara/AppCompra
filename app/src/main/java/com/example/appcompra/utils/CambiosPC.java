package com.example.appcompra.utils;

public class CambiosPC {
    private int idProducto;
    private String operacion;
    private int idLista;

    public CambiosPC(int idProducto, String operacion, int idLista) {
        this.idProducto = idProducto;
        this.operacion = operacion;
        this.idLista = idLista;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
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
