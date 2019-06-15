package com.example.appcompra.clases;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.utils.Notificacion;
import com.example.appcompra.utils.Peticion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

public class Singleton {

    //Declaramos todas las colecciones

    private TreeSet<Categoria> categorias;
    private PriorityQueue<Peticion> peticionesEnviar;
    private PriorityQueue<String> respuestasServidor;
    private LinkedList<Notificacion> notificaciones;
    private MainActivity.PeticionesThread hiloComunicacion;
    private TreeMap<Integer, TreeSet<Producto>> productosCategoria;
    private TreeMap<Integer, TreeSet<ProductoLista>> productosLista;
    private TreeSet<Lista> listas;
    private TreeSet<ProductoLista> despensa;
    private int posicionSpinnerCategorias;
    private int posicionSpinnerListas;
    private int idListaSeleccionada;
    private int idCategoriaSelecionada;
    private ArrayList<String> roles;
    private static Singleton instance;

    public static Singleton getInstance () {
        if (instance==null)
            instance = new Singleton();

        return instance;
    }

    public Singleton () {
        categorias=new TreeSet<>();
        listas=new TreeSet<>();
        productosCategoria=new TreeMap<>();
        productosLista=new TreeMap<>();
        notificaciones=new LinkedList<>();
        posicionSpinnerCategorias =0;
        idListaSeleccionada=0;
        idCategoriaSelecionada=0;
        despensa=new TreeSet<>();
        peticionesEnviar=new PriorityQueue<>();
        respuestasServidor=new PriorityQueue<>();
        roles=new ArrayList<>();
        roles.add("Ninguno");
        roles.add("Administrador");
        roles.add("Participante");
        roles.add("Espectador");
    }

    //Setter getter y metodos synchronized para acceder a las colecciones
    public synchronized void añadirNotificacion(Notificacion n){this.notificaciones.add(n);}
    public synchronized void añadirProductosLista(int idLista, TreeSet<ProductoLista> p){
        if(productosLista.containsKey(idLista)){
            for(ProductoLista productoLista: getProductosListaLista(idLista)){
                for(ProductoLista productoLista1: p){
                    if(productoLista.getId()==productoLista1.getId())
                        productoLista.sumarUnidades(productoLista1.getUnidades());
                }
            }
            productosLista.get(idLista).addAll(p);
        }else{
            productosLista.put(idLista,p);
        }
    }
    public synchronized boolean existenProductosLista(){
        if(productosLista.containsKey(idListaSeleccionada)){
            if(productosLista.get(idListaSeleccionada).isEmpty())
                return false;
            else
                return true;
        }
        else return false;
    }
    public void setDespensa(TreeSet<ProductoLista> despensa) {
        for(ProductoLista productoLista: getDespensa()){
            for(ProductoLista productoLista1: despensa){
                if(productoLista.getId()==productoLista1.getId())
                    productoLista.sumarUnidades(productoLista1.getUnidades());
            }
        }
        this.despensa = despensa;
    }
    public void borrarLista(int id) {
        Iterator iterator;
        iterator = listas.iterator();
        while (iterator.hasNext()) {
            Lista l=(Lista)iterator.next();
            if(l.getId()==id)iterator.remove();
        }
    }
    public synchronized void enviarPeticion(Peticion peticion){
        boolean existePeticion=false;
        for(Peticion p: peticionesEnviar){
            if(p.getCodPeticion().equals(peticion.getCodPeticion()))
                existePeticion=true;
        }
        if(!existePeticion){
            peticionesEnviar.offer(peticion);
        }
        hiloComunicacion.interrupt();
    }
    public synchronized String getPeticionMaxPrioridad(){ return peticionesEnviar.poll().getStringPeticion(); }
    public synchronized void añadirRespuestaServidor(String respuesta){
        respuestasServidor.offer(respuesta);
        peticionesEnviar.remove();
    }
    public synchronized void deseleccionarProductos() {
        for (Map.Entry<Integer, TreeSet<Producto>> entry :productosCategoria.entrySet()) {
            for (Producto p: entry.getValue()) {
                if(p.isSeleccionado())
                    p.setSeleccionado(!p.isSeleccionado());
            }
        }
    }
    public synchronized void añadirProductosDespensa(TreeSet<ProductoLista> productosSeleccionados) {
        Iterator iterator=productosSeleccionados.iterator();
        for(ProductoLista productoLista: getDespensa()){
            while (iterator.hasNext()){
                ProductoLista pro=(ProductoLista)iterator.next();
                if(productoLista.getId()==pro.getId()){
                    productoLista.sumarUnidades(pro.getUnidades());
                    iterator.remove();
                }
            }
        }
        despensa.addAll(productosSeleccionados);
    }
    public synchronized TreeSet<ProductoLista> getDespensa() {
        return despensa;
    }
    public synchronized void setCategorias(TreeSet<Categoria> categorias) { this.categorias = categorias; }
    public synchronized boolean existenListas(){
        return listas!=null && !listas.isEmpty();
    }
    public synchronized boolean existenCategorias() { return categorias!=null && !categorias.isEmpty(); }
    public synchronized LinkedList<Notificacion> getNotificaciones() {return notificaciones;}
    public int getIdListaSeleccionada() {return idListaSeleccionada;}
    public void setIdListaSeleccionada(int idListaSeleccionada) {this.idListaSeleccionada = idListaSeleccionada;}
    public int getPosicionSpinnerListas() {return posicionSpinnerListas;}
    public void setPosicionSpinnerListas(int posicionSpinnerListas) {this.posicionSpinnerListas = posicionSpinnerListas;}
    public synchronized PriorityQueue<Peticion> getPeticionesEnviar() { return peticionesEnviar;}
    public synchronized void peticionProcesada() { respuestasServidor.poll();}
    public synchronized boolean existenProductosCategoriaSeleccionada() { return productosCategoria.containsKey(idCategoriaSelecionada);}
    public boolean existenDespensa(){ return despensa!=null && !despensa.isEmpty(); }
    public void añadirNuevosProductosCategoria(int idCategoria,TreeSet<Producto> productos){ productosCategoria.put(idCategoria,productos);}
    public void setPosicionSpinnerCategorias(int pos){this.posicionSpinnerCategorias =pos;}
    public int getPosicionSpinnerCategorias() {return posicionSpinnerCategorias;}
    public void añadirNuevaLista(Lista lista) {listas.add(lista);}
    public synchronized TreeMap<Integer, TreeSet<Producto>> getProductosCategoria() { return productosCategoria;}
    public synchronized TreeSet<Producto> getProductosCategoriaCategoria(int idCategoria) {return productosCategoria.get(idCategoria);}
    public ArrayList<String> getRoles() {
        return roles;
    }
    public void setHiloComunicacion(MainActivity.PeticionesThread hiloComunicacion) {this.hiloComunicacion = hiloComunicacion;}
    public synchronized int getIdCategoriaSelecionada() {
        return idCategoriaSelecionada;
    }
    public synchronized void setIdCategoriaSelecionada(int idCategoriaSelecionada) {this.idCategoriaSelecionada = idCategoriaSelecionada;}
    public synchronized TreeSet<ProductoLista> getProductosListaLista(int idLista) { return productosLista.get(idLista); }
    public synchronized TreeSet<Categoria> getCategorias() {
        return categorias;
    }
    public synchronized TreeSet<Lista> getListas() {
        return listas;
    }
    public synchronized void setListas(TreeSet<Lista> listas) {
        this.listas = listas;
    }
    public synchronized boolean existenNotificaciones(){return getNotificaciones().size()>0;}
    public synchronized void setNotificaciones(LinkedList<Notificacion> not) {this.notificaciones=not;}
    public synchronized String mostrarNotificaciones(){
        String cadena="";
        for(Notificacion n:notificaciones){
            cadena+=n.stringNotificacion();
        }
        return cadena;
    }



}
