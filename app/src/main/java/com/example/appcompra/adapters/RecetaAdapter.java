package com.example.appcompra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.clases.Receta;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.ViewHolder> {
    private List<Receta> recetas;
    private Activity activity;
    private int layout;
    private Context context;

    public RecetaAdapter(TreeSet<Receta> recetas, Activity activity, int layout, Context context) {
        this.recetas =new ArrayList<>(recetas);
        this.activity = activity;
        this.layout = layout;
        this.context = context;
    }

    public RecetaAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        final Receta receta=recetas.get(i);
        if(receta.getUrl()!=null)
            Picasso.get().load(receta.getUrl()).into(viewHolder.imagen);
        viewHolder.nombre.setText(receta.getNombre());
    }


    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagen;
        TextView nombre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre);
            imagen=itemView.findViewById(R.id.imagen);
        }
    }
}

