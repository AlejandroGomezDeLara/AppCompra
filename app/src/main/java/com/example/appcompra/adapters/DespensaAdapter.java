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
import com.example.appcompra.clases.ProductoLista;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class DespensaAdapter extends RecyclerView.Adapter<DespensaAdapter.ViewHolder> {
    private List<ProductoLista> productos;
    private Activity activity;
    private int layout;
    private Context context;

    public DespensaAdapter(TreeSet<ProductoLista> productos, Activity activity, int layout, Context context) {
        this.productos = new ArrayList<>(productos);
        this.activity = activity;
        this.layout = layout;
        this.context = context;
    }

    public DespensaAdapter() {
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
        final ProductoLista producto=(ProductoLista)productos.get(i);
        viewHolder.nombre.setText(producto.getNombre());
        viewHolder.unidades.setText(producto.getUnidades()+" u");
        if(producto.getUrl()!=null)
            Picasso.get().load(producto.getUrl()).into(viewHolder.imagen);
    }


    @Override
    public int getItemCount() {
        return productos.size();
    }
    public void filtrarLista(ArrayList<ProductoLista> listaFiltrada){
        productos=listaFiltrada;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        ImageView imagen;
        TextView unidades;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre_producto);
            imagen=itemView.findViewById(R.id.imagen);
            unidades=itemView.findViewById(R.id.unidades);
        }
    }

}
