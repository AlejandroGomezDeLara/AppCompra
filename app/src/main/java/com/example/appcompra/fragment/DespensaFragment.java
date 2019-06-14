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
import com.example.appcompra.clases.Lista;
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
    protected ProgressBar loadingIndicator;
    protected TextView mEmptyStateTextView;
    protected Usuario usuario;
    protected Button addProductos;
    protected Button addProductosCentro;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(Singleton.getInstance().existenDespensa()){
            updateUI(Singleton.getInstance().getDespensa());
        }else{
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_DESPENSA_PETICION,QueryUtils.getUsuario().getId(),5));
        }
    }

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
        Lista li=null;
        for(Lista l:Singleton.getInstance().getListas()){
            if(l.getId()==Singleton.getInstance().getIdListaSeleccionada())
                li=l;
        }
        if(li!=null) {
            switch (li.getRol().toLowerCase()){
                case "administrador":
                    addProductos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_admin));
                    addProductosCentro.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_admin));
                    break;
                case "participante":
                    addProductos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_participante));
                    addProductosCentro.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_participante));
                    break;
                case "espectador":
                    addProductos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_espectador));
                    addProductosCentro.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_espectador));
                    break;
            }
        }
        return view;
    }
    public void intentProductos(){
        Singleton.getInstance().setPosicionSpinnerListas(0);
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(3);
    }



    public void onResume() {
        super.onResume();
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
        /*productos.clear();
        productos.addAll(m);
        */
        if(m.isEmpty()){
            addProductosCentro.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            addProductos.setVisibility(View.GONE);
        }else{
            mEmptyStateTextView.setVisibility(View.GONE);
            addProductos.setVisibility(View.VISIBLE);
            addProductosCentro.setVisibility(View.GONE);
        }
        adapter=new DespensaAdapter(m, getActivity(), R.layout.item_row_despensa, getActivity());
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