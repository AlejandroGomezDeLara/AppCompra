package com.example.appcompra.clases;

import java.util.ArrayList;
import java.util.TreeMap;

public class Singleton {

    private ArrayList<Categoria> categorias;
    private TreeMap<Integer,ArrayList<Producto>> ultimosProductos;
    private int posicionSpinnerCategorias;
    private int posicionSpinnerListas;
    private ArrayList<Lista> listas;
    private TreeMap<Integer,ArrayList<Producto>> productosLista;
    private static Singleton instance;

    public Singleton () {
        categorias=new ArrayList<>();
        listas=new ArrayList<>();
        ultimosProductos=new TreeMap<>();
        productosLista=new TreeMap<>();
        posicionSpinnerCategorias =0;
        posicionSpinnerListas=0;
    }

    public static Singleton getInstance () {
        if (instance==null)
            instance = new Singleton();

        return instance;
    }

    public TreeMap<Integer, ArrayList<Producto>> getProductosLista() {
        return productosLista;
    }

    public void setProductosLista(TreeMap<Integer, ArrayList<Producto>> productosLista) {
        this.productosLista = productosLista;
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

    public boolean existenProdcuctosLista(){
        return productosLista!=null && !productosLista.isEmpty();
    }

    public void añadirNuevosProductos(int idCategoria,ArrayList<Producto> productos){
        ultimosProductos.put(idCategoria,productos);
    }

    public void setPosicionSpinnerCategorias(int pos){
        this.posicionSpinnerCategorias =pos;
    }

    public int getPosicionSpinnerCategorias() {
        return posicionSpinnerCategorias;
    }

    public void añadirNuevaLista(Lista lista) {
        listas.add(lista);
    }

    public int getPosicionSpinnerListas() {
        return posicionSpinnerListas;
    }

    public void setPosicionSpinnerListas(int posicionSpinnerListas) {
        this.posicionSpinnerListas = posicionSpinnerListas;
    }

    public TreeMap<Integer, ArrayList<Producto>> getUltimosProductos() {
        return ultimosProductos;
    }

    public void setUltimosProductos(TreeMap<Integer, ArrayList<Producto>> ultimosProductos) {
        this.ultimosProductos = ultimosProductos;
    }
}
