package com.example.appcompra.clases;

import android.content.Context;
import android.content.res.Resources;

public class TipoProducto extends Producto {

    public TipoProducto(String nombre,String categoria, Context context) {
        super(nombre,categoria,context);
        this.imagen=setImagen(this.nombre);
    }

    public int setImagen(String nombre){
        System.out.println(nombre);
        Resources resources = context.getResources();
        if(resources.getIdentifier(nombre.toLowerCase().replaceAll(" ","_"), "drawable",
                context.getPackageName())>0){
            return resources.getIdentifier(nombre.toLowerCase().replaceAll(" ","_"), "drawable",
                    context.getPackageName());
        }else{
            if (nombre.contains(" ")){
                return resources.getIdentifier(nombre.substring(0,nombre.indexOf(" ")).toLowerCase().replaceAll(" ","_"), "drawable",
                        context.getPackageName());
            }else{
                return 0;
            }
        }
    }
}
