package com.example.appcompra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.TipoProductoLista;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DespensaAdapter extends RecyclerView.Adapter<DespensaAdapter.ViewHolder> {
    private List<Producto> productos;
    private Activity activity;
    private int layout;
    private Context context;

    public DespensaAdapter(List<Producto> productos, Activity activity, int layout, Context context) {
        this.productos = productos;
        this.activity = activity;
        this.layout = layout;
        this.context = context;
    }


    private Context getContext(){return context;}
    @NonNull
    @Override
    public DespensaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DespensaAdapter.ViewHolder viewHolder,final int i) {
        final TipoProductoLista producto=(TipoProductoLista)productos.get(i);
        viewHolder.nombre.setText(producto.getNombre());
        viewHolder.cantidad.setText(producto.getUnidades());
        if(producto.getUrl()!=null)
            Picasso.get().load(producto.getUrl()).into(viewHolder.imagen);
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
        TextView cantidad;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre_producto);
            imagen=itemView.findViewById(R.id.imagen_producto);
            cantidad=itemView.findViewById(R.id.cantidad);
        }
    }

}
