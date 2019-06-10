package com.example.appcompra.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appcompra.Constants;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoLista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

// idLista=0 si es despensa
// N;ID;{        tp:[
//                  {idProducto,remove(2),idlista},
//                  {idProducto,add(1),idlista},
//                  {idProducto,mark(3),idlista}
//                ],
//                pc:[
//                    {idProducto,remove(2),idlista},
////                  {idProducto,add(1),idlista},
////                  {idProducto,mark(3),idlista}
//                ],
//                lsb:[
//                  {idLista},
//                  ],
//                us:[
//                  {nombreUsuario,add,idLista},
//                  {nombreUsuario,remove,idLista}
//                  ]
//               }
public class Cambios {
    //1 add 2 delete 3 mark
    private LinkedList<CambiosTP> cambiosTipos;
    private LinkedList<CambiosPC> cambiosComerciales;
    private LinkedList<Integer> cambiosListas;
    private LinkedList<CambiosUS> cambiosUsuarios;

    private static Cambios instance;

    public static Cambios getInstance () {
        if (instance==null)
            instance = new Cambios();

        return instance;
    }
    public Cambios() {
        cambiosUsuarios=new LinkedList<>();
        cambiosTipos=new LinkedList<>();
        cambiosListas=new LinkedList<>();
        cambiosComerciales=new LinkedList<>();
        cambiosUsuarios=new LinkedList<>();
    }

    public  LinkedList<CambiosTP> getCambiosTipos() {
        return cambiosTipos;
    }

    public void setCambiosTipos(LinkedList<CambiosTP> cambiosTipos) {
        cambiosTipos = cambiosTipos;
    }

    public  LinkedList<CambiosPC> getCambiosComerciales() {
        return cambiosComerciales;
    }

    public void setCambiosComerciales(LinkedList<CambiosPC> cambiosComerciales) {
        cambiosComerciales = cambiosComerciales;
    }

    public  LinkedList<Integer> getCambiosListas() {
        return cambiosListas;
    }

    public  void setCambiosListas(LinkedList<Integer> cambiosListas) {
        cambiosListas = cambiosListas;
    }

    public LinkedList<CambiosUS> getCambiosUsuarios() {
        return cambiosUsuarios;
    }

    public void setCambiosUsuarios(LinkedList<CambiosUS> cambiosUsuarios) {
        cambiosUsuarios = cambiosUsuarios;
    }

    public void addCambioTP(int idProducto,String operacion,int idLista,int unidades){
        cambiosTipos.add(new CambiosTP(idProducto,operacion,idLista,unidades));
    }
    public void addCambioPC(int idProducto,String operacion,int idLista,int unidades){
        cambiosComerciales.add(new CambiosPC(idProducto,operacion,idLista,unidades));
    }

    public void addCambioLS(int idLista){
        cambiosListas.add(idLista);
    }

    public void addCambioUS(String nombre,String operacion,String rol,int idLista){
        cambiosUsuarios.add(new CambiosUS(nombre,operacion,rol,idLista));
    }

    public boolean existenCambios(){
        if(cambiosListas.isEmpty() && cambiosComerciales.isEmpty() && cambiosUsuarios.isEmpty() && cambiosTipos.isEmpty())
            return false;
        else
            return true;
    }

    public String getJsonCambios(){
        String json= Constants.ENVIAR_NOTIFICACIONES+Constants.SEPARATOR+QueryUtils.getUsuario().getId()+Constants.SEPARATOR+
                "{tp:[";
        //ProcesarTipos de productos
        for (int i=0;i<cambiosTipos.size();i++){
            CambiosTP c=cambiosTipos.get(i);
            json+="{id:"+c.getIdProducto()+",operacion:"+c.getOperacion()+",idLista:"+c.getIdLista()+",unidades:"+c.getUnidades();
            if(i==cambiosTipos.size()-1){
                json+="}";
            }else{
                json+="},";
            }
        }
        json+="],pc:[";
        //Procesar Comerciales
        for (int i=0;i<cambiosComerciales.size();i++){
            CambiosPC c=cambiosComerciales.get(i);
            json+="{id:"+c.getIdProducto()+",operacion:"+c.getOperacion()+",idLista:"+c.getIdLista()+",unidades:"+c.getUnidades();
            if(i==cambiosComerciales.size()-1){
                json+="}";
            }else{
                json+="},";
            }
        }
        json+="],lsb:[";
        //Procesar listas
        for (int i=0;i<cambiosListas.size();i++){
            json+="{id:"+cambiosListas.get(i);
            if(i==cambiosListas.size()-1){
                json+="}";
            }else{
                json+="},";
            }
        }
        json+="],us:[";
        //Procesar usuarios
        for (int i=0;i<cambiosUsuarios.size();i++){
            CambiosUS c=cambiosUsuarios.get(i);
            json+="{nombre:"+c.getNombreUsuario()+",operacion:"+c.getOperacion()+",rol:"+c.getRol()+",idLista:"+c.getIdLista();
            if(i==cambiosUsuarios.size()-1){
                json+="}";
            }else{
                json+="},";
            }
        }
        json+="]}";

        return json;
    }

    public void limpiarCambios(){
        cambiosUsuarios.clear();
        cambiosTipos.clear();
        cambiosListas.clear();
        cambiosComerciales.clear();
        cambiosUsuarios.clear();
    }

}
