package com.example.appcompra.clases;

import android.content.Context;

public class Producto {
    public String nombre;
    public String categoria;
    public Context context;
    public String url;
    public boolean seleccionado;

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Producto(String nombre, String categoria,Context context) {
        this.nombre = corregirNombre(nombre);
        this.categoria = categoria;
        this.context = context;
        this.seleccionado=false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
