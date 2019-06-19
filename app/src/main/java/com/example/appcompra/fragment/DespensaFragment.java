package com.example.appcompra.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.DespensaViewModel;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;
import java.util.TreeSet;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class DespensaFragment extends Fragment {

    protected TreeSet<ProductoLista> productos;
    protected RecyclerView recyclerView;
    protected DespensaAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected TextView mEmptyStateTextView;
    protected Usuario usuario;
    protected Button addProductos;
    protected Button addProductosCentro;
    protected ImageView deleteProductos;
    protected SwipeRefreshLayout refreshLayout;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_despensa, container, false);
        Singleton.getInstance().setIdListaSeleccionada(0);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView=view.findViewById(R.id.recyclerView);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        productos=new TreeSet<>();
        usuario= QueryUtils.getUsuario();
        addProductos=view.findViewById(R.id.añadir_boton);
        addProductos.setVisibility(View.GONE);
        addProductosCentro=view.findViewById(R.id.añadir_boton_centro);
        deleteProductos=view.findViewById(R.id.eliminar_boton);
        addProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentProductos();
            }
        });
        loadingIndicator.setVisibility(View.VISIBLE);
        addProductosCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentProductos();
            }
        });
        Singleton.getInstance().setIdListaSeleccionada(0);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        if(Singleton.getInstance().hayProductosListaSeleccionados())
            Singleton.getInstance().borrarProductosSeleccionados();
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Singleton.getInstance().limpiarProductosLista();
                        Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_DESPENSA_PETICION,QueryUtils.getUsuario().getId(),"0",5));
                        refreshLayout.setRefreshing(false);
                    }
                }
        );
        deleteProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(ProductoLista p: Singleton.getInstance().getProductosListaSeleccionados()){
                    Cambios.getInstance().añadirCambioTipoProducto(p.getId(),"delete",0,0,0,0,"despensa");
                    Singleton.getInstance().borrarProductosSeleccionados();
                    updateUI(Singleton.getInstance().getDespensa());
                }
            }
        });
        return view;
    }
    public void intentProductos(){
        Singleton.getInstance().setPosicionSpinnerListas(0);
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(3);
    }



    public void onResume() {
        super.onResume();
        if(Singleton.getInstance().existenNotificaciones())
            Log.e("not",Singleton.getInstance().mostrarNotificaciones());
        if(Cambios.getInstance().existenCambios()){
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.ENVIAR_NOTIFICACIONES,QueryUtils.getUsuario().getId(),Cambios.getInstance().getCambiosString(),1));
        }
        if(Singleton.getInstance().existenDespensa()){
            updateUI(Singleton.getInstance().getDespensa());
        }else{
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_DESPENSA_PETICION,QueryUtils.getUsuario().getId(),5));
        }
        ((MainActivity)getActivity()).getDespensaViewModel().getProductosDespensa().observe(getActivity(), new Observer<TreeSet<ProductoLista>>() {
            @Override
            public void onChanged(@Nullable TreeSet<ProductoLista> p) {
                if(p!=null){
                    updateUI(p);
                }
            }
        });
    }

    private void updateUI(TreeSet<ProductoLista> m){
        productos.clear();
        productos.addAll(m);

        if (m.isEmpty()) {
            addProductosCentro.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            addProductos.setVisibility(View.GONE);
        } else {
            mEmptyStateTextView.setVisibility(View.GONE);
            addProductos.setVisibility(View.VISIBLE);
            addProductosCentro.setVisibility(View.GONE);
        }
        adapter = new DespensaAdapter(m, getActivity(), R.layout.item_row_despensa, getActivity(), new DespensaAdapter.OnItemClickListener() {
            @Override
            public void onSeleccionarLista() {
                Log.e("xd",Singleton.getInstance().hayProductosListaSeleccionados()+"");
                if (Singleton.getInstance().hayProductosListaSeleccionados()) {
                    deleteProductos.setVisibility(View.VISIBLE);
                    addProductos.setVisibility(View.GONE);
                } else {
                    deleteProductos.setVisibility(View.GONE);
                    addProductos.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadingIndicator.setVisibility(View.GONE);


    }
    @Override
    public void onPause() {
        super.onPause();

    }
}