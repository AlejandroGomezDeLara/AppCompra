package com.example.appcompra.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import com.example.appcompra.clases.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private LinkedList<Lista> listas;
    private Activity activity;
    private int layout;
    private Context context;
    private OnItemClickListener listener;

    public ListaAdapter(TreeSet<Lista> listas, Activity activity, int layout, Context context, OnItemClickListener listener) {
        this.listas = new LinkedList<>(listas);
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
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final ListaAdapter.ViewHolder viewHolder,final int i) {
        final Lista lista=listas.get(i);
        viewHolder.titulo.setText(lista.getTitulo());
        switch (lista.getRol().toLowerCase()){
            case "administrador":
                viewHolder.linearLista.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_lista_admin));
                break;
            case "participante":
                viewHolder.linearLista.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_lista_participante));
                break;
            case "espectador":
                viewHolder.linearLista.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_lista_espectador));
                break;
        }
        if(!lista.getRol().toLowerCase().equals("administrador")){
            viewHolder.compartir.setVisibility(View.GONE);
        }else{
            viewHolder.compartir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompartirLista(lista);
                }
            });
            viewHolder.compartir.setVisibility(View.VISIBLE);
        }
        viewHolder.linearLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setPosicionSpinnerListas(i+1);
                Singleton.getInstance().setIdListaSeleccionada(lista.getId());
                ((MainActivity)activity).getViewPager().setCurrentItem(5);
            }
        });
        if(lista.getNumeroUsuarios()==0){
            viewHolder.usuarios.setVisibility(View.GONE);
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
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0).getNombre());
            switch (lista.getUsuarios().get(0).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }

        }
        if(lista.getNumeroUsuarios()==2){
            viewHolder.usuario1.setVisibility(View.VISIBLE);
            viewHolder.usuario2.setVisibility(View.VISIBLE);
            viewHolder.usuario3.setVisibility(View.GONE);
            viewHolder.usuario4.setVisibility(View.GONE);
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0).getNombre());
            viewHolder.nombreUsuario2.setText(lista.getUsuarios().get(1).getNombre());
            switch (lista.getUsuarios().get(0).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;

            }
            switch (lista.getUsuarios().get(1).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
        }
        if(lista.getNumeroUsuarios()==3){
            viewHolder.usuario1.setVisibility(View.VISIBLE);
            viewHolder.usuario2.setVisibility(View.VISIBLE);
            viewHolder.usuario3.setVisibility(View.VISIBLE);
            viewHolder.usuario4.setVisibility(View.GONE);
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0).getNombre());
            viewHolder.nombreUsuario2.setText(lista.getUsuarios().get(1).getNombre());
            viewHolder.nombreUsuario3.setText(lista.getUsuarios().get(2).getNombre());
            switch (lista.getUsuarios().get(0).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
            switch (lista.getUsuarios().get(1).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
            switch (lista.getUsuarios().get(2).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario3.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario3.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario3.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
        }
        if(lista.getNumeroUsuarios()>=4){
            viewHolder.usuario1.setVisibility(View.VISIBLE);
            viewHolder.usuario2.setVisibility(View.VISIBLE);
            viewHolder.usuario3.setVisibility(View.VISIBLE);
            viewHolder.usuario4.setVisibility(View.VISIBLE);
            viewHolder.nombreUsuario1.setText(lista.getUsuarios().get(0).getNombre());
            viewHolder.nombreUsuario2.setText(lista.getUsuarios().get(1).getNombre());
            viewHolder.nombreUsuario3.setText(lista.getUsuarios().get(2).getNombre());
            viewHolder.nombreUsuario4.setText(lista.getUsuarios().get(3).getNombre());
            switch (lista.getUsuarios().get(0).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario1.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
            switch (lista.getUsuarios().get(1).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario2.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
            switch (lista.getUsuarios().get(2).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario3.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario3.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario3.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
            switch (lista.getUsuarios().get(3).getRol().toLowerCase()) {
                case "administrador":
                    viewHolder.imagenUsuario4.setColorFilter(ContextCompat.getColor(context, R.color.administrador));
                    break;
                case "participante":
                    viewHolder.imagenUsuario4.setColorFilter(ContextCompat.getColor(context, R.color.participante));
                    break;
                case "espectador":
                    viewHolder.imagenUsuario4.setColorFilter(ContextCompat.getColor(context, R.color.espectador));
                    break;
            }
        }
        if(lista.getNumeroUsuarios()>4){
            viewHolder.numeroPersonas.setText("y "+(lista.getNumeroUsuarios()-4)+" más");
        }else{
            viewHolder.numeroPersonas.setVisibility(View.GONE);
        }
        viewHolder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBorrarLista(lista);
            }
        });

    }

    public ListaAdapter() {
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        LinearLayout linearLista;
        TextView numeroPersonas;
        LinearLayout usuario1;
        LinearLayout usuario2;
        LinearLayout usuario3;
        LinearLayout usuario4;
        ImageView imagenUsuario1;
        ImageView imagenUsuario2;
        ImageView imagenUsuario3;
        ImageView imagenUsuario4;
        TextView nombreUsuario1;
        TextView nombreUsuario2;
        TextView nombreUsuario3;
        TextView nombreUsuario4;
        ImageView borrar;
        ImageView compartir;
        LinearLayout usuarios;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usuarios=itemView.findViewById(R.id.usuarios);
            borrar=itemView.findViewById(R.id.borrar);
            compartir=itemView.findViewById(R.id.compartir);
            titulo=itemView.findViewById(R.id.titulo);
            linearLista=itemView.findViewById(R.id.linearLista);
            usuario1=itemView.findViewById(R.id.usuario1);
            usuario2=itemView.findViewById(R.id.usuario2);
            usuario3=itemView.findViewById(R.id.usuario3);
            usuario4=itemView.findViewById(R.id.usuario4);
            imagenUsuario1=itemView.findViewById(R.id.imagen_usuario1);
            imagenUsuario2=itemView.findViewById(R.id.imagen_usuario2);
            imagenUsuario3=itemView.findViewById(R.id.imagen_usuario3);
            imagenUsuario4=itemView.findViewById(R.id.imagen_usuario4);
            numeroPersonas=itemView.findViewById(R.id.numero_personas);
            nombreUsuario1=itemView.findViewById(R.id.nombre_usuario1);
            nombreUsuario2=itemView.findViewById(R.id.nombre_usuario2);
            nombreUsuario3=itemView.findViewById(R.id.nombre_usuario3);
            nombreUsuario4=itemView.findViewById(R.id.nombre_usuario4);
        }
    }
    public interface OnItemClickListener{
        void onBorrarLista(Lista l);
        void onCompartirLista(Lista l);
    }

}
