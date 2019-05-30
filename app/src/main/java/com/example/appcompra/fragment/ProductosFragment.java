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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ProductosFragment extends Fragment {

    protected ArrayList<Producto> productos;
    protected ArrayList<Categoria> categorias;
    protected Usuario usuario;
    protected RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected Spinner categoriasSpinner;
    protected int idCategoria;
    protected TextView mEmptyStateTextView;
    protected ProductoViewModel modelProductos;
    protected CategoriaViewModel categoriaViewModel;
    protected PeticionProductosTask peticionTask = null;
    protected PeticionProductosTaskTest peticionTaskTest = null;
    protected Button addProductoListaButton;
    protected Spinner listasSpinner;
    protected ArrayList<Integer> idListas;

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
        usuario=QueryUtils.getUsuario();
        productos=new ArrayList<>();
        categorias=new ArrayList<>();
        idListas=new ArrayList<>();
        updateSpinnerCategorias(categorias);
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
        categoriaViewModel= ViewModelProviders.of(getActivity()).get(CategoriaViewModel.class);
        modelProductos= ViewModelProviders.of(getActivity()).get(ProductoViewModel.class);
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
                updateUI(new ArrayList<Producto>());
                loadingIndicator.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setVisibility(View.GONE);
                Singleton.getInstance().setPosicionSpinnerCategorias(position);
                if(!Singleton.getInstance().getUltimosProductos().containsKey(idCategoria)){
                    pedirProductos(idCategoria);
                }else{
                    loadingIndicator.setVisibility(View.GONE);
                    updateUI(Singleton.getInstance().getUltimosProductos().get(idCategoria));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        //updateEditTextFiltrar(view);
        addProductoListaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkedList<ProductoLista> productosSeleccionados=new LinkedList<>();
                for (Map.Entry<Integer, ArrayList<Producto>> entry : Singleton.getInstance().getUltimosProductos().entrySet()) {
                    for (Producto p: entry.getValue()) {
                        if(p.isSeleccionado())
                            productosSeleccionados.add(new ProductoLista(p.getId(),p.getNombre(),0,null,null,false,p.getUrl(),null,null));
                    }
                }
                for (int i=0;i<productosSeleccionados.size();i++){
                    ProductoLista p=productosSeleccionados.get(i);
                    Cambios.getInstance().addCambioTP(p.getId(),"add",Singleton.getInstance().getIdListaSeleccionada());
                }
                Singleton.getInstance().añadirProductosLista(Singleton.getInstance().getIdListaSeleccionada(),productosSeleccionados);
                if(Singleton.getInstance().getIdListaSeleccionada()==0)
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(0);
                else
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(5);
            }
        });
        return view;
    }
    private void updateSpinnerCategorias(ArrayList<Categoria> c) {
        List<String> valoresSpinner=new ArrayList<>();
        for (int i=0;i<c.size();i++){
            valoresSpinner.add(c.get(i).getNombre());
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
    private void updateSpinnerListas(ArrayList<Lista> l) {
        List<String> valoresSpinner=new ArrayList<>();
        idListas.clear();
        valoresSpinner.add("Despensa");
        idListas.add(0);
        for (int i=0;i<l.size();i++){
            valoresSpinner.add(l.get(i).getTitulo());
            idListas.add(l.get(i).getId());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item,valoresSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listasSpinner.setAdapter(adapter);
        listasSpinner.setSelection(Singleton.getInstance().getPosicionSpinnerListas());
    }
    private void updateUI(ArrayList<Producto> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new ProductoAdapter(m, getActivity(), R.layout.item_row_productos, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
    public void pedirProductos(int idCategoria) {
        peticionTaskTest = new PeticionProductosTaskTest(idCategoria);
        peticionTaskTest.execute((Void) null);
        /*
        peticionTask = new PeticionProductosTask(idCategoria);
        peticionTask.execute((Void) null);*/
    }
    public class PeticionProductosTask extends AsyncTask<Void, Void, ArrayList<Producto>> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private int idCategoria;
        private ArrayList<Producto> p=new ArrayList<>();

        PeticionProductosTask(int idCategoria) {
            this.idCategoria=idCategoria;
        }

        @Override
        protected ArrayList<Producto> doInBackground(Void... params) {
            socket=QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.PRODUCTOS_CATEGORIA_PETICION+Constants.SEPARATOR+QueryUtils.getUsuario().getId()+Constants.SEPARATOR+idCategoria);
                String entrada=in.readLine();
                if(entrada!=null && !entrada.isEmpty()){
                    Log.e("respuesta",entrada.split(Constants.SEPARATOR)[1]);
                    if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.PRODUCTOS_CATEGORIA_RESPUESTA_CORRECTA)){
                        json=entrada.split(Constants.SEPARATOR)[1];
                        p=QueryUtils.tipoProductosJson(json);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return p;
        }

        @Override
        protected void onPostExecute(final ArrayList<Producto> tipoProductos) {
            if(!tipoProductos.isEmpty()){
                if(!Singleton.getInstance().getUltimosProductos().containsKey(idCategoria)){
                    Singleton.getInstance().añadirNuevosProductos(idCategoria,tipoProductos);
                }
                updateUI(tipoProductos);
            }else{
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }
            loadingIndicator.setVisibility(View.GONE);

        }
        @Override
        protected void onCancelled() {
        }
    }
    public class PeticionProductosTaskTest extends AsyncTask<Void, Void, ArrayList<Producto>> {

        private String json;
        private int idCategoria;
        private ArrayList<Producto> p=new ArrayList<>();

        PeticionProductosTaskTest(int idCategoria) {
            this.idCategoria=idCategoria;
        }

        @Override
        protected ArrayList<Producto> doInBackground(Void... params) {
            json= Constants.DUMMY_PRODUCTOS_CATEGORIA_BODEGA;
            p=QueryUtils.tipoProductosJson(json);
            return p;
        }

        @Override
        protected void onPostExecute(final ArrayList<Producto> tipoProductos) {
            if(!tipoProductos.isEmpty()){
                if(!Singleton.getInstance().getUltimosProductos().containsKey(idCategoria)){
                    Singleton.getInstance().añadirNuevosProductos(idCategoria,tipoProductos);
                }
                updateUI(tipoProductos);
            }else{
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }
            loadingIndicator.setVisibility(View.GONE);

        }
        @Override
        protected void onCancelled() {
        }
    }

    private void updateEditTextFiltrar(View view){
        EditText editText=view.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });
    }
    private void filtrar(String contenidoEditText){
        ArrayList<Producto> lista=new ArrayList<>();
        for (Producto item:productos){
            if(item.getNombre().toLowerCase().contains(contenidoEditText.toLowerCase())){
                lista.add(item);
            }
        }
        adapter.filtrarLista(lista);
    }
    public void onResume() {
        super.onResume();
        ConnectivityManager manager=(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info=manager.getActiveNetworkInfo();
        boolean isConnected=info!=null && info.isConnected();
        updateSpinnerListas(Singleton.getInstance().getListas());
        if(isConnected) {
            categoriaViewModel.getCategorias().observe(getActivity(), new Observer<ArrayList<Categoria>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Categoria> categorias) {
                    if(categorias!=null){
                        updateSpinnerCategorias(categorias);
                    }
                }
            });

            modelProductos.getProductos().observe(getActivity(), new Observer<ArrayList<Producto>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Producto> p) {
                    if (p != null) {
                        updateUI(p);
                    }else{
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                    }
                }
            });

        }else{
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

}