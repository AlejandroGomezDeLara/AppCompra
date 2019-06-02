package com.example.appcompra.clases;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class Lista {
    private int id;
    private String titulo;
    private ArrayList<Producto> productos;
    private ArrayList<Usuario> usuarios;
    private String rol;

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public Lista(int id,String titulo,String rol) {
        this.titulo = titulo;
        this.id=id;
        this.rol=rol;
        usuarios=new ArrayList<>();
        productos=new ArrayList<>();
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroUsuarios() {
        return usuarios.size();
    }


    public void borrarUsuario(Usuario u) {
        if(usuarios.contains(u))
            usuarios.remove(u);
    }
}
