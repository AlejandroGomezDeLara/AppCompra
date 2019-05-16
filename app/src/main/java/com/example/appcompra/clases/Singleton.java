package com.example.appcompra.clases;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private ArrayList<Producto> listaProductos;
    private ArrayList<Categoria> categorias;

    private static Singleton instance;

    public Singleton () {
        listaProductos = new ArrayList();
        categorias=new ArrayList<>();
    }

    public static Singleton getInstance () {
        if (instance==null)
            instance = new Singleton();

        return instance;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public boolean existenProductos(){
        return listaProductos!=null && !listaProductos.isEmpty();
    }

    public boolean existenCategorias() {
        return categorias!=null && !categorias.isEmpty();
    }
}
