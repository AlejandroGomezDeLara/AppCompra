package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.DespensaViewModel;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;
import java.util.TreeSet;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class DespensaFragment extends Fragment {

    protected TreeSet<ProductoLista> productos;
    protected RecyclerView recyclerView;
    protected DespensaAdapter adapter;
    protected DespensaViewModel model;
    protected ProgressBar loadingIndicator;
    protected TextView mEmptyStateTextView;
    protected Usuario usuario;
    protected Button addProductos;
    protected Button addProductosCentro;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_despensa, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView=view.findViewById(R.id.recyclerView);
        model= ViewModelProviders.of(getActivity()).get(DespensaViewModel.class);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        productos=new TreeSet<>();
        usuario= QueryUtils.getUsuario();
        addProductos=view.findViewById(R.id.añadir_boton);
        addProductos.setVisibility(View.GONE);
        addProductosCentro=view.findViewById(R.id.añadir_boton_centro);
        addProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentProductos();
            }
        });
        addProductosCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentProductos();
            }
        });
        Singleton.getInstance().setIdListaSeleccionada(0);
        return view;
    }
    public void intentProductos(){
        Singleton.getInstance().setPosicionSpinnerListas(0);
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(3);
    }

    public void añadirPRoductoDespensa(ProductoLista productoLista){
        boolean existe=false;
        for(ProductoLista productoLista1: productos){
            if(productoLista.getId()==productoLista1.getId()){
                existe=true;
                productoLista1.sumarUnidades(productoLista.getUnidades());
            }
        }
        if(!existe)productos.add(productoLista);


    }


    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getProductosListaViewModel().getProductosLista().observe(getActivity(), new Observer<TreeSet<ProductoLista>>() {
            @Override
            public void onChanged(@Nullable TreeSet<ProductoLista> p) {
                if(p!=null){
                    updateUI(p);
                }
            }
        });
    }

    private void updateUI(TreeSet<ProductoLista> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new DespensaAdapter(m, getActivity(), R.layout.item_row_despensa, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mEmptyStateTextView.setVisibility(View.GONE);
        addProductosCentro.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        addProductos.setVisibility(View.VISIBLE);
    }
    @Override
    public void onPause() {
        super.onPause();

    }
}