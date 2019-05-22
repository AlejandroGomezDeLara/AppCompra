package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcompra.Constants;
import com.example.appcompra.LoginActivity;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.CategoriaViewModel;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoViewModel;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ProductosFragment extends Fragment {
    protected ArrayList<Producto> productos;
    protected ArrayList<Categoria> categorias;
    private Usuario usuario;
    protected RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected Spinner categoriasSpinner;
    protected int idCategoria;
    protected TextView mEmptyStateTextView;
    protected ProductoViewModel modelProductos;
    protected CategoriaViewModel categoriaViewModel;
    protected PeticionProductosTask peticionTask = null;
    QueryUtils q;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        loadingIndicator=view.findViewById(R.id.loading_indicator);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        idCategoria=9;
        q=new QueryUtils();
        usuario=((MainActivity)this.getActivity()).getUsuario();
        categoriasSpinner=view.findViewById(R.id.spinner_categorias);
        recyclerView=view.findViewById(R.id.recyclerView);
        productos=new ArrayList<>();
        categorias=new ArrayList<>();
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

        categoriasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, final long id) {
                idCategoria = position + 1;
                updateUI(new ArrayList<Producto>());
                loadingIndicator.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setVisibility(View.GONE);
                Singleton.getInstance().setPosicionSpinner(position);
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

        return view;
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

    public void onResume() {
        super.onResume();
        ConnectivityManager manager=(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info=manager.getActiveNetworkInfo();
        boolean isConnected=info!=null && info.isConnected();
        if(isConnected) {
            categoriaViewModel.getCategorias().observe(getActivity(), new Observer<ArrayList<Categoria>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Categoria> categorias) {
                    if(categorias!=null){
                        updateSpinner(categorias);
                    }
                }
            });

            modelProductos.getProductos().observe(getActivity(), new Observer<ArrayList<Producto>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Producto> p) {
                    if (p != null) {
                        updateUI(p);
                    }
                }
            });

        }else{
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
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

    private void updateSpinner(ArrayList<Categoria> c) {
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
        categoriasSpinner.setSelection(Singleton.getInstance().getPosicionSpinner());
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
        peticionTask = new PeticionProductosTask(idCategoria);
        peticionTask.execute((Void) null);
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
                        String nombreCategoria="";
                        for (Categoria cat : Singleton.getInstance().getCategorias())
                        {
                            if(cat.getId()==idCategoria){
                                nombreCategoria=cat.getNombre();
                            }
                        }
                        p=QueryUtils.tipoProductosJson(json,nombreCategoria);
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

}