package com.example.appcompra.utils;

import com.example.appcompra.Constants;
import org.json.*;

public class Cambios{
    private String codCambio;
    private int idUsuario;
    private String cambiosListas;
    private String cambiosProductosComeciales;
    private String cambiosTipoProducto;
    private String cambiosUsuarios;
    private JSONArray cambiosArray;
    private JSONObject cambios;

    private static Cambios instance;

    public static Cambios getInstance () {
        if (instance==null)
            instance = new Cambios("N",QueryUtils.getUsuario().getId());

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

    public Cambios(String codCambio,int idUsuario){
        this.codCambio=codCambio;
        this.idUsuario=idUsuario;
        this.cambiosArray=new JSONArray();
    }

    public void añadirCambioTipoProducto(int id,String operacion,int unidades,int cadena,String receta) {
        JSONObject o=new JSONObject();
        try{
            o.put("id",id);
            o.put("operacion",operacion);
            o.put("unidades",unidades);
            o.put("cadena",cadena);
            o.put("receta",receta);
        }catch (JSONException e){
            e.printStackTrace();
        }
        cambiosArray.put(o);
    }

    public void añadirCambioProductoComercial(int id,String operacion,int unidades,int cadena,String receta,String marca){
        JSONObject o=new JSONObject();
        try {
            o.put("id",id);
            o.put("operacion",operacion);
            o.put("unidades",unidades);
            o.put("cadena",cadena);
            o.put("receta",receta);
            o.put("receta",marca);
        }catch (JSONException e){
            e.printStackTrace();
        }

        cambiosArray.put(o);
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

        cambiosArray.put(o);
    }



    public String getCambiosString(){
        return codCambio+Constants.SEPARATOR+idUsuario+Constants.SEPARATOR+cambios.toString();
    }

    public void limpiarCambios(){
        cambiosArray=new JSONArray();
    }

}
