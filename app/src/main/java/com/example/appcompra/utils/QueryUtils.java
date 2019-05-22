package com.example.appcompra.utils;

import android.content.Context;
import android.util.Log;

import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.clases.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class QueryUtils {
    private static Socket socket;
    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        QueryUtils.usuario = usuario;
    }

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
                urlImagen=productoActual.getString("urlimagen");
                TipoProducto p=new TipoProducto(id,nombre,categoria,urlImagen);
                productos.add(p);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return productos;
    }

    public static ArrayList<Categoria> categoriasJson(String entrada){
        int id;
        String nombre;
        ArrayList<Categoria> categorias=new ArrayList<>();
        try{
            JSONObject raiz=new JSONObject(entrada);
            JSONArray data=raiz.getJSONArray("categorias");
            JSONObject categoriaActual;
            for (int i=0;i<data.length();i++){
                categoriaActual=data.getJSONObject(i);
                id=categoriaActual.getInt("id");
                nombre=categoriaActual.getString("nombre");
                Categoria c=new Categoria(id,nombre);
                categorias.add(c);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return categorias;
    }

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        QueryUtils.socket = socket;
    }

    public static ArrayList<Lista> listasJson(String json) {
        int id;
        String nombre;
        String url;
        ArrayList<Lista> listas=new ArrayList<>();
        ArrayList<String> usuariosLista;
        try{
            JSONObject raiz=new JSONObject(json);
            JSONArray data=raiz.getJSONArray("listas");
            JSONObject listaActual;
            for (int i=0;i<data.length();i++){
                usuariosLista=new ArrayList<>();
                listaActual=data.getJSONObject(i);
                id=listaActual.getInt("id");
                nombre=listaActual.getString("nombre");
                url=listaActual.getString("url");
                if(listaActual.getString("usuarios")!=null){
                    String usuarios=listaActual.getString("usuarios");
                    if(!usuarios.isEmpty()){
                        for (int j=0;j<usuarios.split(",").length;j++){
                            usuariosLista.add(usuarios.split(",")[j]);
                        }
                    }
                }
                Lista c=new Lista(id,nombre,url);
                if(!usuariosLista.isEmpty())
                    c.setUsuarios(usuariosLista);
                listas.add(c);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return listas;
    }

}
