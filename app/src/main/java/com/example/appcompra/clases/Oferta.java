package com.example.appcompra.clases;

public class Oferta {
    private int id;
    private String nombre;
    private String url;
    private String descripcion;
    private String cadena;

    public Oferta(int id, String nombre, String cadena,String descripcion, String url) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.descripcion = descripcion;
        this.cadena=cadena;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
