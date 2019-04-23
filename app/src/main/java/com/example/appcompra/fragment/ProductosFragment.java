package com.example.appcompra.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appcompra.R;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.adapters.ProductoAdapter;
import java.util.ArrayList;

public class ProductosFragment extends Fragment {
    protected ArrayList<TipoProducto> productos;
    protected RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    ProgressBar loadingIndicator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView=view.findViewById(R.id.recyclerView);
        productos=new ArrayList<>();
        productos.add(new TipoProducto("Cocacola",getContext()));
        productos.add(new TipoProducto("brocoli",getContext()));
        productos.add(new TipoProducto("tomate",getContext()));
        productos.add(new TipoProducto("pinia",getContext()));
        productos.add(new TipoProducto("peluche",getContext()));
        productos.add(new TipoProducto("diente",getContext()));
        productos.add(new TipoProducto("champinion",getContext()));
        productos.add(new TipoProducto("vinagre",getContext()));
        productos.add(new TipoProducto("patatas fritas",getContext()));


        updateUI(productos);
        updateEditTextFiltrar(view);
        return view;
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
    }

    private void filtrar(String contenidoEditText){
        ArrayList<TipoProducto> lista=new ArrayList<>();
        for (TipoProducto item:productos){
            if(item.getNombre().toLowerCase().contains(contenidoEditText.toLowerCase())){
                lista.add(item);
            }
        }
        adapter.filtrarLista(lista);
    }
    private void updateUI(ArrayList<TipoProducto> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new ProductoAdapter(m, getActivity(), R.layout.item_row_productos, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}