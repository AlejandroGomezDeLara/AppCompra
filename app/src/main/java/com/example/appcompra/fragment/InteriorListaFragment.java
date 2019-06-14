package com.example.appcompra.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.DespensaAdapter;
import com.example.appcompra.adapters.UsuariosAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.ProductosListaViewModel;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;
import java.util.TreeSet;

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
    protected Button addProductos;
    protected Button addProductosCentro;
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
        mEmptyStateTextView.setVisibility(View.GONE);
        addProductos = view.findViewById(R.id.añadir_boton);
        addProductosCentro = view.findViewById(R.id.añadir_boton_centro);
        idLista=Singleton.getInstance().getIdListaSeleccionada();
        addProductosCentro.setVisibility(View.GONE);
        addProductos.setVisibility(View.GONE);
        productos = new ArrayList<>();
        usuario = QueryUtils.getUsuario();
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
        String usuarios="";

        for(Lista l: Singleton.getInstance().getListas()){
            if(l.getId()==idLista){
                listaActual=l;
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
            BottomNavigationView menu=((AppCompatActivity) getActivity()).findViewById(R.id.menu_bottom);
            switch (listaActual.getRol().toLowerCase()){
                case "administrador":
                    toolbar.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.gradient_lista_admin));
                    menu.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.gradient_lista_admin));
                    break;
                case "participante":
                    toolbar.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.gradient_lista_participante));
                    menu.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.gradient_lista_participante));
                    break;
                case "espectador":
                    toolbar.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.gradient_lista_espectador));
                    menu.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.gradient_lista_espectador));
                    break;
            }
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
        setColores();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    public void setColores(){
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

    private void updateUI(TreeSet<ProductoLista> m) {
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
        adapter = new DespensaAdapter(m, getActivity(), R.layout.item_row_despensa, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadingIndicator.setVisibility(View.GONE);

    }

    public void onResume() {
        super.onResume();
        if(Singleton.getInstance().existenProductosLista()){
            updateUI(Singleton.getInstance().getProductosListaLista(Singleton.getInstance().getIdListaSeleccionada()));
        }else{
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_LISTA_PETICION,QueryUtils.getUsuario().getId(),Singleton.getInstance().getIdListaSeleccionada()+"",5));
        }
        ((MainActivity)getActivity()).getProductosListaViewModel().getProductosLista().observe(getActivity(), new Observer<TreeSet<ProductoLista>>() {
            @Override
            public void onChanged(@Nullable TreeSet<ProductoLista> p) {
                if(p!=null){
                    updateUI(p);
                }
            }
        });

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
                Cambios.getInstance().añadirCambioUsuarios(u.getNombre(),"delete",u.getRol(),listaActual.getId());
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