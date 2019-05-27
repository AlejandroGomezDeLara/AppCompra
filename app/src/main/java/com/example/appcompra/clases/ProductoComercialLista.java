package com.example.appcompra.clases;

public class ProductoComercialLista extends TipoProductoLista {
    private String marca;
    private String cantidad;
    public ProductoComercialLista(int id, String nombre, String unidades, String receta, String cadena, boolean comprado, String marca,String url,String cantidad) {
        super(id, nombre, unidades, receta, cadena, comprado, url);
        this.marca=marca;
        this.cantidad=cantidad;
    }
}
