package com.example.appcompra.clases;

import java.util.LinkedList;
import java.util.TreeSet;

public class Receta implements Comparable{
    private int id;
    private String nombre;
    private String descripcion;
    private String preparacion;
    private String url;
    private TreeSet<Producto> ingredientes;

    public Receta(int id, String nombre, String descripcion, String preparacion, String url) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.preparacion = preparacion;
        this.url = url;
    }

    public Receta(int id, String nombre, String descripcion, String preparacion, String url,TreeSet<Producto> ingredientes ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.preparacion = preparacion;
        this.url = url;
        this.ingredientes=ingredientes;
    }

    public Receta(int id, String nombre, String urlImagen) {
        this.id=id;
        this.nombre=nombre;
        this.url=urlImagen;
    }

    public TreeSet<Producto> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(TreeSet<Producto> ingredientes) {
        this.ingredientes = ingredientes;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Object o) {
        if(((Receta)o).getId() == this.getId()){
            return 0;
        }
        else return 1;
    }
}
