package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.adapters.UsuariosAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private ImageView compartir;
    protected ArrayList<Integer> idListas;
    private Spinner spinnerListas;

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
        idListas=new ArrayList<>();
        Toolbar toolbar= (Toolbar)((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        compartir=toolbar.findViewById(R.id.compartirReceta);
        compartir.setVisibility(View.VISIBLE);
        if(!Singleton.getInstance().existenListas())
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.LISTAS_PETICION,QueryUtils.getUsuario().getId(),4));
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                añadirProductosListaPopup();
            }
        });
        return view;
    }

    public void añadirProductosListaPopup(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_receta, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        spinnerListas=view.findViewById(R.id.spinnerListas);
        updateSpinnerListas(Singleton.getInstance().getListas());
        spinnerListas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Singleton.getInstance().setIdListaSeleccionada(idListas.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button botonCancelar=view.findViewById(R.id.botonCancelarPopup);
        Button botonAceptar=view.findViewById(R.id.botonAceptarPopup);

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Singleton.getInstance().getIdListaSeleccionada()==0){
                    Singleton.getInstance().añadirProductosDespensa(recetaActual.getIngredientes());
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(0);
                }
                else{
                    Singleton.getInstance().añadirProductosListaRecetas(Singleton.getInstance().getIdListaSeleccionada(),recetaActual.getIngredientes());
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(5);
                }

                dialog.dismiss();
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
            TreeSet<Producto> tiposProductos=new TreeSet<>();
            for(ProductoLista p: r.getIngredientes()){
                tiposProductos.add(new Producto(p.getId(),p.getNombre(),p.getUrl()));
            }
            if(r.getIngredientes()!=null){
                adapter=new ProductoAdapter(tiposProductos, getActivity(), R.layout.item_row_ingrediente, getActivity());
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

    @Override
    public void onPause() {
        super.onPause();
        compartir.setVisibility(View.GONE);
    }

    private void updateSpinnerListas(TreeSet<Lista> l) {
        TreeSet<Lista> listasPermisos=new TreeSet<>();
        listasPermisos.addAll(l);
        Iterator iterator=listasPermisos.iterator();
        while (iterator.hasNext()) {
            if(((Lista)iterator.next()).getRol().toLowerCase().equals("espectador"))iterator.remove();
        }
        ArrayList<Lista> li=new ArrayList<>(listasPermisos);
        List<String> valoresSpinner=new ArrayList<>();
        idListas.clear();
        for (int i=0;i<li.size();i++){
            valoresSpinner.add(li.get(i).getTitulo());
            idListas.add(li.get(i).getId());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item,valoresSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListas.setAdapter(adapter);
        if(valoresSpinner.size()>Singleton.getInstance().getPosicionSpinnerListas())
            spinnerListas.setSelection(Singleton.getInstance().getPosicionSpinnerListas());
    }

}

