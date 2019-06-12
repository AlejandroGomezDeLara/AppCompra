package com.example.appcompra.clases;

import java.util.TreeSet;

public class ProductosConID {
    private int id;
    private TreeSet<Producto> productosConID;

    public ProductosConID(int id, TreeSet<Producto> productosConID) {
        this.id = id;
        this.productosConID = productosConID;
    }

    public int getId() {
        return id;
    }

    public void setIdCategoria(int id) {
        this.id = id;
    }

    public TreeSet<Producto> getProductosConID() {
        return productosConID;
    }

    public void setProductosCategoria(TreeSet<Producto> productosConID) {
        this.productosConID = productosConID;
    }
}
