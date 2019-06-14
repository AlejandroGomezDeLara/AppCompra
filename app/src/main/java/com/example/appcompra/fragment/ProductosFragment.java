package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.models.CategoriaViewModel;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.models.ProductoViewModel;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ProductosFragment extends Fragment {

    protected TreeSet<Producto> productos;
    protected TreeSet<Categoria> categorias;
    protected Usuario usuario;
    protected RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected Spinner categoriasSpinner;
    protected int idCategoria;
    protected TextView mEmptyStateTextView;
    protected Button addProductoListaButton;
    protected Spinner listasSpinner;
    protected ArrayList<Integer> idListas;
    protected EditText cantidadEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        loadingIndicator=view.findViewById(R.id.loading_indicator);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        addProductoListaButton=view.findViewById(R.id.añadir_boton);
        listasSpinner=view.findViewById(R.id.listasSpinner);
        categoriasSpinner=view.findViewById(R.id.spinner_categorias);
        recyclerView=view.findViewById(R.id.recyclerView);
        cantidadEditText=view.findViewById(R.id.editTextCantidad);
        usuario=QueryUtils.getUsuario();
        productos=new TreeSet<>();
        categorias=new TreeSet<>();
        idListas=new ArrayList<>();



        if(Singleton.getInstance().existenListas())
            updateSpinnerListas(Singleton.getInstance().getListas());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(getContext(), "Ha llegado al final realizando nueva petición", Toast.LENGTH_LONG).show();
                }
            }
        });
        listasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Singleton.getInstance().setIdListaSeleccionada(idListas.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoriasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, final long id) {
                idCategoria = position + 1;
                Singleton.getInstance().setIdCategoriaSelecionada(idCategoria);
                Singleton.getInstance().setPosicionSpinnerCategorias(position);
                if(!Singleton.getInstance().existenProductosCategoriaSeleccionada()){
                    Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_CATEGORIA_PETICION,QueryUtils.getUsuario().getId(),idCategoria+"",2));
                    updateUI(new TreeSet<Producto>());
                    loadingIndicator.setVisibility(View.VISIBLE);
                }else{
                    productos=Singleton.getInstance().getProductosCategoria().get(idCategoria);
                    updateUI(productos);
                }
                mEmptyStateTextView.setVisibility(View.GONE);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        //updateEditTextFiltrar(view);
        addProductoListaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeSet<ProductoLista> productosSeleccionados=new TreeSet<>();
                for (Map.Entry<Integer, TreeSet<Producto>> entry : Singleton.getInstance().getProductosCategoria().entrySet()) {
                    for (Producto p: entry.getValue()) {
                        if(p.isSeleccionado())
                            productosSeleccionados.add(new ProductoLista(p.getId(),p.getNombre(),1,null,null,false,p.getUrl(),null,null));
                    }
                }
                for(ProductoLista p:productosSeleccionados){
                    p.setUnidades(Integer.parseInt(cantidadEditText.getText().toString()));
                }
                int idListaSeleccionada=Singleton.getInstance().getIdListaSeleccionada();

                for(ProductoLista p: productosSeleccionados) {
                    Cambios.getInstance().añadirCambioTipoProducto(p.getId(),"add",idListaSeleccionada,Integer.parseInt(cantidadEditText.getText().toString()),null,null);
                }

                Singleton.getInstance().añadirProductosLista(idListaSeleccionada,productosSeleccionados);
                Singleton.getInstance().deseleccionarProductos();
                if(Singleton.getInstance().getIdListaSeleccionada()==0){
                    Singleton.getInstance().añadirProductosDespensa(productosSeleccionados);
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(0);
                }
                else
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(5);

            }
        });
        adapter=new ProductoAdapter();
        if(!Singleton.getInstance().existenCategorias())
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.CATEGORIAS_PETICION,QueryUtils.getUsuario().getId(),3));
        return view;
    }
    private void updateSpinnerCategorias(TreeSet<Categoria> c) {
        List<String> valoresSpinner=new ArrayList<>();
        List<Categoria> ci=new ArrayList<>(c);
        for (int i=0;i<ci.size();i++){
            valoresSpinner.add(ci.get(i).getNombre());
        }
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                valoresSpinner
        );
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item,valoresSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriasSpinner.setAdapter(adapter);
        categoriasSpinner.setSelection(Singleton.getInstance().getPosicionSpinnerCategorias());
    }

    private void updateSpinnerListas(TreeSet<Lista> l) {
        List<String> valoresSpinner=new ArrayList<>();
        ArrayList<Lista> li=new ArrayList<>(l);
        idListas.clear();
        valoresSpinner.add("Despensa");
        idListas.add(0);
        for (int i=0;i<li.size();i++){
            valoresSpinner.add(li.get(i).getTitulo());
            idListas.add(li.get(i).getId());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item,valoresSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listasSpinner.setAdapter(adapter);
        if(valoresSpinner.size()>Singleton.getInstance().getPosicionSpinnerListas())
            listasSpinner.setSelection(Singleton.getInstance().getPosicionSpinnerListas());
    }

    private void updateUI(TreeSet<Producto> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new ProductoAdapter(m, getActivity(), R.layout.item_row_productos, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(m.isEmpty()){
            mEmptyStateTextView.setText("No hay productos en esa categoria");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        loadingIndicator.setVisibility(View.GONE);
    }



    public void onResume() {
        super.onResume();
        updateSpinnerListas(Singleton.getInstance().getListas());
        idCategoria=Singleton.getInstance().getPosicionSpinnerCategorias()+1;
        ((MainActivity)getActivity()).getCategoriaViewModel().getCategorias().observe(getActivity(),new Observer<TreeSet<Categoria>>(){
            @Override
            public void onChanged(@Nullable TreeSet<Categoria> c) {
                if(c!=null)
                    updateSpinnerCategorias(c);
            }
        });

        ((MainActivity) getActivity()).getModelProductos().getProductos().observe(getActivity(), new Observer<TreeSet<Producto>>() {
            @Override
            public void onChanged(@Nullable TreeSet<Producto> p) {
            if(p!=null)
                updateUI(p);
            }
        });
    }
}