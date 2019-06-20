package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Oferta;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class PrincipalFragment extends Fragment {
    private ImageView imagenFondoLista;
    private TextView nombreLista;
    private CardView ultimaLista;
    private TextView usuariosLista;
    private TextView textoRecetaRecomendada;
    private ImageView imagenRecetaRecomendada;
    private ImageView imagenOfertaAleatoria;
    private TextView textoOfertaAleatoria;
    private CardView cardViewReceta;
    private Receta recetaActual;
    private Oferta ofertaActual;
    private RecyclerView recomendacionesRecyclerView;
    private TreeSet<Producto> productosRecomendados;
    private ProductoAdapter adapter;
    private int idCategoriaSeleccionadaAnterior;
    private CardView recomendacionesCardView;
    private Spinner spinnerListas;
    protected ArrayList<Integer> idListas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.RECETA_ALEATORIA_PETICION,QueryUtils.getUsuario().getId(),10));
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.PEDIR_OFERTAS_PETICION,QueryUtils.getUsuario().getId(),4));
        Singleton.getInstance().setIdCategoriaSelecionada(-1);
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.RECOMENDACIONES_PETICION,QueryUtils.getUsuario().getId(),4));
        productosRecomendados=new TreeSet<>();
        idListas=new ArrayList<>();
        imagenFondoLista=view.findViewById(R.id.fondo_lista);
        nombreLista=view.findViewById(R.id.nombre_lista);
        ultimaLista=view.findViewById(R.id.ultimaLista);
        usuariosLista=view.findViewById(R.id.usuarios);
        textoRecetaRecomendada=view.findViewById(R.id.nombre_receta);
        imagenRecetaRecomendada=view.findViewById(R.id.imagen_receta);
        cardViewReceta=view.findViewById(R.id.card_view_receta);
        textoOfertaAleatoria=view.findViewById(R.id.nombre_oferta);
        imagenOfertaAleatoria=view.findViewById(R.id.imagen_oferta);
        recomendacionesRecyclerView=view.findViewById(R.id.recyclerView);
        recomendacionesCardView=view.findViewById(R.id.card_view_recomendaciones);
        recomendacionesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirProductosListaPopup();
            }
        });
        idCategoriaSeleccionadaAnterior=Singleton.getInstance().getIdCategoriaSelecionada();
        cardViewReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentRecetas();
            }
        });
        rellenarUltimaLista();
        ultimaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentUltimaLista();
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        if(!Singleton.getInstance().existenListas())
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.LISTAS_PETICION,QueryUtils.getUsuario().getId(),4));
        if(Singleton.getInstance().existenNotificaciones())
            Log.e("not",Singleton.getInstance().mostrarNotificaciones());
        if(Cambios.getInstance().existenCambios()){
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.ENVIAR_NOTIFICACIONES,QueryUtils.getUsuario().getId(),Cambios.getInstance().getCambiosString(),1));
        }


        ((MainActivity)getActivity()).getRecetaAleatoriaViewModel().getRecetaAleatoria().observe(getActivity(), new Observer<Receta>() {
            @Override
            public void onChanged(@Nullable Receta r) {
                if(r!=null){
                    recetaActual=r;
                    updateRecetaAleatoria(recetaActual);
                }
            }
        });
        ((MainActivity)getActivity()).getModelProductos().getProductos().observe(getActivity(), new Observer<TreeSet<Producto>>() {
            @Override
            public void onChanged(@Nullable TreeSet<Producto> p) {
                if(p!=null){
                    productosRecomendados=p;
                    updateRecomendaciones(productosRecomendados);
                }
            }
        });
        ((MainActivity)getActivity()).getOfertaAleatoriaViewModel().getOfertaAleatoria().observe(getActivity(), new Observer<Oferta>() {
            @Override
            public void onChanged(@Nullable Oferta f) {
                if(f!=null){
                    ofertaActual=f;
                    updateOfertaAleatoria(ofertaActual);
                }
            }
        });
    }

    public void rellenarUltimaLista(){
        nombreLista.setText(Singleton.getInstance().getSharedPreferences().getString("nombreUltimaLista","nombre de la ultima lista"));
        usuariosLista.setText("hay "+Singleton.getInstance().getSharedPreferences().getInt("usuariosUltimaLista",0)+" usuarios");
        String rolLista=Singleton.getInstance().getSharedPreferences().getString("rolUltimaLista","espectador");
        switch (rolLista){
            case "administrador":
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.gradient_lista_admin));
                break;
            case "espectador":
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.gradient_lista_espectador));
                break;
            case "participante":
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.gradient_lista_participante));
                break;
            default:
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.producto_seleccionado));
            break;
        }
    }

    public void intentUltimaLista(){
        if(Singleton.getInstance().existenListas()){
            Singleton.getInstance().setIdListaSeleccionada(Singleton.getInstance().getSharedPreferences().getInt("idUltimaLista",0));
            ((MainActivity)getActivity()).getViewPager().setCurrentItem(5);
        }
    }
    public void intentRecetas(){
        if(recetaActual!=null){
            Singleton.getInstance().setIdRecetaSeleccionada(recetaActual.getId());
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.INTERIOR_RECETA_PETICION,QueryUtils.getUsuario().getId(),Singleton.getInstance().getIdRecetaSeleccionada()+"",5));
            ((MainActivity)getActivity()).getViewPager().setCurrentItem(6);
        }
    }

    public void updateRecetaAleatoria(Receta r){
        Picasso.get().load(r.getUrl()).into(imagenRecetaRecomendada);
        textoRecetaRecomendada.setText(r.getNombre());
    }

    public void updateOfertaAleatoria(Oferta f){
        Picasso.get().load(f.getUrl()).into(imagenOfertaAleatoria);
        textoOfertaAleatoria.setText(f.getNombre());
    }

    public void updateRecomendaciones(TreeSet<Producto> productos){
        adapter=new ProductoAdapter(productos, getActivity(), R.layout.item_row_productos, getActivity());
        recomendacionesRecyclerView.setHasFixedSize(true);
        recomendacionesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recomendacionesRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                if(Singleton.getInstance().getIdListaSeleccionada()!=0){
                    TreeSet<ProductoLista> productoSeleccionados=new TreeSet<>();
                    for(Producto p:productosRecomendados){
                        productoSeleccionados.add(new ProductoLista(p.getId(),p.getNombre(),1,0,0,false,p.getUrl(),"",""));
                    }
                    Singleton.getInstance().añadirProductosLista(Singleton.getInstance().getIdListaSeleccionada(),productoSeleccionados);
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


    @Override
    public void onPause() {
        super.onPause();
        Singleton.getInstance().setIdCategoriaSelecionada(idCategoriaSeleccionadaAnterior);
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