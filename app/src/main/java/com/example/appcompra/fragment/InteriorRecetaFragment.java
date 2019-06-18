package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.TreeSet;


public class InteriorRecetaFragment extends Fragment {
    private RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    private Receta recetaActual;
    private ImageView imagen;
    private TextView nombre;
    private TextView preparacion;
    private TextView descripcion;
    private TextView preparacion_label;
    private TextView descripcion_label;
    private TextView ingredientes_label;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interior_receta, container, false);
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.INTERIOR_RECETA_PETICION,QueryUtils.getUsuario().getId(),Singleton.getInstance().getIdRecetaSeleccionada()+"",5));
        recyclerView=view.findViewById(R.id.recyclerView);
        imagen=view.findViewById(R.id.imagen);
        nombre=view.findViewById(R.id.nombre);
        preparacion=view.findViewById(R.id.preparacion);
        descripcion=view.findViewById(R.id.descripcion);
        preparacion_label=view.findViewById(R.id.preparacion_label);
        descripcion_label=view.findViewById(R.id.descripcion_label);
        ingredientes_label=view.findViewById(R.id.ingredientes_label);
        return view;
    }

    private void updateUI(Receta r){
        /*productos.clear();
        productos.addAll(m);
        */
        if(r!=null){
            Picasso.get().load(r.getUrl()).fit().centerCrop().into(imagen);
            nombre.setText(r.getNombre());
            preparacion.setText(r.getPreparacion());
            descripcion.setText(r.getDescripcion());
            if(r.getIngredientes()!=null){
                adapter=new ProductoAdapter(r.getIngredientes(), getActivity(), R.layout.item_row_ingrediente, getActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ingredientes_label.setVisibility(View.VISIBLE);
                descripcion_label.setVisibility(View.VISIBLE);
                preparacion_label.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getInteriorRecetaViewModel().getRecetaActual().observe(getActivity(), new Observer<Receta>() {
            @Override
            public void onChanged(@Nullable Receta r) {
                if(r!=null){
                    recetaActual=r;
                    updateUI(recetaActual);
                }
            }
        });
    }
}

