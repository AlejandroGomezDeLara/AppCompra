package com.example.appcompra.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.clases.TipoProducto;

import java.util.ArrayList;
import java.util.List;


public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private List<TipoProducto> productos;
    private Activity activity;
    private int layout;
    private Context context;

    public ProductoAdapter(List<TipoProducto> productos, Activity activity, int layout, Context context) {
        this.productos = productos;
        this.activity = activity;
        this.layout = layout;
        this.context = context;
    }


    private Context getContext(){return context;}
    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }
    public void addProducto(List<TipoProducto> productos){
        this.productos=productos;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull final ProductoAdapter.ViewHolder viewHolder,final int i) {
        final TipoProducto producto=productos.get(i);
        if(producto.getNombre().contains("niio"))
            viewHolder.nombre.setText(corregirNombre(producto.getNombre().replaceAll("niio","ñ")));
        else
            viewHolder.nombre.setText(corregirNombre(producto.getNombre()));
        if(producto.getImagen()==0){
            viewHolder.imagen.setImageResource(R.drawable.interrogacion);
        }else{
            viewHolder.imagen.setImageResource(producto.getImagen());
        }
    }


    @Override
    public int getItemCount() {
        return productos.size();
    }
    public void filtrarLista(ArrayList<TipoProducto> listaFiltrada){
        productos=listaFiltrada;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        ImageView imagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre_producto);

            imagen=itemView.findViewById(R.id.imagen_producto);
        }
    }
    public String corregirNombre(String nombre){
        if(nombre.contains("niio")){
            nombre.replaceAll("niio","ñ");
        }
        if(nombre.indexOf("_")>-1 || nombre.indexOf(" ")>-1){
            if(nombre.indexOf("_")>-1)
                nombre=nombre.substring(0,nombre.indexOf("_"))+"...";
            else if(nombre.indexOf(" ")>-1)
                nombre=nombre.substring(0,nombre.indexOf(" "))+"...";

        }
        if(nombre.length()>8){
            nombre=nombre.substring(0,7)+"...";
        }
        return nombre;
    }
}
