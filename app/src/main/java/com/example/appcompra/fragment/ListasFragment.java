package com.example.appcompra.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.ListaAdapter;
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.TipoProducto;
import com.example.appcompra.adapters.ProductoAdapter;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListasFragment extends Fragment {
    protected ArrayList<Lista> listas;
    protected RecyclerView recyclerView;
    protected ListaAdapter adapter;
    ProgressBar loadingIndicator;
    private Usuario usuario;
    protected PeticionListasTask listasTask=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView=view.findViewById(R.id.recyclerView);
        listas=new ArrayList<>();
        usuario=((MainActivity)this.getActivity()).getUsuario();
        listasTask=new PeticionListasTask();
        listasTask.execute((Void) null);
        updateEditTextFiltrar(view);
        return view;
    }

    private void updateEditTextFiltrar(View view){
        EditText editText=view.findViewById(R.id.editText);
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

    public void onResume() {
        super.onResume();
    }

    private void filtrar(String contenidoEditText){
        ArrayList<Lista> listass=new ArrayList<>();
        for (Lista item:listas){
            if(item.getTitulo().toLowerCase().contains(contenidoEditText.toLowerCase())){
                listass.add(item);
            }
        }
        adapter.filtrarLista(listass);
    }
    private void updateUI(ArrayList<Lista> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new ListaAdapter(m, getActivity(), R.layout.item_row_listas, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class PeticionListasTask extends AsyncTask<Void, Void, ArrayList<Lista>> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private ArrayList<Lista> listas=new ArrayList<>();

        PeticionListasTask() {
        }

        @Override
        protected ArrayList<Lista> doInBackground(Void... params) {

            socket= QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.LISTAS_PETICION+Constants.SEPARATOR+usuario.getId());
                String entrada=in.readLine();
                Log.e("respuesta",entrada.split(Constants.SEPARATOR)[1]);
                if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.LISTAS_RESPUESTA_CORRECTA)){
                    json=entrada.split(Constants.SEPARATOR)[1];
                    listas=QueryUtils.listasJson(json);
                }

            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }

            return listas;
        }

        @Override
        protected void onPostExecute(final ArrayList<Lista> listas) {
            updateUI(listas);
        }

        @Override
        protected void onCancelled() {

        }


    }

}