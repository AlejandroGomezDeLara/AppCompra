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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;

public class InteriorListaFragment extends Fragment {
    protected ArrayList<Producto> productos;
    protected RecyclerView recyclerView;
    ProgressBar loadingIndicator;
    protected DespensaAdapter adapter;
    private Usuario usuario;
    protected TextView mEmptyStateTextView;
    protected Button addProducto;
    protected Button addProductoCentro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interior_lista, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView = view.findViewById(R.id.recyclerView);
        mEmptyStateTextView = view.findViewById(R.id.emptyStateView);
        mEmptyStateTextView.setVisibility(View.GONE);
        addProducto = view.findViewById(R.id.añadir_boton);
        addProductoCentro = view.findViewById(R.id.añadir_boton_centro);
        addProductoCentro.setVisibility(View.GONE);
        productos = new ArrayList<>();
        usuario = QueryUtils.getUsuario();
        rellenarProductos();
        updateUI(productos);
        updateEditTextFiltrar(view);
        return view;
    }

    public void rellenarProductos() {
        productos.add(new TipoProducto(2, "Hamburguesa", "Ingredientes", "https://image.flaticon.com/icons/png/512/93/93104.png"));
        productos.add(new TipoProducto(3, "Panes", "Ingredientes", "https://image.flaticon.com/icons/png/512/93/93104.png"));
        productos.add(new TipoProducto(4, "panecillo", "Ingredientes", "https://image.flaticon.com/icons/png/512/93/93104.png"));
        productos.add(new TipoProducto(5, "Mas cosas", "Ingredientes", "https://image.flaticon.com/icons/png/512/93/93104.png"));

    }

    private void updateEditTextFiltrar(View view) {
        EditText editText = view.findViewById(R.id.editText);
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

    private void filtrar(String contenidoEditText) {
        ArrayList<Producto> lista = new ArrayList<>();
        for (Producto item : productos) {
            if (item.getNombre().toLowerCase().contains(contenidoEditText.toLowerCase())) {
                lista.add(item);
            }
        }
        adapter.filtrarLista(lista);
    }

    private void updateUI(ArrayList<Producto> m) {
        /*productos.clear();
        productos.addAll(m);
        */
        adapter = new DespensaAdapter(m, getActivity(), R.layout.item_row_despensa, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}