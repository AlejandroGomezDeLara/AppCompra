package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.ProductosListaViewModel;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class InteriorListaFragment extends Fragment {
    protected ArrayList<Producto> productos;
    protected RecyclerView recyclerView;
    ProgressBar loadingIndicator;
    protected DespensaAdapter adapter;
    private Usuario usuario;
    protected String idLista;
    protected TextView mEmptyStateTextView;
    protected Button addProducto;
    protected Button addProductoCentro;
    protected ProductosListaViewModel model;

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
        idLista=getActivity().getIntent().getStringExtra("id");
        model= ViewModelProviders.of(getActivity()).get(ProductosListaViewModel.class);
        addProductoCentro.setVisibility(View.GONE);
        productos = new ArrayList<>();
        usuario = QueryUtils.getUsuario();
        updateUI(productos);
        updateEditTextFiltrar(view);
        return view;
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


    public void onResume() {
        super.onResume();
        ConnectivityManager manager=(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info=manager.getActiveNetworkInfo();
        boolean isConnected=info!=null && info.isConnected();
        if(isConnected) {
            model.getProductosLista(idLista).observe(getActivity(), new Observer<ArrayList<Producto>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Producto> p) {
                    if(p!=null){
                        updateUI(p);
                    }else{
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                    }
                }
            });

        }else{
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }
}