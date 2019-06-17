package com.example.appcompra.clases;

import java.util.TreeSet;

public class RecetasConID {
    private int id;
    private TreeSet<Receta> recetasConID;

    public RecetasConID(int id, TreeSet<Receta> recetasConID) {
        this.id = id;
        this.recetasConID = recetasConID;
    }

    public int getId() {
        return id;
    }

    public void setIdCategoria(int id) {
        this.id = id;
    }

    public TreeSet<Receta> getRecetasConID() {
        return recetasConID;
    }

    public void setRecetasCategoria(TreeSet<Receta> recetasConID) {
        this.recetasConID = recetasConID;
    }
}
