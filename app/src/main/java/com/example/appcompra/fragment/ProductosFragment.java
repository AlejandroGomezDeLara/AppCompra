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

public class ProductosFragment extends Fragment {
    protected ArrayList<Producto> productos;
    private Usuario usuario;
    protected RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected PeticionProductosTask peticionTask = null;
    protected PeticionCategoriasTask categoriasTask=null;
    protected Spinner categoriasSpinner;
    QueryUtils q;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        q=new QueryUtils();
        usuario=((MainActivity)this.getActivity()).getUsuario();
        categoriasSpinner=view.findViewById(R.id.spinner_categorias);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(getContext(), "Ha llegado al final realizando nueva petición", Toast.LENGTH_LONG).show();

                }
            }
        });
        productos=new ArrayList<>();
        categoriasTask=new PeticionCategoriasTask();
        categoriasTask.execute((Void) null);
        peticionTask = new PeticionProductosTask();
        peticionTask.execute((Void) null);
        updateUI(productos);
        updateEditTextFiltrar(view);
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

        PeticionProductosTask() {
        }

        @Override
        protected ArrayList<Producto> doInBackground(Void... params) {

            socket=QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.PRODUCTOS_CATEGORIA_PETICION+Constants.SEPARATOR+"ID CATEGORIA AQUI"+Constants.SEPARATOR+"NUMERO DE PAGINADO");
                String entrada=in.readLine();
                Log.e("respuesta",entrada.split(Constants.SEPARATOR)[1]);
                if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.PRODUCTOS_CATEGORIA_RESPUESTA_CORRECTA)){
                    json=entrada.split(Constants.SEPARATOR)[1];
                    tipoProductos=q.tipoProductosJson(json,"Pescado");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            //json="{\"productos\":[{\"id\":1,\"nombre\":\"hamburguesa\",\"url\":\"https://image.flaticon.com/icons/png/512/93/93104.png\"},{\"id\":2,\"nombre\":\"patata\",\"url\":\"https://image.flaticon.com/icons/png/512/89/89421.png\"},{\"id\":3,\"nombre\":\"aceitunas\",\"url\":\"https://image.flaticon.com/icons/png/512/89/89421.png\"},{\"id\":4,\"nombre\":\"aceite\",\"url\":\"https://image.flaticon.com/icons/png/512/89/89421.png\"}]}";
            tipoProductos=q.tipoProductosJson(json,"Pescado");
            return tipoProductos;
        }

        @Override
        protected void onPostExecute(final ArrayList<Producto> tipoProductos) {
            updateUI(tipoProductos);
            Log.e("peticion","productos actualizados");
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
                out.println(Constants.CATEGORIAS_PETICION);
                String entrada=in.readLine();
                Log.e("respuesta",entrada.split(Constants.SEPARATOR)[1]);
                if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.CATEGORIAS_RESPUESTA_CORRECTA)){
                    json=entrada.split(Constants.SEPARATOR)[1];
                    categorias=q.categoriasJson(json);
                }

            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }

            //json="{\"categorias\":[{\"id\":1,\"nombre\":\"Marisco y pescado\"},{\"id\":2,\"nombre\":\"Carne\"},{\"id\":3,\"nombre\":\"Cereales\"}]}";
            categorias=q.categoriasJson(json);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                valoresSpinner
        );
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
}