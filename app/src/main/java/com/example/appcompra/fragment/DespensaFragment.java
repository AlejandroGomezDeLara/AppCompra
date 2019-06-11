package com.example.appcompra.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.DespensaViewModel;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class DespensaFragment extends Fragment {

    protected ArrayList<ProductoLista> productos;
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
        return view;
    }
    public void intentProductos(){
        Singleton.getInstance().setPosicionSpinnerListas(0);
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(3);
    }

    public void rellenarProductos(){
        productos.add(new ProductoLista(2,"Hamburguesa",2,null,null,false,"https://image.flaticon.com/icons/png/512/93/93104.png",null,null));
        productos.add(new ProductoLista(4,"Pepinos",3,null,null,false,"https://image.flaticon.com/icons/png/512/93/93104.png",null,"200g"));
        productos.add(new ProductoLista(3,"Hamburguesa",4,null,"mercadona",false,"https://image.flaticon.com/icons/png/512/93/93104.png","Gula del norte","400g"));
        productos.add(new ProductoLista(9,"Huevo",1,null,"mercadona",false,"https://image.flaticon.com/icons/png/512/93/93104.png","Gula del norte","400g"));

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

    private void updateUI(ArrayList<ProductoLista> m){
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
    @Override
    public void onPause() {
        super.onPause();

    }
}