package com.example.appcompra.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.TipoProducto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private List<Producto> productos;
    private Activity activity;
    private int layout;
    private Context context;

    public ProductoAdapter(TreeSet<Producto> productos, Activity activity, int layout, Context context) {
        this.productos = new ArrayList<>(productos);
        this.activity = activity;
        this.layout = layout;
        this.context = context;
    }

    public ProductoAdapter() {
    }

    private Context getContext(){return context;}
    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ProductoAdapter.ViewHolder viewHolder,final int i) {
        final Producto producto=productos.get(i);
        String nombre=producto.getNombre();
        nombre=nombre.substring(0,1).toUpperCase() + nombre.substring(1);
        viewHolder.nombre.setText(corregirNombre(nombre));
        Picasso.get().load(producto.getUrl()).into(viewHolder.imagen);
        if(viewHolder.cardView!=null){
            if(producto.isSeleccionado()){
                viewHolder.cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorProducto));
                viewHolder.nombre.setTextColor(getContext().getResources().getColor(R.color.white));
                viewHolder.imagen.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.white)));
            }else{
                viewHolder.imagen.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorProducto)));
                viewHolder.cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                viewHolder.nombre.setTextColor(getContext().getResources().getColor(R.color.colorProducto));
            }
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if(producto.isSeleccionado()){
                        producto.setSeleccionado(false);
                    }else {
                        producto.setSeleccionado(true);
                    }
                    if(producto.isSeleccionado()){
                        viewHolder.cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorProducto));
                        viewHolder.nombre.setTextColor(getContext().getResources().getColor(R.color.white));
                        viewHolder.imagen.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.white)));
                    }else{
                        viewHolder.imagen.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorProducto)));
                        viewHolder.cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                        viewHolder.nombre.setTextColor(getContext().getResources().getColor(R.color.colorProducto));
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return productos.size();
    }
    public void filtrarLista(ArrayList<Producto> listaFiltrada){
        productos=listaFiltrada;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        ImageView imagen;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.item_row_cardView);
            nombre=itemView.findViewById(R.id.nombre_producto);
            imagen=itemView.findViewById(R.id.imagen_producto);
        }
    }
    public String corregirNombre(String nombre){
        if(nombre.contains("_"))
            nombre=nombre.replaceAll("_"," ");
        return nombre;
    }
}
