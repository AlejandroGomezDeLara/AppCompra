package com.example.appcompra.clases;

import android.content.Context;
import android.content.res.Resources;

public class ProductoComercial extends Producto{
    private String marca;

    public ProductoComercial(int id,String nombre,String marca,String url) {
        super(id,nombre,url);
        this.marca=marca;
    }

}
