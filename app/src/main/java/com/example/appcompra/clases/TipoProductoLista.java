package com.example.appcompra.clases;

public class TipoProductoLista extends Producto {
    private String cantidad;
    private String receta;
    private String cadena;
    private boolean comprado;

    public TipoProductoLista(int id, String nombre, String cantidad, String receta, String cadena, boolean comprado, String url) {
        super(id, nombre, url);
        this.cantidad=cantidad;
        this.receta=receta;
        this.cadena=cadena;
        this.comprado=comprado;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getReceta() {
        return receta;
    }

    public void setReceta(String receta) {
        this.receta = receta;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}
