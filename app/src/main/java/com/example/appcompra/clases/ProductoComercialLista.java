package com.example.appcompra.clases;

public class ProductoComercialLista extends TipoProductoLista {
    private String marca;

    public ProductoComercialLista(int id, String nombre, int cantidad, String receta, String cadena, boolean comprado, String marca,String url) {
        super(id, nombre, cantidad, receta, cadena, comprado, url);
        this.marca=marca;
    }
}
