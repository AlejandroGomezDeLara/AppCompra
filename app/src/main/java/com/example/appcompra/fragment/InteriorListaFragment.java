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
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.TreeSet;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class InteriorListaFragment extends Fragment {

    protected ArrayList<ProductoLista> productos;
    protected RecyclerView recyclerView;
    protected ProgressBar loadingIndicator;
    protected DespensaAdapter adapter;
    protected UsuariosAdapter usuariosAdapter;
    protected int idLista;
    protected int posLista;
    protected TextView mEmptyStateTextView;
    protected Button addProductos;
    protected Button addProductosCentro;
    protected ImageView deleteProductos;
    protected ImageView markProductos;
    protected ImageView vincular;
    protected ProductosListaViewModel model;
    protected RecyclerView usuariosRecyclerView;
    protected Lista listaActual;
    private SwipeRefreshLayout refreshLayout;
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
        deleteProductos=view.findViewById(R.id.eliminar_boton);
        markProductos=view.findViewById(R.id.mark_boton);
        idLista=Singleton.getInstance().getIdListaSeleccionada();
        addProductosCentro.setVisibility(View.GONE);
        addProductos.setVisibility(View.GONE);
        productos = new ArrayList<>();
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
        if(Singleton.getInstance().hayProductosListaSeleccionados())
            Singleton.getInstance().borrarProductosSeleccionados();
        deleteProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(ProductoLista p: Singleton.getInstance().getProductosListaSeleccionados()){
                    Cambios.getInstance().añadirCambioTipoProducto(p.getId(),"delete",listaActual.getId(),0,0,0,listaActual.getTitulo());
                    Singleton.getInstance().borrarProductosSeleccionados();
                    updateUI(Singleton.getInstance().getProductosListaLista(Singleton.getInstance().getIdListaSeleccionada()));
                }
            }
        });

        markProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(ProductoLista p: Singleton.getInstance().getProductosListaSeleccionados()){
                    if(p.isComprado())
                        Cambios.getInstance().añadirCambioTipoProducto(p.getId(),"unmark",listaActual.getId(),0,0,p.getReceta(),listaActual.getTitulo());
                    else
                        Cambios.getInstance().añadirCambioTipoProducto(p.getId(),"mark",listaActual.getId(),0,0,p.getReceta(),listaActual.getTitulo());
                    p.setComprado(!p.isComprado());
                }
                Singleton.getInstance().limpiarProductosSeleccionados();
                updateUI(Singleton.getInstance().getProductosListaLista(Singleton.getInstance().getIdListaSeleccionada()));
            }
        });


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
                    addProductos.setVisibility(View.GONE);
                    addProductosCentro.setVisibility(View.GONE);
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
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Singleton.getInstance().limpiarProductosLista();
                        Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_LISTA_PETICION,QueryUtils.getUsuario().getId(),Singleton.getInstance().getIdListaSeleccionada()+"",5));
                        refreshLayout.setRefreshing(false);
                    }
                }
        );
        adapter=new DespensaAdapter();
        updateEditTextFiltrar(view);
        setColores();
        if(listaActual!=null){
            Singleton.getInstance().getEditor().putInt("idUltimaLista",listaActual.getId());
            Singleton.getInstance().getEditor().putString("nombreUltimaLista",listaActual.getTitulo());
            Singleton.getInstance().getEditor().putString("rolUltimaLista",listaActual.getRol());
            Singleton.getInstance().getEditor().putInt("usuariosUltimaLista",listaActual.getNumeroUsuarios());
            Singleton.getInstance().getEditor().apply();
        }
        Toolbar toolbar= (Toolbar)((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        vincular=toolbar.findViewById(R.id.vincular);
        vincular=toolbar.findViewById(R.id.vincular);
        vincular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vincularConfirmacionPopup();
            }
        });
        vincular.setVisibility(View.VISIBLE);
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
                    deleteProductos.setBackgroundResource(R.drawable.shape_admin);
                    markProductos.setBackgroundResource(R.drawable.shape_admin);
                    break;
                case "participante":
                    addProductos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_participante));
                    addProductosCentro.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_participante));
                    deleteProductos.setBackgroundResource(R.drawable.shape_participante);
                    markProductos.setBackgroundResource(R.drawable.shape_participante);
                    break;
                case "espectador":
                    addProductos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_espectador));
                    addProductosCentro.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_espectador));
                    deleteProductos.setBackgroundResource(R.drawable.shape_espectador);
                    markProductos.setBackgroundResource(R.drawable.shape_espectador);
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
        productos.clear();
        productos.addAll(m);
        if(!listaActual.getRol().toLowerCase().equals("espectador")) {


            if (m.isEmpty()) {
                addProductosCentro.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                addProductos.setVisibility(View.GONE);
            } else {
                mEmptyStateTextView.setVisibility(View.GONE);
                addProductos.setVisibility(View.VISIBLE);
                addProductosCentro.setVisibility(View.GONE);
            }
        }else{
            addProductos.setVisibility(View.GONE);
            addProductosCentro.setVisibility(View.GONE);
        }
        adapter = new DespensaAdapter(m, getActivity(), R.layout.item_row_despensa, getActivity(), new DespensaAdapter.OnItemClickListener() {
            @Override
            public void onSeleccionarLista() {
                Log.e("xd",Singleton.getInstance().hayProductosListaSeleccionados()+"");
                if(!listaActual.getRol().toLowerCase().equals("espectador")) {
                    if (Singleton.getInstance().hayProductosListaSeleccionados()) {
                        deleteProductos.setVisibility(View.VISIBLE);
                        markProductos.setVisibility(View.VISIBLE);
                        addProductos.setVisibility(View.GONE);
                    } else {
                        deleteProductos.setVisibility(View.GONE);
                        markProductos.setVisibility(View.GONE);
                        addProductos.setVisibility(View.VISIBLE);
                    }
                }else{
                    deleteProductos.setVisibility(View.GONE);
                    markProductos.setVisibility(View.GONE);
                    addProductos.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadingIndicator.setVisibility(View.GONE);

    }

    public void onResume() {
        super.onResume();
        if(Cambios.getInstance().existenCambios()){
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.ENVIAR_NOTIFICACIONES,QueryUtils.getUsuario().getId(),Cambios.getInstance().getCambiosString(),1));
        }
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_LISTA_PETICION,QueryUtils.getUsuario().getId(),Singleton.getInstance().getIdListaSeleccionada()+"",5));

        if(Singleton.getInstance().existenProductosLista()){
            updateUI(Singleton.getInstance().getProductosListaLista(Singleton.getInstance().getIdListaSeleccionada()));
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

        vincular.setVisibility(View.GONE);
        super.onPause();
    }

    public void usuariosPopup(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_usuarios, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        ImageView compartir=view.findViewById(R.id.compartir);
        usuariosRecyclerView=view.findViewById(R.id.recyclerView);
        usuariosAdapter=new UsuariosAdapter(listaActual,listaActual.getUsuarios(), getActivity(), R.layout.item_row_popup_usuarios, getContext(), new UsuariosAdapter.OnItemClickListener() {
            @Override
            public void onBorrarUsuario(Usuario u) {
                listaActual.borrarUsuario(u);
                Cambios.getInstance().añadirCambioUsuarios(u.getNombre(),"delete",u.getRol(),listaActual.getId(),listaActual.getTitulo());
                usuariosRecyclerView.setAdapter(usuariosAdapter);
                usuariosAdapter.notifyDataSetChanged();
            }
        });
        usuariosRecyclerView.setHasFixedSize(true);
        usuariosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        usuariosRecyclerView.setAdapter(usuariosAdapter);
        if(!listaActual.getRol().toLowerCase().equals("administrador")){
            compartir.setVisibility(View.GONE);
        }
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartirListaPopup(listaActual);
            }
        });
        usuariosAdapter.notifyDataSetChanged();
    }
    public void compartirListaPopup( Lista l){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_compartir, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final AutoCompleteTextView editText=view.findViewById(R.id.editText);
        final Spinner spinnerRoles=view.findViewById(R.id.spinnerRoles);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item,Singleton.getInstance().getRoles()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        listaActual=l;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp=view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rolElegido=spinnerRoles.getSelectedItem().toString();
                String nombreUsuario=editText.getText().toString();
                if(!nombreUsuario.isEmpty()){
                    if(!rolElegido.equals("Ninguno") && !nombreUsuario.isEmpty()) {
                        Singleton.getInstance().enviarPeticion(new Peticion(Constants.COMPARTIR_LISTA_PETICION,QueryUtils.getUsuario().getId(),listaActual.getId()+Constants.SEPARATOR+nombreUsuario+Constants.SEPARATOR+rolElegido.toLowerCase(),10));
                        listaActual.añadirUsuario(new Usuario(nombreUsuario,rolElegido));
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getContext(),"Elige un rol para el usuario",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Introduce algún nombre",Toast.LENGTH_LONG).show();
                }
            }
        });
        botonCancelarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void vincularConfirmacionPopup(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_confirmacion, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        TextView texto=view.findViewById(R.id.texto_popup);
        texto.setText("¿Quieres vincular esta lista a tu despensa?");
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp=view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().enviarPeticion(new Peticion(Constants.VINCULAR_LISTA_PETICION,QueryUtils.getUsuario().getId(),idLista+"",10));
                dialog.dismiss();
            }
        });
        botonCancelarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}