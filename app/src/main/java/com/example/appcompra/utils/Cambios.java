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
    private CambiosAsyncTask cambiosAsyncTask=null;
    private CambiosAsyncTaskTest cambiosAsyncTaskTest=null;

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

    public void addCambioTP(int idProducto,String operacion,int idLista){
        cambiosTipos.add(new CambiosTP(idProducto,operacion,idLista));
    }
    public void addCambioPC(int idProducto,String operacion,int idLista){
        cambiosComerciales.add(new CambiosPC(idProducto,operacion,idLista));
    }

    public void addCambioLS(int idLista){
        cambiosListas.add(idLista);
    }

    public void addCambioUS(String nombre,String operacion,int idLista){
        cambiosUsuarios.add(new CambiosUS(nombre,operacion,idLista));
    }

    public boolean existenCambios(){
        if(cambiosListas.isEmpty() && cambiosComerciales.isEmpty() && cambiosUsuarios.isEmpty() && cambiosTipos.isEmpty())
            return false;
        else
            return true;
    }

    public void enviarCambios(){
        cambiosAsyncTaskTest=new CambiosAsyncTaskTest();
        cambiosAsyncTaskTest.execute((Void) null);
        /*cambiosAsyncTask=new CambiosAsyncTask();
        cambiosAsyncTask.execute((Void) null);*/
    }

    public String getJsonCambios(){
        String json= Constants.ENVIAR_NOTIFICACIONES+Constants.SEPARATOR+QueryUtils.getUsuario().getId()+Constants.SEPARATOR+
                "{tp:[";
        //ProcesarTipos de productos
        for (int i=0;i<cambiosTipos.size();i++){
            CambiosTP c=cambiosTipos.get(i);
            json+="{id:"+c.getIdProducto()+",operacion:"+c.getOperacion()+",idLista:"+c.getIdLista();
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
            json+="{id:"+c.getIdProducto()+",operacion:"+c.getOperacion()+",idLista:"+c.getIdLista();
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
            json+="{id:"+c.getNombreUsuario()+",operacion:"+c.getOperacion()+",idLista:"+c.getIdLista();
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

    public class CambiosAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        CambiosAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            socket= QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.ENVIAR_NOTIFICACIONES+Constants.SEPARATOR+getJsonCambios());
                Log.e("salida notificaciones",Constants.ENVIAR_NOTIFICACIONES+Constants.SEPARATOR+getJsonCambios());
                String entrada=in.readLine();
                Log.e("entrada notificaciones",entrada);
            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean ca) {
            if(ca)
                limpiarCambios();

        }

        @Override
        protected void onCancelled() {

        }
    }
    public class CambiosAsyncTaskTest extends AsyncTask<Void, Void, Boolean> {

        CambiosAsyncTaskTest() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.e("salida notificaciones",getJsonCambios());
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean ca) {
            if(ca)
            limpiarCambios();
        }

        @Override
        protected void onCancelled() {

        }
    }


}
