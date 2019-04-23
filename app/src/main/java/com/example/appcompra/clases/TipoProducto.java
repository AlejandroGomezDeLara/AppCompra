package com.example.appcompra.clases;

import android.content.Context;
import android.content.res.Resources;

public class TipoProducto {
    private String nombre;
    private int imagen;
    private String categoria;
    private Context context;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public TipoProducto(String nombre, Context context) {
        this.context=context;
        this.nombre = nombre;
        this.imagen=setImagen(nombre);
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public int setImagen(String nombre){
        Resources resources = context.getResources();
        return resources.getIdentifier(nombre.replaceAll(" ","_"), "drawable",
                context.getPackageName());
    }


}
