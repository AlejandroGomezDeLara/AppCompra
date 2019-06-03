package com.example.appcompra.clases;

public class ProductoLista extends Producto {
    private int unidades;
    private String receta;
    private String cadena;
    private String marca;
    private String cantidad;
    private boolean comprado;

    public ProductoLista(int id, String nombre, int unidades, String receta, String cadena, boolean comprado, String url,String marca,String cantidad) {
        super(id, nombre, url);
        this.unidades =unidades;
        this.receta=receta;
        this.cadena=cadena;
        this.comprado=comprado;
        this.marca=marca;
        this.cantidad=cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
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

    public void sumarUnidades() {
        this.unidades+=1;
    }
}
