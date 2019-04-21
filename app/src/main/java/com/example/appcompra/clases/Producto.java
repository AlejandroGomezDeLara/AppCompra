package com.example.appcompra.clases;

public class Producto {
    private String nombre;
    private String marca;
    private int imagen;
    private String categoria;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Producto(String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen= imagen;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }
}
