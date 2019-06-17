package com.example.appcompra.clases;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

public class TipoProducto extends Producto {

    public TipoProducto(int id,String nombre,String url) {
        super(id,nombre,url);
    }

    public TipoProducto(int id, String nombre) {
        super(id,nombre);
    }
}
