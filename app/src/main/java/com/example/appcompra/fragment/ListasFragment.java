package com.example.appcompra.fragment;

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
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.ListaAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.Usuario;

import java.util.ArrayList;
import java.util.LinkedList;

public class ListasFragment extends Fragment {
    protected ArrayList<Lista> listas;
    protected RecyclerView recyclerView;
    protected ListaAdapter adapter;
    ProgressBar loadingIndicator;
    private Usuario usuario;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView=view.findViewById(R.id.recyclerView);
        listas=new ArrayList<>();
        usuario=((MainActivity)this.getActivity()).getUsuario();
        rellenarListas();
        updateUI(listas);
        updateEditTextFiltrar(view);
        return view;
    }
    public void rellenarListas(){

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
        ArrayList<Lista> listass=new ArrayList<>();
        for (Lista item:listas){
            if(item.getTitulo().toLowerCase().contains(contenidoEditText.toLowerCase())){
                listass.add(item);
            }
        }
        adapter.filtrarLista(listass);
    }
    private void updateUI(ArrayList<Lista> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new ListaAdapter(m, getActivity(), R.layout.item_row_listas, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}