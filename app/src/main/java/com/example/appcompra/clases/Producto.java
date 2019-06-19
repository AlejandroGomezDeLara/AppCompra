package com.example.appcompra.clases;

import android.content.Context;

public class Producto implements Comparable{
    public int id;
    public String nombre;
    public String url;
    public boolean seleccionado;

    public Producto(int id, String nombre) {
        this.id=id;
        this.nombre=nombre;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Producto(int id,String nombre,String url) {
        this.id=id;
        this.url=url;
        this.nombre = nombre;
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


    @Override
    public int compareTo(Object o) {
        if(((Producto)o).getId() == this.getId()){
            return 0;
        }

        else return 1;
    }
}
