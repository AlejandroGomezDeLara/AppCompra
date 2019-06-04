package com.example.appcompra.clases;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Singleton {

    private ArrayList<Categoria> categorias;
    private TreeMap<Integer,ArrayList<Producto>> ultimosProductos;
    private TreeMap<Integer, ArrayList<ProductoLista>> productosLista;
    private ArrayList<Producto> despensa;
    private int posicionSpinnerCategorias;
    private int posicionSpinnerListas;
    private int idListaSeleccionada;
    private ArrayList<String> roles;

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }
    public void addRol(String rol){
        roles.add(rol);
    }

    public int getIdListaSeleccionada() {
        return idListaSeleccionada;
    }

    public void setIdListaSeleccionada(int idListaSeleccionada) {
        this.idListaSeleccionada = idListaSeleccionada;
    }

    public void deseleccionarProductos(){
        for (Map.Entry<Integer, ArrayList<Producto>> entry : ultimosProductos.entrySet()) {
            for (Producto p: entry.getValue()) {
                p.setSeleccionado(false);
            }
        }
    }
    public int getPosicionSpinnerListas() {
        return posicionSpinnerListas;
    }

    public void setPosicionSpinnerListas(int posicionSpinnerListas) {
        this.posicionSpinnerListas = posicionSpinnerListas;
    }

    private ArrayList<Lista> listas;
    private static Singleton instance;

    public Singleton () {
        categorias=new ArrayList<>();
        listas=new ArrayList<>();
        ultimosProductos=new TreeMap<>();
        productosLista=new TreeMap<>();
        posicionSpinnerCategorias =0;
        idListaSeleccionada=0;
        despensa=new ArrayList<>();
        roles=new ArrayList<>();
        roles.add("Ninguno");
        roles.add("Administrador");
        roles.add("Participante");
        roles.add("Espectador");
    }

    public static Singleton getInstance () {
        if (instance==null)
            instance = new Singleton();

        return instance;
    }

    public TreeMap<Integer, ArrayList<ProductoLista>> getProductosLista() {
        return productosLista;
    }

    public void setProductosLista(TreeMap<Integer, ArrayList<ProductoLista>> productosLista) {
        this.productosLista = productosLista;
    }

    public ArrayList<Lista> getListas() {
        return listas;
    }

    public void setListas(ArrayList<Lista> listas) {
        this.listas = listas;
    }

    public void añadirProductosLista(int idLista, LinkedList<ProductoLista> p){
        if(productosLista.containsKey(idLista)){
            ArrayList<ProductoLista> productos=productosLista.get(idLista);
            for (int i=0;i<p.size();i++){
                for (int j=0;j<productos.size();j++){
                    if(p.get(i).getId()==productos.get(j).getId()){
                        productos.get(j).sumarUnidades(p.get(i).getUnidades());
                        p.remove(p.get(i));
                    }
                }

            }

            productos.addAll(p);
        }
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

    public boolean existenDespensa(){
        return despensa!=null && !despensa.isEmpty();
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

    public TreeMap<Integer, ArrayList<Producto>> getUltimosProductos() {
        return ultimosProductos;
    }

    public void setUltimosProductos(TreeMap<Integer, ArrayList<Producto>> ultimosProductos) {
        this.ultimosProductos = ultimosProductos;
    }

    public ArrayList<Producto> getDespensa() {
        return despensa;
    }

    public void setDespensa(ArrayList<Producto> despensa) {
        this.despensa = despensa;
    }

    public void borrarLista(Lista l) {
        listas.remove(l);
    }
}
