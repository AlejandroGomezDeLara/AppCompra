package com.example.appcompra.clases;

import java.util.TreeSet;

public class ProductosListaConID {
    private int id;
    private TreeSet<ProductoLista> productoLista;

    public ProductosListaConID(int id, TreeSet<ProductoLista> productosConID) {
        this.id = id;
        this.productoLista = productosConID;
    }

    public int getId() {
        return id;
    }

    public void setIdLista(int id) {
        this.id = id;
    }

    public TreeSet<ProductoLista> getProductosListaConID() {
        return productoLista;
    }

    public void setProductosLista(TreeSet<ProductoLista> productosConID) {
        this.productoLista = productosConID;
    }
}
