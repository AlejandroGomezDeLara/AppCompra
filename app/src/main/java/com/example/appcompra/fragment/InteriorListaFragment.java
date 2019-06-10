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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.adapters.UsuariosAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.ProductosListaViewModel;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class InteriorListaFragment extends Fragment {

    protected ArrayList<ProductoLista> productos;
    protected RecyclerView recyclerView;
    protected ProgressBar loadingIndicator;
    protected DespensaAdapter adapter;
    protected UsuariosAdapter usuariosAdapter;
    protected Usuario usuario;
    protected int idLista;
    protected int posLista;
    protected TextView mEmptyStateTextView;
    protected Button addProducto;
    protected Button addProductoCentro;
    protected ProductosListaViewModel model;
    protected RecyclerView usuariosRecyclerView;
    protected Lista listaActual;
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
        for (int i=0;i<Singleton.getInstance().getListas().size();i++){
            if(Singleton.getInstance().getListas().get(i).getId()==idLista) {
                listaActual = Singleton.getInstance().getListas().get(i);
            }
        }
        if(listaActual!=null){
            for (int i=0;i<listaActual.getUsuarios().size();i++) {
                Usuario usuarioActual=listaActual.getUsuarios().get(i);
                if(i==listaActual.getUsuarios().size()-1){
                   usuarios+=usuarioActual.getNombre();
                }else{
                    usuarios+=usuarioActual.getNombre()+", ";
                }
            }
            Toolbar toolbar= (Toolbar)((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
            TextView titulo=toolbar.findViewById(R.id.title);
            TextView subtitle=toolbar.findViewById(R.id.subtitle);
            subtitle.setText(usuarios);
            subtitle.setVisibility(View.VISIBLE);
            titulo.setText(listaActual.getTitulo());
            LinearLayout linearToolbar=toolbar.findViewById(R.id.linearToolbar);
            linearToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usuariosPopup();
                }
            });



        }
        adapter=new DespensaAdapter();
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
        ArrayList<ProductoLista> lista = new ArrayList<>();
        for (ProductoLista item : productos) {
            if (item.getNombre().toLowerCase().contains(contenidoEditText.toLowerCase())) {
                lista.add(item);
            }
        }
        adapter.filtrarLista(lista);
    }

    private void updateUI(ArrayList<ProductoLista> m) {
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
            model.getProductosLista(idLista).observe(getActivity(), new Observer<ArrayList<ProductoLista>>() {
                @Override
                public void onChanged(@Nullable ArrayList<ProductoLista> p) {
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

    public void usuariosPopup(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_usuarios, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        usuariosRecyclerView=view.findViewById(R.id.recyclerView);
        usuariosAdapter=new UsuariosAdapter(listaActual,listaActual.getUsuarios(), getActivity(), R.layout.item_row_popup_usuarios, getContext(), new UsuariosAdapter.OnItemClickListener() {
            @Override
            public void onBorrarUsuario(Usuario u) {
                listaActual.borrarUsuario(u);
                Cambios.getInstance().addCambioUS(u.getNombre(),"delete",null,idLista);
                usuariosRecyclerView.setAdapter(usuariosAdapter);
                usuariosAdapter.notifyDataSetChanged();
            }
        });
        usuariosRecyclerView.setHasFixedSize(true);
        usuariosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        usuariosRecyclerView.setAdapter(usuariosAdapter);
        usuariosAdapter.notifyDataSetChanged();
    }
}