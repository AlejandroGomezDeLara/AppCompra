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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcompra.Constants;
import com.example.appcompra.R;
import com.example.appcompra.adapters.ListaAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.models.ListasViewModel;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ListasFragment extends Fragment {

    protected ArrayList<Lista> listas;
    protected RecyclerView recyclerView;
    protected ListaAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected Usuario usuario;
    protected Button addLista;
    protected Button addListaCentro;
    protected ListasViewModel model;
    protected String nombreNuevaLista;
    protected TextView mEmptyStateTextView;
    protected PeticionListasTask listasTask=null;
    protected PeticionNuevaListaTask nuevaListaTask=null;
    protected BorrarListaTask borrarListaTask=null;
    protected Lista listaSeleccionada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView=view.findViewById(R.id.recyclerView);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        model= ViewModelProviders.of(getActivity()).get(ListasViewModel.class);
        listas=new ArrayList<>();
        usuario=QueryUtils.getUsuario();
        if(!Singleton.getInstance().existenListas()){
            listasTask=new PeticionListasTask();
            listasTask.execute((Void) null);
        }else{
            updateUI(Singleton.getInstance().getListas());
        }
        //updateEditTextFiltrar(view);
        addLista=view.findViewById(R.id.añadir_boton);
        addListaCentro=view.findViewById(R.id.añadir_boton);
        addLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaListaPopup();
            }
        });
        addListaCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaListaPopup();
            }
        });
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
        ConnectivityManager manager=(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info=manager.getActiveNetworkInfo();
        boolean isConnected=info!=null && info.isConnected();
        if(isConnected) {
            model.getListas().observe(getActivity(), new Observer<ArrayList<Lista>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Lista> listas) {
                    if(listas!=null){
                        updateUI(listas);

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
        adapter=new ListaAdapter(m, getActivity(), R.layout.item_row_listas, getActivity(), new ListaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Lista l) {
                borrarListaPopup();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mEmptyStateTextView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        addListaCentro.setVisibility(View.GONE);
        addLista.setVisibility(View.VISIBLE);
    }

    public void crearNuevaListaPopup(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_crear_lista, null);
        final AutoCompleteTextView editText=view.findViewById(R.id.crear_lista_editText);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);

        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreNuevaLista=editText.getText().toString();
                nuevaListaTask=new PeticionNuevaListaTask();
                nuevaListaTask.execute((Void) null);
                dialog.dismiss();
            }
        });
    }
    public void borrarListaPopup(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_confirmacion, null);
        final AutoCompleteTextView editText=view.findViewById(R.id.crear_lista_editText);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp=view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarListaTask=new BorrarListaTask();
                borrarListaTask.execute((Void) null);
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
                    Singleton.getInstance().setListas(listas);
                }

            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }

            return listas;
        }

        @Override
        protected void onPostExecute(final ArrayList<Lista> listas) {
            if(!Singleton.getInstance().existenListas()){
                Toast.makeText(getContext(), "No hay listas", Toast.LENGTH_LONG).show();
            }else{
                updateUI(listas);
            }
            loadingIndicator.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {

        }


    }
    public class PeticionNuevaListaTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private Lista lista;
        private Boolean listaCreada;

        PeticionNuevaListaTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            socket= QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.CREACION_NUEVA_LISTA+Constants.SEPARATOR+usuario.getId()+Constants.SEPARATOR+nombreNuevaLista+Constants.SEPARATOR);
                String entrada=in.readLine();
                Log.e("respuesta",entrada.split(Constants.SEPARATOR)[1]);
                if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.CREACION_NUEVA_LISTA_CORRECTA)){
                    json=entrada.split(Constants.SEPARATOR)[1];
                    lista=new Lista(Integer.parseInt(entrada.split(Constants.SEPARATOR)[1]),nombreNuevaLista);
                    Singleton.getInstance().añadirNuevaLista(lista);
                }else{
                    listaCreada=false;
                }

            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }

            return listaCreada;
        }

        @Override
        protected void onPostExecute(final Boolean creada) {
            if(creada)
                updateUI(Singleton.getInstance().getListas());
            else{
                Toast.makeText(getContext(), "No se ha podido crear la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }


    }
    public class BorrarListaTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private boolean peticion;

        BorrarListaTask() {
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            socket= QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.BORRAR_LISTA_PETICION);
                Singleton.getInstance().borrarLista(listaSeleccionada);

            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }

            return peticion;
        }

        @Override
        protected void onPostExecute(final Boolean creada) {
            if(creada)
                updateUI(Singleton.getInstance().getListas());
            else{
                Toast.makeText(getContext(), "No se ha podido borrar la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}