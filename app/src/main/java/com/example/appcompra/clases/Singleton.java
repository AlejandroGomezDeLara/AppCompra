package com.example.appcompra.clases;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Singleton {

    private ArrayList<Categoria> categorias;
    private TreeMap<Integer,ArrayList<Producto>> ultimosProductos;
    public TreeMap<Integer, ArrayList<Producto>> getUltimosProductos() {
        return ultimosProductos;
    }

    public void setUltimosProductos(TreeMap<Integer, ArrayList<Producto>> ultimosProductos) {
        this.ultimosProductos = ultimosProductos;
    }

    private ArrayList<Lista> listas;

    private static Singleton instance;

    public Singleton () {
        categorias=new ArrayList<>();
        listas=new ArrayList<>();
        ultimosProductos=new TreeMap<>();
    }

    public static Singleton getInstance () {
        if (instance==null)
            instance = new Singleton();

        return instance;
    }

    public ArrayList<Lista> getListas() {
        return listas;
    }

    public void setListas(ArrayList<Lista> listas) {
        this.listas = listas;
    }


    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    public boolean existenListas(){
        return listas!=null && !listas.isEmpty();
    }

    public boolean existenCategorias() {
        return categorias!=null && !categorias.isEmpty();
    }
}
