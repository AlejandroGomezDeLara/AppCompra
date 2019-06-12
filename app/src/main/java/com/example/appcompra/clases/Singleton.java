package com.example.appcompra.clases;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.utils.Peticion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

public class Singleton {

    private ArrayList<Categoria> categorias;

    private PriorityQueue<Peticion> peticionesEnviar;

    private PriorityQueue<String> respuestasServidor;

    private MainActivity.PeticionesThread hiloComunicacion;

    private TreeMap<Integer, TreeSet<Producto>> productosCategoria;

    private TreeMap<Integer, TreeSet<ProductoLista>> productosLista;

    private TreeSet<Lista> listas;

    private ArrayList<String> caracteresPeticionesDirectas;

    private TreeSet<Producto> despensa;

    private int posicionSpinnerCategorias;

    private int posicionSpinnerListas;

    private int idListaSeleccionada;

    private ArrayList<String> roles;

    public ArrayList<String> getRoles() {
        return roles;
    }

    public MainActivity.PeticionesThread getHiloComunicacion() {
        return hiloComunicacion;
    }

    public void setHiloComunicacion(MainActivity.PeticionesThread hiloComunicacion) {
        this.hiloComunicacion = hiloComunicacion;
    }

    private static Singleton instance;

    public Singleton () {
        categorias=new ArrayList<>();
        listas=new TreeSet<>();
        productosCategoria=new TreeMap<>();
        productosLista=new TreeMap<>();
        posicionSpinnerCategorias =0;
        idListaSeleccionada=0;
        despensa=new TreeSet<>();
        peticionesEnviar=new PriorityQueue<>();
        respuestasServidor=new PriorityQueue<>();
        roles=new ArrayList<>();
        caracteresPeticionesDirectas=new ArrayList<>();
        roles.add("Ninguno");
        roles.add("Administrador");
        roles.add("Participante");
        roles.add("Espectador");
        caracteresPeticionesDirectas.add(Constants.COMPARTIR_LISTA_CORRECTA);
        caracteresPeticionesDirectas.add(Constants.CATEGORIAS_RESPUESTA_CORRECTA);
        caracteresPeticionesDirectas.add(Constants.PRODUCTOS_CATEGORIA_RESPUESTA_CORRECTA);
        caracteresPeticionesDirectas.add(Constants.PRODUCTOS_LISTA_CORRECTA);
        caracteresPeticionesDirectas.add(Constants.LISTAS_RESPUESTA_CORRECTA);
        caracteresPeticionesDirectas.add(Constants.PRODUCTOS_DESPENSA_CORRECTA);
        caracteresPeticionesDirectas.add(Constants.CREACION_NUEVA_LISTA_CORRECTA);



    }

    public synchronized ArrayList<String> getCaracteresPeticionesDirectas() {
        return caracteresPeticionesDirectas;
    }

    public static Singleton getInstance () {
        if (instance==null)
            instance = new Singleton();

        return instance;
    }

    public TreeMap<Integer, TreeSet<ProductoLista>> getProductosLista() {
        return productosLista;
    }

    public void setProductosLista(TreeMap<Integer, TreeSet<ProductoLista>> productosLista) {
        this.productosLista = productosLista;
    }

    public TreeSet<Lista> getListas() {
        return listas;
    }

    public void setListas(TreeSet<Lista> listas) {
        this.listas = listas;
    }

    public void añadirProductosLista(int idLista, LinkedList<ProductoLista> p){
        if(productosLista.containsKey(idLista)){
            productosLista.get(idLista).addAll(p);
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

    public void añadirNuevosProductosCategoria(int idCategoria,TreeSet<Producto> productos){
        productosCategoria.put(idCategoria,productos);
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

    public TreeMap<Integer, TreeSet<Producto>> getProductosCategoria() {
        return productosCategoria;
    }

    public void setProductosCategoria(TreeMap<Integer, TreeSet<Producto>> ultimosProductos) {
        this.productosCategoria = ultimosProductos;
    }


    public TreeSet<Producto> getDespensa() {
        return despensa;
    }

    public void setDespensa(TreeSet<Producto> despensa) {
        this.despensa = despensa;
    }

    public void borrarLista(Lista l) {
        listas.remove(l);
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

    public int getPosicionSpinnerListas() {
        return posicionSpinnerListas;
    }

    public void setPosicionSpinnerListas(int posicionSpinnerListas) {
        this.posicionSpinnerListas = posicionSpinnerListas;
    }

    public PriorityQueue<Peticion> getPeticionesEnviar() {
        return peticionesEnviar;
    }

    public void setPeticionesEnviar(PriorityQueue<Peticion> peticionesEnviar) {
        this.peticionesEnviar = peticionesEnviar;
    }

    public PriorityQueue<String> getRespuestasServidor() {
        return respuestasServidor;
    }

    public void setRespuestasServidor(PriorityQueue<String> respuestasServidor) {
        this.respuestasServidor = respuestasServidor;
    }

    public synchronized void enviarPeticion(Peticion peticion){
        peticionesEnviar.offer(peticion);
        hiloComunicacion.interrupt();
    }

    public synchronized String getPeticionMaxPrioridad(){
        return peticionesEnviar.peek().getStringPeticion();
    }

    public synchronized void añadirRespuestaServidor(String respuesta){
        respuestasServidor.offer(respuesta);
        peticionesEnviar.poll();
    }

    public synchronized String getRespuestaMaxPrioridad(){
        return respuestasServidor.peek();
    }

    public synchronized void peticionProcesada() {
        peticionesEnviar.poll();
    }



}
