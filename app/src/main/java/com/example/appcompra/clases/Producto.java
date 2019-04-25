package com.example.appcompra.clases;

import android.content.Context;

public class Producto {
    public String nombre;
    public String categoria;
    public Context context;
    public int imagen;

    public Producto(String nombre, String categoria, Context context) {
        this.nombre = corregirNombre(nombre);
        this.categoria = categoria;
        this.context = context;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String corregirNombre(String nombre){
        if(nombre.contains("ñ")){
            return nombre.replaceAll("ñ","niio");
        }else{
            return nombre;
        }
    }
}
