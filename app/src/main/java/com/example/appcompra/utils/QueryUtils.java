package com.example.appcompra.utils;

import android.util.Log;

import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductosConID;
import com.example.appcompra.clases.ProductosListaConID;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.RecetasConID;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
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
        int receta;
        int cadena;
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
                receta=productoActual.getInt("receta");
                unidades=productoActual.getInt("unidades");
                comprado=productoActual.getBoolean("comprado");
                urlImagen=productoActual.getString("urlimagen");
                ProductoLista p=new ProductoLista(id,nombre,unidades,receta,0,comprado,urlImagen,null,null);
                productos.add(p);
            }

            for (int i=0;i<comerciales.length();i++){
                productoActual=comerciales.getJSONObject(i);
                id=productoActual.getInt("id");
                nombre=productoActual.getString("nombre");
                cantidad=productoActual.getString("cantidad");
                unidades=productoActual.getInt("unidades");
                receta=productoActual.getInt("receta");
                cadena=productoActual.getInt("cadena");
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

    public static LinkedList<Notificacion> procesarNotificacion(String json) {
        LinkedList<Notificacion> notificaciones =new LinkedList<>();
        String autor="";
        String tipoNotificacion;
        String operacion;
        String rol="";
        String nombreLista;
        try{
            JSONObject raiz=new JSONObject(json);
            JSONArray data=raiz.getJSONArray("notificaciones");
            JSONObject notificacionActual;
            for (int i=0;i<data.length();i++){
                notificacionActual=data.getJSONObject(i);
                if(notificacionActual.has("autor"))
                    autor=notificacionActual.getString("autor");
                else
                    autor=notificacionActual.getString("usuario");
                operacion=notificacionActual.getString("operacion");

                if(notificacionActual.has("rol")){
                    rol=notificacionActual.getString("rol");
                    tipoNotificacion="usuarios";
                }else{
                    tipoNotificacion="listas";
                }
                nombreLista=notificacionActual.getString("nombreLista");
                Notificacion n=new Notificacion(autor,tipoNotificacion,operacion,rol,nombreLista);
                if(!autor.equals(QueryUtils.getUsuario().getNombre()))
                    notificaciones.add(n);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return notificaciones;
    }
    /*JSON RECETAS
     * {"id":12,
           "recetas":[
              {
                 "id":"24",
                 "nombre":"pastas",
                 "descripcion":"esta es la descripcion de la receta",
                 "preparacion":"1.-Añadir huevos, 2.- Añadir pastas, 3.- añadir tomate a la pasta",
                 "urlimagen":"https://image.flaticon.com/icons/png/512/108/108976.png",
                 "ingredientes":[  ]
              },
              {
                 "id":"24",
                 "nombre":"pastas",
                 "descripcion":"esta es la descripcion de la receta",
                 "preparacion":"1.-Añadir huevos, 2.- Añadir pastas, 3.- añadir tomate a la pasta",
                 "urlimagen":"https://image.flaticon.com/icons/png/512/108/108976.png",
                 "ingredientes":[  ]
              }
           ]
        } */
    public static RecetasConID recetasJSON(String entrada) {
        int id;
        String nombre;
        String urlImagen;
        String descripcion;
        String preparacion;
        TreeSet<Receta> recetas =new TreeSet<>();
        int idCategoria=0;
        try{
            JSONObject raiz=new JSONObject(entrada);
            idCategoria=raiz.getInt("id");
            JSONArray data=raiz.getJSONArray("recetas");
            JSONObject recetaActual;
            for (int i=0;i<data.length();i++){
                recetaActual=data.getJSONObject(i);
                id=recetaActual.getInt("id");
                nombre=recetaActual.getString("nombre");
                urlImagen=recetaActual.getString("url");
                /*
                descripcion=recetaActual.getString("descripcion");
                preparacion=recetaActual.getString("preparacion");
                JSONArray ing=recetaActual.getJSONArray("ingredientes");
                LinkedList<TipoProducto> ingredientes=new LinkedList<>();
                for(int j=0;j<ing.length();j++){
                    JSONObject ingre=ing.getJSONObject(j);
                    TipoProducto tp=new TipoProducto(ingre.getInt("id"),ingre.getString("nombre"));
                    ingredientes.add(tp);
                }
                if(!ingredientes.isEmpty())
                    r.setIngredientes(ingredientes);
                    */
                Receta r=new Receta(id,nombre,urlImagen);
                recetas.add(r);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return new RecetasConID(idCategoria,recetas);
    }
    public static Receta interiorRecetaJSON(String entrada) {
        Receta r=null;
        int id;
        String nombre;
        String urlImagen;
        String descripcion;
        String preparacion;
        try{
            JSONObject raiz=new JSONObject(entrada);
            id=raiz.getInt("id");
            nombre=raiz.getString("nombre");
            preparacion=raiz.getString("preparacion");
            descripcion=raiz.getString("descripcion");
            urlImagen=raiz.getString("url");
            TreeSet<ProductoLista> ingredientes=new TreeSet<>();
            JSONArray ing=raiz.getJSONArray("ingredientes");
            for(int j=0;j<ing.length();j++){
                JSONObject ingre=ing.getJSONObject(j);
                ProductoLista tp=new ProductoLista(ingre.getInt("id"),ingre.getString("nombre"),1,id,0,false,ingre.getString("urlimagen"),"null","");
                ingredientes.add(tp);
            }
            r=new Receta(id,nombre,descripcion,preparacion,urlImagen);
            if(!ingredientes.isEmpty())
                r.setIngredientes(ingredientes);

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }
        return  r;
    }

}
