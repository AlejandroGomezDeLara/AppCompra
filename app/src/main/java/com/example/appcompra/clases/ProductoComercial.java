package com.example.appcompra.clases;

import android.content.Context;
import android.content.res.Resources;

public class ProductoComercial extends Producto{
    private String marca;

    public ProductoComercial(String nombre,String marca,String url,String categoria,Context context ) {
        super(nombre,categoria,context);
        this.marca=marca;
        this.url=url;
    }

}
