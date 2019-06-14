package com.example.appcompra.utils;

import android.util.Log;

import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductosConID;
import com.example.appcompra.clases.ProductosListaConID;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeSet;

public class QueryUtils {
    private static Socket socket;
    private static Usuario usuario;
    private static int usuarioId;
    private static String IP;

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        QueryUtils.IP = IP;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        QueryUtils.usuario = usuario;
        usuarioId=usuario.getId();
    }

    public static ProductosConID tipoProductosJson(String entrada){
        int id;
        String nombre;
        String urlImagen;
        TreeSet<Producto> productos =new TreeSet<>();
        int idCategoria=0;
        try{
            JSONObject raiz=new JSONObject(entrada);
            idCategoria=raiz.getInt("id");
            JSONArray data=raiz.getJSONArray("productos");
            JSONObject productoActual;
            for (int i=0;i<data.length();i++){
                productoActual=data.getJSONObject(i);
                id=productoActual.getInt("id");
                nombre=productoActual.getString("nombre");
                urlImagen=productoActual.getString("urlimagen");
                TipoProducto p=new TipoProducto(id,nombre,urlImagen);
                productos.add(p);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return new ProductosConID(idCategoria,productos);
    }

    public static TreeSet<Categoria> categoriasJson(String entrada){
        int id;
        String nombre;
        TreeSet<Categoria> categorias=new TreeSet<>();
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

    public static TreeSet<Lista> listasJson(String json) {
        int id;
        String nombre;
        String rol="";
        TreeSet<Lista> listas=new TreeSet<>();
        try{
            JSONObject raiz=new JSONObject(json);
            JSONArray data=raiz.getJSONArray("listas");
            JSONObject listaActual;
            for (int i=0;i<data.length();i++){
                listaActual=data.getJSONObject(i);
                id=listaActual.getInt("id");
                nombre=listaActual.getString("nombre");
                JSONArray usuariosLista=listaActual.getJSONArray("usuarios");
                ArrayList<Usuario> usuarios=new ArrayList<>();
                for (int j=0;j<usuariosLista.length();j++){
                    JSONObject usuarioActual=usuariosLista.getJSONObject(j);
                    Usuario usuario=new Usuario(usuarioActual.getString("nombre"),usuarioActual.getString("rol"));
                    if(usuario.getNombre().equals(QueryUtils.getUsuario().getNombre()))
                        rol=usuario.getRol();
                    usuarios.add(usuario);
                }
                Lista c=new Lista(id,nombre,rol);
                if(!usuarios.isEmpty())
                    c.setUsuarios(usuarios);
                listas.add(c);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return listas;
    }

    public static ProductosListaConID productosLista(String json){
        int id;
        int idLista=0;
        String nombre;
        String urlImagen;
        String marca;
        String receta;
        String cadena;
        String cantidad;
        int unidades;
        boolean comprado;
        TreeSet<ProductoLista> productos =new TreeSet<>();

        try{
            JSONObject raiz=new JSONObject(json);
            idLista=raiz.getInt("id");
            JSONArray tipos=raiz.getJSONArray("tipos");
            JSONArray comerciales=raiz.getJSONArray("comerciales");
            JSONObject productoActual;

            for (int i=0;i<tipos.length();i++){
                productoActual=tipos.getJSONObject(i);
                id=productoActual.getInt("id");
                nombre=productoActual.getString("nombre");
                receta=productoActual.getString("receta");
                unidades=productoActual.getInt("unidades");
                comprado=productoActual.getBoolean("comprado");
                urlImagen=productoActual.getString("urlimagen");
                ProductoLista p=new ProductoLista(id,nombre,unidades,receta,null,comprado,urlImagen,null,null);
                productos.add(p);
            }

            for (int i=0;i<comerciales.length();i++){
                productoActual=comerciales.getJSONObject(i);
                id=productoActual.getInt("id");
                nombre=productoActual.getString("nombre");
                cantidad=productoActual.getString("cantidad");
                unidades=productoActual.getInt("unidades");
                receta=productoActual.getString("receta");
                cadena=productoActual.getString("cadena");
                comprado=productoActual.getBoolean("comprado");
                urlImagen=productoActual.getString("urlimagen");
                marca=productoActual.getString("marca");
                ProductoLista p=new ProductoLista(id,nombre,unidades,receta,cadena,comprado,urlImagen,marca,cantidad);

                productos.add(p);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return new ProductosListaConID(idLista,productos);
    }

}
