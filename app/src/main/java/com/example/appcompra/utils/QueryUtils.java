package com.example.appcompra.utils;

import android.content.Context;
import android.util.Log;

import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.TipoProducto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryUtils {
    public static ArrayList<Producto> tipoProductosJson(String entrada, String categoria){
        int id;
        String nombre;
        String urlImagen;
        ArrayList<Producto> productos =new ArrayList<>();
        try{
            JSONObject raiz=new JSONObject(entrada);
            JSONArray data=raiz.getJSONArray("productos");
            JSONObject productoActual;
            for (int i=0;i<data.length();i++){
                productoActual=data.getJSONObject(i);
                id=productoActual.getInt("id");
                nombre=productoActual.getString("nombre");
                urlImagen=productoActual.getString("url");
                TipoProducto p=new TipoProducto(id,nombre,categoria,urlImagen);
                productos.add(p);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return productos;
    }
}
