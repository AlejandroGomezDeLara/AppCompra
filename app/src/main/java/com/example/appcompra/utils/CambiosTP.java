package com.example.appcompra.utils;

public class CambiosTP {
    private int idProducto;
    private String operacion;
    private int idLista;
    private int unidades;

    public CambiosTP(int idProducto, String operacion, int idLista,int unidades) {
        this.idProducto = idProducto;
        this.operacion = operacion;
        this.idLista = idLista;
        this.unidades=unidades;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
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
