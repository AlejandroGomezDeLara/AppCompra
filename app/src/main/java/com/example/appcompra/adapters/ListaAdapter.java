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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.clases.Lista;

import java.util.ArrayList;
import java.util.List;


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private List<Lista> listas;
    private Activity activity;
    private int layout;
    private Context context;

    public ListaAdapter(List<Lista> listas, Activity activity, int layout, Context context) {
        this.listas = listas;
        this.activity = activity;
        this.layout = layout;
        this.context = context;
    }


    private Context getContext(){return context;}
    @NonNull
    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }
    public void addLista(List<Lista> listas){
        this.listas=listas;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull final ListaAdapter.ViewHolder viewHolder,final int i) {
        final Lista lista=listas.get(i);
        viewHolder.titulo.setText(lista.getTitulo());
        viewHolder.linearLista.setBackgroundResource(lista.getImagenFondo());
        if(lista.getNumeroUsuarios()>4){
            viewHolder.numeroPersonas.setText("y "+(lista.getNumeroUsuarios()-4)+" más");
        }else{
            viewHolder.numeroPersonas.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return listas.size();
    }
    public void filtrarLista(ArrayList<Lista> listaFiltrada){
        listas=listaFiltrada;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        LinearLayout linearLista;
        TextView numeroPersonas;
        ImageView imagenUsuario1;
        ImageView imagenUsuario2;
        ImageView imagenUsuario3;
        ImageView imagenUsuario4;
        TextView nombreUsuario1;
        TextView nombreUsuario2;
        TextView nombreUsuario3;
        TextView nombreUsuario4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo=itemView.findViewById(R.id.titulo);
            linearLista=itemView.findViewById(R.id.lista);
            numeroPersonas=itemView.findViewById(R.id.numero_personas);
            imagenUsuario1=itemView.findViewById(R.id.imagen_usuario1);
            imagenUsuario2=itemView.findViewById(R.id.imagen_usuario2);
            imagenUsuario3=itemView.findViewById(R.id.imagen_usuario3);
            imagenUsuario4=itemView.findViewById(R.id.imagen_usuario4);
            nombreUsuario1=itemView.findViewById(R.id.nombre_usuario1);
            nombreUsuario2=itemView.findViewById(R.id.nombre_usuario2);
            nombreUsuario3=itemView.findViewById(R.id.nombre_usuario3);
            nombreUsuario4=itemView.findViewById(R.id.nombre_usuario4);
        }
    }
    public String corregirNombre(String nombre){
        if(nombre.indexOf("_")>-1 || nombre.indexOf(" ")>-1){
            if(nombre.indexOf("_")>-1)
                nombre=nombre.substring(0,nombre.indexOf("_"))+"...";
            else if(nombre.indexOf(" ")>-1)
                nombre=nombre.substring(0,nombre.indexOf(" "))+"...";

        }
        return nombre;
    }
}
