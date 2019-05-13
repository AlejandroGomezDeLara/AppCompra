package com.example.appcompra.clases;

import android.content.Context;
import android.content.res.Resources;

public class ProductoComercial extends Producto{
    private String marca;

    public ProductoComercial(int id,String nombre,String marca,String url,String categoria ) {
        super(id,nombre,categoria,url);
        this.marca=marca;
    }

}
