package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.adapters.ListaAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoComercialLista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.TipoProductoLista;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.DespensaViewModel;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;
import java.util.LinkedList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class DespensaFragment extends Fragment {

    protected ArrayList<Producto> productos;
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
        productos=new ArrayList<>();
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
        rellenarProductos();
        updateUI(productos);
        //updateEditTextFiltrar(view);
        return view;
    }
    public void intentProductos(){
        ProductosFragment myFragment = new ProductosFragment();
        Bundle arguments=new Bundle();
        arguments.putInt("posLista",0);
        myFragment.setArguments(arguments);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragment, myFragment).addToBackStack(null).commit();
    }
    public void rellenarProductos(){
        productos.add(new TipoProductoLista(2,"Hamburguesa","2 unidades",null,null,false,"https://image.flaticon.com/icons/png/512/93/93104.png"));
        productos.add(new TipoProductoLista(4,"Pepinos","5 unidades",null,null,false,"https://image.flaticon.com/icons/png/512/93/93104.png"));
        productos.add(new ProductoComercialLista(3,"Hamburguesa","500g",null,null,false,"mercadona","https://image.flaticon.com/icons/png/512/93/93104.png","4"));
    }
    private void updateEditTextFiltrar(View view){
        EditText editText=view.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });
    }

    public void onResume() {
        super.onResume();
        ConnectivityManager manager=(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info=manager.getActiveNetworkInfo();
        boolean isConnected=info!=null && info.isConnected();
        /*if(isConnected) {
            model.getProductosDespensa().observe(getActivity(), new Observer<ArrayList<Producto>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Producto> p) {
                    if(p!=null){
                        updateUI(p);
                    }else{
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        addProductosCentro.setVisibility(View.VISIBLE);
                    }
                }
            });

        }else{
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }*/
    }

    private void filtrar(String contenidoEditText){
        ArrayList<Producto> lista=new ArrayList<>();
        for (Producto item:productos){
            if(item.getNombre().toLowerCase().contains(contenidoEditText.toLowerCase())){
                lista.add(item);
            }
        }
        adapter.filtrarLista(lista);
    }
    private void updateUI(ArrayList<Producto> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new DespensaAdapter(m, getActivity(), R.layout.item_row_despensa, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mEmptyStateTextView.setVisibility(View.GONE);
        addProductosCentro.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        addProductos.setVisibility(View.VISIBLE);
    }
}