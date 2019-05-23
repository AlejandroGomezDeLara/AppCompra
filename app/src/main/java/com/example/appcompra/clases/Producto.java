package com.example.appcompra.clases;

import android.content.Context;

public class Producto {
    public int id;
    public String nombre;
    public String url;
    public boolean seleccionado;

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Producto(int id,String nombre,String url) {
        this.id=id;
        this.url=url;
        this.nombre = corregirNombre(nombre);
        this.seleccionado=false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String corregirNombre(String nombre){
        if(nombre.contains("ñ")){
            return nombre.replaceAll("ñ","niio");
        }else{
            return nombre;
        }
    }
}
