package com.example.appcompra.clases;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.utils.Cambios;
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
    private TreeSet<Categoria> categoriasRecetas;
    private PriorityQueue<Peticion> peticionesEnviar;
    private PriorityQueue<String> respuestasServidor;
    private LinkedList<Notificacion> notificaciones;
    private MainActivity.PeticionesThread hiloComunicacion;
    private TreeMap<Integer, TreeSet<Producto>> productosCategoria;
    private TreeMap<Integer, TreeSet<ProductoLista>> productosLista;
    private TreeSet<Lista> listas;
    private TreeSet<ProductoLista> despensa;
    private TreeMap<Integer, TreeSet<Receta>> recetas;
    private int posicionSpinnerCategorias;
    private int posicionSpinnerCategoriasRecetas;
    private int posicionSpinnerListas;
    private int idListaSeleccionada;
    private int idCategoriaSelecionada;
    private int idCategoriaRecetaSeleccionada;
    private int idRecetaSeleccionada;
    private Receta recetaActual;
    private Oferta ofertaActual;
    private ArrayList<String> roles;
    private static Singleton instance;
    private ArrayList<Oferta> ofertas;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public static Singleton getInstance () {
        if (instance==null)
            instance = new Singleton();

        return instance;
    }

    public Singleton () {
        recetas=new TreeMap<>();
        categorias=new TreeSet<>();
        categoriasRecetas=new TreeSet<>();
        listas=new TreeSet<>();
        recetaActual=null;
        ofertas=new ArrayList<>();
        productosCategoria=new TreeMap<>();
        productosLista=new TreeMap<>();
        notificaciones=new LinkedList<>();
        posicionSpinnerCategorias =0;
        posicionSpinnerCategoriasRecetas =0;
        idListaSeleccionada=0;
        idRecetaSeleccionada=0;
        idCategoriaSelecionada=0;
        idCategoriaRecetaSeleccionada=0;
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
        String nombreLista="";
        for(Lista l:listas){
            if(l.getId()==idLista){
                nombreLista=l.getTitulo();
            }
        }
        for(ProductoLista pro:p){
            Cambios.getInstance().añadirCambioTipoProducto(pro.getId(),"add",idLista,pro.getUnidades(),pro.getCadena(),pro.getReceta(),nombreLista);
        }
    }

    public synchronized void añadirProductosListaRecetas(int idLista, TreeSet<ProductoLista> p){
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
        String nombreLista="";
        for(Lista l:listas){
            if(l.getId()==idLista){
                nombreLista=l.getTitulo();
            }
        }
        for(ProductoLista pro:p){
            Cambios.getInstance().añadirCambioTipoProducto(pro.getId(),"add",idLista,pro.getUnidades(),pro.getCadena(),pro.getReceta(),nombreLista);
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


    public void limpiarProductosLista() {
        this.productosLista.clear();
    }

    public TreeSet<ProductoLista> getProductosListaSeleccionados(){
        TreeSet<ProductoLista> seleccionados=new TreeSet<>();
        if(Singleton.getInstance().getIdListaSeleccionada()!=0){
            if(productosLista.containsKey(Singleton.getInstance().getIdListaSeleccionada()))
                for(ProductoLista p:getProductosListaLista(idListaSeleccionada)){
                    if(p.isSeleccionado())seleccionados.add(p);
                }
        }else{
            for(ProductoLista p:despensa){
                if(p.isSeleccionado())seleccionados.add(p);
            }
        }

        return seleccionados;
    }

    public boolean hayProductosListaSeleccionados(){
        boolean cierto=false;
        if(Singleton.getInstance().getIdListaSeleccionada()!=0) {
            if(productosLista.containsKey(Singleton.getInstance().getIdListaSeleccionada()))
                for(ProductoLista p:getProductosListaLista(Singleton.getInstance().getIdListaSeleccionada())){
                    if(p.isSeleccionado())cierto=true;
                }
        }else{
            for(ProductoLista p:despensa){
                if(p.isSeleccionado())cierto=true;
            }
        }

        return cierto;
    }

    public void borrarProductosSeleccionados() {
        if(idListaSeleccionada!=0){
            if (productosLista.containsKey(Singleton.getInstance().getIdListaSeleccionada())){
                Iterator iterator=getProductosListaLista(Singleton.getInstance().getIdListaSeleccionada()).iterator();
                while (iterator.hasNext()){
                    ProductoLista pro=(ProductoLista)iterator.next();
                    if(pro.isSeleccionado())iterator.remove();
                }
            }
        }else{
            Iterator iterator=despensa.iterator();
            while(iterator.hasNext()){
                ProductoLista pro=(ProductoLista)iterator.next();
                if(pro.isSeleccionado())iterator.remove();
            }
        }

    }

    public boolean existenRecetas(){
        return recetas.size()> 0;
    }

    public boolean existenCategoriasRecetas(){
        return this.categoriasRecetas.size()> 0;
    }
    public TreeSet<Categoria> getCategoriasRecetas() {
        return categoriasRecetas;
    }

    public void setCategoriasRecetas(TreeSet<Categoria> categoriasRecetas) {
        this.categoriasRecetas = categoriasRecetas;
    }

    public int getIdCategoriaRecetaSeleccionada() {
        return idCategoriaRecetaSeleccionada;
    }

    public void setIdCategoriaRecetaSeleccionada(int idCategoriaRecetaSeleccionada) {
        this.idCategoriaRecetaSeleccionada = idCategoriaRecetaSeleccionada;
    }

    public int getPosicionSpinnerCategoriasRecetas() {
        return posicionSpinnerCategoriasRecetas;
    }

    public void setPosicionSpinnerCategoriasRecetas(int posicionSpinnerCategoriasRecetas) {
        this.posicionSpinnerCategoriasRecetas = posicionSpinnerCategoriasRecetas;
    }

    public boolean existenRecetasCategoria() {
        return recetas.containsKey(idCategoriaRecetaSeleccionada);
    }

    public boolean existenRecetaCategoria(){
        return recetas.containsKey(idCategoriaRecetaSeleccionada);
    }


    public void añadirNuevasRecetasCategoriaSeleccionada(int idCategoria,TreeSet<Receta> recetas){
        Log.e("Xd","añadido "+recetas.size()+" productos a la categoria "+idCategoria);
        this.recetas.put(idCategoria,recetas);}

    public TreeSet<Receta> getRecetasCategoriaSelecionada() {
        return this.recetas.get(idCategoriaRecetaSeleccionada);
    }

    public void limpiarProductosSeleccionados() {
        if(idListaSeleccionada==0){
            for(ProductoLista p:despensa){
                p.setSeleccionado(false);
            }
        }else{
            for(ProductoLista p: getProductosListaLista(idListaSeleccionada)){
                p.setSeleccionado(false);
            }
        }
    }

    public int getIdRecetaSeleccionada() {
        return idRecetaSeleccionada;
    }

    public void setIdRecetaSeleccionada(int idRecetaSeleccionada) {
        this.idRecetaSeleccionada = idRecetaSeleccionada;
    }

    public Receta getRecetaActual() {
        return recetaActual;
    }

    public void setRecetaActual(Receta receta) {
        this.recetaActual=receta;
    }

    public Oferta getOfertaActual() {
        return ofertaActual;
    }

    public void setOfertaActual(Oferta ofertaActual) {
        this.ofertaActual = ofertaActual;
    }

    public ArrayList<Oferta> getOfertas() {
        return ofertas;
    }

    public void setOfertas(ArrayList<Oferta> ofertas) {
        this.ofertas = ofertas;
    }
}
