package com.example.appcompra.clases;

import android.content.Context;
import android.content.res.Resources;

public class TipoProducto extends Producto {

    public TipoProducto(String nombre,String categoria, Context context) {
        super(nombre,categoria,context);
        this.imagen=setImagen(nombre);
    }

    public int setImagen(String nombre){
        Resources resources = context.getResources();
        return resources.getIdentifier(nombre.replaceAll(" ","_"), "drawable",
                context.getPackageName());
    }


}
