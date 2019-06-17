package com.example.appcompra.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class DespensaAdapter extends RecyclerView.Adapter<DespensaAdapter.ViewHolder> {
    private List<ProductoLista> productos;
    private Activity activity;
    private int layout;
    private Context context;
    private OnItemClickListener listener;
    public DespensaAdapter(TreeSet<ProductoLista> productos, Activity activity, int layout, Context context,OnItemClickListener listener) {
        this.productos = new ArrayList<>(productos);
        this.activity = activity;
        this.layout = layout;
        this.context = context;
        this.listener=listener;
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

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final DespensaAdapter.ViewHolder viewHolder,final int i) {
        final ProductoLista producto=(ProductoLista)productos.get(i);
        Lista li=null;
        String nombre=producto.getNombre();
        nombre=nombre.substring(0,1).toUpperCase() + nombre.substring(1);
        viewHolder.nombre.setText(corregirNombre(nombre));
        viewHolder.unidades.setText(producto.getUnidades()+" u");

        if(producto.getUrl()!=null)
            Picasso.get().load(producto.getUrl()).into(viewHolder.imagen);
        if(producto.getCadena()==null){
            viewHolder.imagen.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }
        if(Singleton.getInstance().getIdListaSeleccionada()==0){
            viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient));
        }else{

            for(Lista l:Singleton.getInstance().getListas()){
                if(l.getId()==Singleton.getInstance().getIdListaSeleccionada())
                    li=l;
            }

            final Lista finalLi = li;

            if(finalLi !=null) {
                Log.e("xd", "cambiado");
                switch (finalLi.getRol().toLowerCase()) {
                    case "administrador":
                        viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_lista_admin));
                        break;
                    case "participante":
                        viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_lista_participante));
                        break;
                    case "espectador":
                        viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_lista_espectador));
                        break;
                }
                viewHolder.linearProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        producto.setSeleccionado(!producto.isSeleccionado());
                        if (producto.isSeleccionado()) {
                            viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context, R.drawable.producto_seleccionado));
                        } else {
                            switch (finalLi.getRol().toLowerCase()) {
                                case "administrador":
                                    viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_lista_admin));
                                    break;
                                case "participante":
                                    viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_lista_participante));
                                    break;
                                case "espectador":
                                    viewHolder.linearProducto.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_lista_espectador));
                                    break;
                            }
                        }
                        listener.onSeleccionarLista();
                    }
                });
            }
        }

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
        LinearLayout linearProducto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearProducto=itemView.findViewById(R.id.linearProducto);
            nombre=itemView.findViewById(R.id.nombre_producto);
            imagen=itemView.findViewById(R.id.imagen);
            unidades=itemView.findViewById(R.id.unidades);

        }
    }

    public String corregirNombre(String nombre){
        if(nombre.contains("_"))
            nombre=nombre.replaceAll("_"," ");
        return nombre;
    }
    public interface OnItemClickListener{
        void onSeleccionarLista();
    }

}
