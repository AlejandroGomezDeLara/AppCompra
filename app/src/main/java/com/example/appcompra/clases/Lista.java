package com.example.appcompra.clases;

import java.util.LinkedList;

public class Lista {
    private String titulo;
    private int imagenFondo;
    private LinkedList<Usuario> usuarios;
    private LinkedList<Producto> productos;

    public LinkedList<Usuario> getUsuarios() {
        return usuarios;
    }

    public LinkedList<Producto> getProductos() {
        return productos;
    }

    public Lista(String titulo, int imagenFondo) {
        this.titulo = titulo;
        this.imagenFondo = imagenFondo;
        usuarios=new LinkedList<>();
        productos=new LinkedList<>();
    }

    public void añadirUsuario(Usuario u){
        usuarios.add(u);
    }
    public void añadirProducto(Producto p){
        productos.add(p);
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getImagenFondo() {
        return imagenFondo;
    }

    public void setImagenFondo(int imagenFondo) {
        this.imagenFondo = imagenFondo;
    }

    public int getNumeroUsuarios() {
        return usuarios.size();
    }

    public void setUsuarios(LinkedList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void setProductos(LinkedList<Producto> productos) {
        this.productos = productos;
    }
}
