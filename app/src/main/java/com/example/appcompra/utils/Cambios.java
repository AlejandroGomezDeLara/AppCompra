package com.example.appcompra.utils;

import android.util.Log;

import com.example.appcompra.Constants;
import org.json.*;

public class Cambios{
    private String codCambio;
    private int idUsuario;
    private JSONArray cambiosTP;
    private JSONArray cambiosPC;
    private JSONArray cambiosUS;
    private JSONObject todosCambios;

    private static Cambios instance;

    public static Cambios getInstance () {
        if (instance==null)
            instance = new Cambios();

        return instance;
    }

    /*
    * N;2;{"tp":[
    *       {"id":"167","operacion":"add","idLista":"1","unidades":"4","cadena":1,"receta":null}
    *       ],
       "pc":[],
       "us":[{"nombre":"dsa","operacion":"delete","rol":null,"idLista":4},
             {"nombre":"dsa","operacion":"delete","rol":null,"idLista":4},
             {"nombre":"dsa","operacion":"delete","rol":null,"idLista":4}
             ]
    }
    */

    public Cambios(){
        this.codCambio=codCambio;
        this.idUsuario=idUsuario;
        this.cambiosPC=new JSONArray();
        this.cambiosTP=new JSONArray();
        this.cambiosUS=new JSONArray();
        this.todosCambios=new JSONObject();
    }

    public void añadirCambioTipoProducto(int id,String operacion,int idLista,int unidades,String cadena,String receta) {
        JSONObject o=new JSONObject();
        try{
            o.put("id",id);
            o.put("idLista",idLista);
            o.put("operacion",operacion);
            o.put("unidades",unidades);
            o.put("cadena",cadena);
            o.put("receta",receta);
        }catch (JSONException e){
            e.printStackTrace();
        }
        cambiosTP.put(o);
    }

    public void añadirCambioProductoComercial(int id,String operacion,int idLista,int unidades,String cadena,String receta,String marca){
        JSONObject o=new JSONObject();
        try {
            o.put("id",id);
            o.put("idLista",idLista);
            o.put("operacion",operacion);
            o.put("unidades",unidades);
            o.put("cadena",cadena);
            o.put("receta",receta);
            o.put("receta",marca);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JSONObject ob;
        boolean existe=false;
        try {
            for (int i=0;i<cambiosTP.length();i++) {
                ob = cambiosTP.getJSONObject(i);
                if(ob.getString("id").equals(o.getString("id"))){
                    Log.e("xd","entra");
                    ob.remove("unidades");
                    ob.put("unidades",o.get("unidades"));
                    existe=true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!existe)
            cambiosPC.put(o);
    }
    public void añadirCambioUsuarios(String nombre,String operacion,String rol,int idLista){
        JSONObject o=new JSONObject();
        try{
            o.put("nombre",nombre);
            o.put("operacion",operacion);
            o.put("rol",rol);
            o.put("idLista",idLista);
        }catch (JSONException e){
            e.printStackTrace();
        }

        cambiosUS.put(o);
    }



    public String getCambiosString(){
        try {
            todosCambios.put("tp",cambiosTP);
            todosCambios.put("pc",cambiosPC);
            todosCambios.put("us",cambiosUS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String cambios=todosCambios.toString();
        limpiarCambios();
        return cambios;
    }

    public void limpiarCambios(){
        todosCambios=new JSONObject();
        cambiosUS=new JSONArray();
        cambiosPC=new JSONArray();
        cambiosTP=new JSONArray();
    }
    public boolean existenCambios(){
        return cambiosUS.length()!=0 || cambiosTP.length()!=0 || cambiosTP.length()!=0;
    }
}
