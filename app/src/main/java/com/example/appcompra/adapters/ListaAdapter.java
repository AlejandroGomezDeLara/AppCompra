package com.example.appcompra.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.fragment.InteriorListaFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private List<Lista> listas;
    private Activity activity;
    private int layout;
    private Context context;
    private OnItemClickListener listener;

    public ListaAdapter(List<Lista> listas, Activity activity, int layout, Context context,OnItemClickListener listener) {
        this.listas = listas;
        this.activity = activity;
        this.layout = layout;
        this.context = context;
        this.listener=listener;
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
        viewHolder.linearLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setPosicionSpinnerListas(i+1);
                ((MainActivity)activity).getViewPager().setCurrentItem(5);
            }
        });
        if(lista.getNumeroUsuarios()==0){
            viewHolder.usuario1.setVisibility(View.GONE);
            viewHolder.usuario2.setVisibility(View.GONE);
            viewHolder.usuario3.setVisibility(View.GONE);
            viewHolder.usuario4.setVisibility(View.GONE);
        }
        if(lista.getNumeroUsuarios()==1){
            viewHolder.usuario1.setVisibility(View.VISIBLE);
            viewHolder.usuario2.setVisibility(View.GONE);
            viewHolder.usuario3.setVisibility(View.GONE);
            viewHolder.usuario4.setVisibility(View.GONE);
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0));
        }
        if(lista.getNumeroUsuarios()==2){
            viewHolder.usuario1.setVisibility(View.VISIBLE);
            viewHolder.usuario2.setVisibility(View.VISIBLE);
            viewHolder.usuario3.setVisibility(View.GONE);
            viewHolder.usuario4.setVisibility(View.GONE);
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0));
            viewHolder.nombreUsuario2.setText(lista.getUsuarios().get(1));
        }
        if(lista.getNumeroUsuarios()==3){
            viewHolder.usuario1.setVisibility(View.VISIBLE);
            viewHolder.usuario2.setVisibility(View.VISIBLE);
            viewHolder.usuario3.setVisibility(View.VISIBLE);
            viewHolder.usuario4.setVisibility(View.GONE);
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0));
            viewHolder.nombreUsuario2.setText(lista.getUsuarios().get(1));
            viewHolder.nombreUsuario3.setText(lista.getUsuarios().get(2));
        }
        if(lista.getNumeroUsuarios()>=4){
            viewHolder.usuario1.setVisibility(View.VISIBLE);
            viewHolder.usuario2.setVisibility(View.VISIBLE);
            viewHolder.usuario3.setVisibility(View.VISIBLE);
            viewHolder.usuario4.setVisibility(View.VISIBLE);
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0));
            viewHolder.nombreUsuario2.setText(lista.getUsuarios().get(1));
            viewHolder.nombreUsuario3.setText(lista.getUsuarios().get(2));
            viewHolder.nombreUsuario4.setText(lista.getUsuarios().get(3));
        }
        if(lista.getNumeroUsuarios()>4){
            viewHolder.numeroPersonas.setText("y "+(lista.getNumeroUsuarios()-4)+" más");
        }else{
            viewHolder.numeroPersonas.setVisibility(View.GONE);
        }
        viewHolder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(lista);
            }
        });
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
        LinearLayout usuario1;
        LinearLayout usuario2;
        LinearLayout usuario3;
        LinearLayout usuario4;
        TextView nombreUsuario1;
        TextView nombreUsuario2;
        TextView nombreUsuario3;
        TextView nombreUsuario4;
        ImageView borrar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            borrar=itemView.findViewById(R.id.borrar);
            titulo=itemView.findViewById(R.id.titulo);
            linearLista=itemView.findViewById(R.id.linearLista);
            usuario1=itemView.findViewById(R.id.usuario1);
            usuario2=itemView.findViewById(R.id.usuario2);
            usuario3=itemView.findViewById(R.id.usuario3);
            usuario4=itemView.findViewById(R.id.usuario4);
            numeroPersonas=itemView.findViewById(R.id.numero_personas);
            nombreUsuario1=itemView.findViewById(R.id.nombre_usuario1);
            nombreUsuario2=itemView.findViewById(R.id.nombre_usuario2);
            nombreUsuario3=itemView.findViewById(R.id.nombre_usuario3);
            nombreUsuario4=itemView.findViewById(R.id.nombre_usuario4);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Lista l);
    }

}
