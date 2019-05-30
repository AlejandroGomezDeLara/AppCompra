package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.ProductosListaViewModel;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class InteriorListaFragment extends Fragment {

    protected ArrayList<Producto> productos;
    protected RecyclerView recyclerView;
    protected ProgressBar loadingIndicator;
    protected DespensaAdapter adapter;
    protected Usuario usuario;
    protected int idLista;
    protected int posLista;
    protected TextView mEmptyStateTextView;
    protected Button addProducto;
    protected Button addProductoCentro;
    protected ProductosListaViewModel model;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interior_lista, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView = view.findViewById(R.id.recyclerView);
        mEmptyStateTextView = view.findViewById(R.id.emptyStateView);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        addProducto = view.findViewById(R.id.añadir_boton);
        addProductoCentro = view.findViewById(R.id.añadir_boton_centro);
        idLista=Singleton.getInstance().getIdListaSeleccionada();
        model= ViewModelProviders.of(getActivity()).get(ProductosListaViewModel.class);
        addProductoCentro.setVisibility(View.VISIBLE);
        addProducto.setVisibility(View.GONE);
        productos = new ArrayList<>();
        usuario = QueryUtils.getUsuario();
        addProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentProductos();
            }
        });
        addProductoCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentProductos();
            }
        });
        String usuarios="";
        Lista listaActual=null;
        for (int i=0;i<Singleton.getInstance().getListas().size();i++){
            if(Singleton.getInstance().getListas().get(i).getId()==idLista) {
                listaActual = Singleton.getInstance().getListas().get(i);
            }
        }
        if(listaActual!=null){
            ArrayList<String> usuariosLista=listaActual.getUsuarios();
            for (int i=0;i<usuariosLista.size();i++) {
                if(i==usuariosLista.size()-1){
                   usuarios+=usuariosLista.get(i);
                }else{
                    usuarios+=usuariosLista.get(i)+", ";
                }
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(listaActual.getTitulo());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(usuarios);




        }
        updateEditTextFiltrar(view);
        return view;
    }
    public void intentProductos(){
        Singleton.getInstance().setPosicionSpinnerListas(Singleton.getInstance().getPosicionSpinnerListas());
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(3);
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
        addProducto.setVisibility(View.VISIBLE);
        addProductoCentro.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setVisibility(View.GONE);

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

    @Override
    public void onPause() {
        super.onPause();
    }

}