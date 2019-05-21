package com.example.appcompra.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.appcompra.Constants;
import com.example.appcompra.LoginActivity;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Producto;
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

public class ProductosFragment extends Fragment {
    protected ArrayList<Producto> productos;
    protected ArrayList<Categoria> categorias;
    private Usuario usuario;
    protected RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected PeticionProductosTask peticionTask = null;
    protected PeticionCategoriasTask categoriasTask=null;
    protected Spinner categoriasSpinner;
    protected int idCategoria;
    QueryUtils q;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        loadingIndicator=view.findViewById(R.id.loading_indicator);
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
        pedirCategorias();
        categoriasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idCategoria=position+1;
                updateUI(new ArrayList<Producto>());
                loadingIndicator.setVisibility(View.VISIBLE);
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
        categorias=Singleton.getInstance().getCategorias();
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
    public class PeticionProductosTask extends AsyncTask<Void, Void, ArrayList<Producto>> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ArrayList<Producto> tipoProductos=new ArrayList<>();
        private String json;
        private int idCategoria;
        PeticionProductosTask(int idCategoria) {
            this.idCategoria=idCategoria;
        }

        @Override
        protected ArrayList<Producto> doInBackground(Void... params) {

            socket=QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.PRODUCTOS_CATEGORIA_PETICION+Constants.SEPARATOR+usuario.getId()+Constants.SEPARATOR+idCategoria);
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
                        Singleton.getInstance().getUltimosProductos().put(idCategoria,q.tipoProductosJson(json,nombreCategoria));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return tipoProductos;
        }

        @Override
        protected void onPostExecute(final ArrayList<Producto> tipoProductos) {
            loadingIndicator.setVisibility(View.GONE);
            if(!Singleton.getInstance().getUltimosProductos().containsKey(idCategoria)){
                Toast.makeText(getContext(), "No hay productos", Toast.LENGTH_LONG).show();
            }else{
                updateUI(Singleton.getInstance().getUltimosProductos().get(idCategoria));
            }
        }

        @Override
        protected void onCancelled() {

        }


    }
    public class PeticionCategoriasTask extends AsyncTask<Void, Void, ArrayList<Categoria>> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private ArrayList<Categoria> categorias=new ArrayList<>();
        PeticionCategoriasTask() {
        }

        @Override
        protected ArrayList<Categoria> doInBackground(Void... params) {

            socket=QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.CATEGORIAS_PETICION+Constants.SEPARATOR+usuario.getId());
                String entrada=in.readLine();
                if(entrada!=null && !entrada.isEmpty()) {
                    Log.e("respuesta", entrada.split(Constants.SEPARATOR)[1]);
                    if (entrada.split(Constants.SEPARATOR)[0].equals(Constants.CATEGORIAS_RESPUESTA_CORRECTA)) {
                        json = entrada.split(Constants.SEPARATOR)[1];
                        categorias = q.categoriasJson(json);
                        Singleton.getInstance().setCategorias(categorias);
                    }
                }
            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }
            return categorias;
        }

        @Override
        protected void onPostExecute(final ArrayList<Categoria> categorias) {
            updateSpinner(categorias);
        }

        @Override
        protected void onCancelled() {

        }


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


    private void pedirProductos(int idCategoria){
        peticionTask = new PeticionProductosTask(idCategoria);
        peticionTask.execute((Void) null);
    }
    private void pedirCategorias(){
        if(!Singleton.getInstance().existenCategorias()){
            categoriasTask=new PeticionCategoriasTask();
            categoriasTask.execute((Void) null);
        }else{
            updateSpinner(Singleton.getInstance().getCategorias());
        }

    }

    private void actualizar(){
        pedirCategorias();
        pedirProductos(idCategoria);
    }

}