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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appcompra.Constants;
import com.example.appcompra.LoginActivity;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ProductosFragment extends Fragment {
    protected ArrayList<Producto> productos;
    private Usuario usuario;
    protected RecyclerView recyclerView;
    protected ProductoAdapter adapter;
    ProgressBar loadingIndicator;
    PeticionProductosTask peticionTask = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        usuario=((MainActivity)this.getActivity()).getUsuario();
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
    public class PeticionProductosTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        PeticionProductosTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            socket=usuario.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                //petición al servidor
                out.println(Constants.PRODUCTOS_PETICION+Constants.SEPARATOR+"ID CATEGORIA AQUI"+Constants.SEPARATOR+"NUMERO DE PAGINADO");
                productos=procesarJson(in.readLine());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                Log.e("xd","Productos actualizados");
            }else{
                Log.e("xd","Error al actualizar los productos");
            }
        }

        @Override
        protected void onCancelled() {

        }

        protected ArrayList<Producto> procesarJson(String entrada){

            //procesar de x forma los productos

            return new ArrayList<Producto>();
        }
    }


}