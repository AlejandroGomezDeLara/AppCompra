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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.ListaAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.models.ListasViewModel;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    protected PeticionListasTaskTest test=null;
    protected PeticionNuevaListaTask nuevaListaTask=null;
    protected PeticionNuevaListaTaskTest nuevaListaTaskTest=null;
    protected BorrarListaTask borrarListaTask=null;
    protected BorrarListaTaskTest borrarListaTaskTest=null;
    protected CompartirListaTask compartirListaTask=null;
    protected CompartirListaTaskTest compartirListaTaskTest=null;
    protected Lista listaSeleccionada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView=view.findViewById(R.id.recyclerView);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        model= ViewModelProviders.of(getActivity()).get(ListasViewModel.class);
        listas=new ArrayList<>();
        usuario=QueryUtils.getUsuario();
        if(!Singleton.getInstance().existenListas()){
            /*listasTask=new PeticionListasTask();
            listasTask.execute((Void) null);*/
            test=new PeticionListasTaskTest();
            test.execute((Void) null);
        }else{
            updateUI(Singleton.getInstance().getListas());
        }

        //updateEditTextFiltrar(view);
        addLista=view.findViewById(R.id.añadir_boton);
        addListaCentro=view.findViewById(R.id.añadir_boton_centro);
        addLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaListaPopup();
            }
        });
        addListaCentro.setVisibility(View.GONE);
        addLista.setVisibility(View.GONE);
        mEmptyStateTextView.setVisibility(View.GONE);
        addListaCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaListaPopup();
            }
        });
        adapter=new ListaAdapter();
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
        ConnectivityManager manager=(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info=manager.getActiveNetworkInfo();
        boolean isConnected=info!=null && info.isConnected();
        if(listas==null){
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            addListaCentro.setVisibility(View.VISIBLE);
            addLista.setVisibility(View.GONE);

        }else{
            mEmptyStateTextView.setVisibility(View.GONE);
            addListaCentro.setVisibility(View.GONE);
            addLista.setVisibility(View.VISIBLE);
        }
        if(isConnected) {
            model.getListas().observe(getActivity(), new Observer<ArrayList<Lista>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Lista> l) {
                    if(l!=null){
                        updateUI(l);
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

    public void updateUI(ArrayList<Lista> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new ListaAdapter(m, getActivity(), R.layout.item_row_listas, getActivity(), new ListaAdapter.OnItemClickListener() {
            @Override
            public void onBorrarLista(Lista l) {
                borrarListaPopup(l);
            }

            @Override
            public void onCompartirLista(Lista l) {
                compartirListaPopup(l);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void crearNuevaListaPopup(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_crear_lista, null);
        final AutoCompleteTextView editText=view.findViewById(R.id.crear_lista_editText);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreNuevaLista=editText.getText().toString();
                if(!nombreNuevaLista.isEmpty()){
                    //peticionNuevaLista();
                    peticionNuevaListaTest();
                    dialog.dismiss();
                }else {
                    Toast.makeText(getContext(),"Introduce un nombre para la lista",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void borrarListaPopup( Lista l){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_confirmacion, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        listaSeleccionada=l;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp=view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //peticionBorrarLista();
                peticionBorrarListaTest();
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
        listaSeleccionada=l;
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
                        peticionCompartirListaTest(nombreUsuario,rolElegido);
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
    public void peticionNuevaLista(){
        nuevaListaTask=new PeticionNuevaListaTask();
        nuevaListaTask.execute((Void) null);
    }
    public void peticionNuevaListaTest(){
        nuevaListaTaskTest=new PeticionNuevaListaTaskTest();
        nuevaListaTaskTest.execute((Void) null);
    }
    public void peticionCompartirLista(String usuario,String rol){
        compartirListaTask=new CompartirListaTask(usuario,rol);
        compartirListaTask.execute((Void) null);
    }
    public void peticionCompartirListaTest(String usuario,String rol){
        compartirListaTaskTest=new CompartirListaTaskTest(usuario,rol);
        compartirListaTaskTest.execute((Void) null);
    }
    public void peticionBorrarLista(){
        borrarListaTask=new BorrarListaTask();
        borrarListaTask.execute((Void) null);
    }
    public void peticionBorrarListaTest(){
        borrarListaTaskTest=new BorrarListaTaskTest();
        borrarListaTaskTest.execute((Void) null);
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
            if(!listas.isEmpty()){
                if(!Singleton.getInstance().existenListas()){
                    Singleton.getInstance().setListas(listas);
                }
                ((MainActivity)getActivity()).actualizarListas();
            }
        }

        @Override
        protected void onCancelled() {

        }


    }
    public class PeticionListasTaskTest extends AsyncTask<Void, Void, ArrayList<Lista>> {

        private String json;
        private ArrayList<Lista> listas=new ArrayList<>();

        PeticionListasTaskTest() {
        }

        @Override
        protected ArrayList<Lista> doInBackground(Void... params) {
            json=Constants.DUMMY_LISTAS;
            listas=QueryUtils.listasJson(json);
            Singleton.getInstance().setListas(listas);
            return listas;
        }

        @Override
        protected void onPostExecute(final ArrayList<Lista> listas) {
            if(!listas.isEmpty()){
                if(!Singleton.getInstance().existenListas()){
                    Singleton.getInstance().setListas(listas);
                }
                ((MainActivity)getActivity()).actualizarListas();
            }
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
                    lista=new Lista(Integer.parseInt(entrada.split(Constants.SEPARATOR)[1]),nombreNuevaLista,"Administrador");
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
                ((MainActivity)getActivity()).actualizarListas();
            else{
                Toast.makeText(getContext(), "No se ha podido crear la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }


    }
    public class PeticionNuevaListaTaskTest extends AsyncTask<Void, Void, Boolean> {
        private String json;
        private Lista lista;
        private Boolean listaCreada;

        PeticionNuevaListaTaskTest() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

                String entrada=Constants.DUMMY_LISTA_ACEPTADA;
                lista=new Lista(Integer.parseInt(entrada.split(Constants.SEPARATOR)[1]),nombreNuevaLista,"Administrador");
                Singleton.getInstance().añadirNuevaLista(lista);
                listaCreada=true;
                return listaCreada;
        }

        @Override
        protected void onPostExecute(final Boolean creada) {
            if(creada)
                ((MainActivity)getActivity()).actualizarListas();
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
                ((MainActivity)getActivity()).actualizarListas();
            else{
                Toast.makeText(getContext(), "No se ha podido borrar la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    public class BorrarListaTaskTest extends AsyncTask<Void, Void, Boolean> {

        private boolean peticion;

        BorrarListaTaskTest() {
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Singleton.getInstance().borrarLista(listaSeleccionada);
            Cambios.getInstance().addCambioLS(listaSeleccionada.getId());
            peticion=true;
            return peticion;
        }

        @Override
        protected void onPostExecute(final Boolean borrada) {
            if(borrada)
                ((MainActivity)getActivity()).actualizarListas();
            else{
                Toast.makeText(getContext(), "No se ha podido borrar la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    public class CompartirListaTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private boolean peticion;
        private String usuario;
        private String rol;
        CompartirListaTask(String usuario,String rol)
        {
            this.usuario=usuario;
            this.rol=rol;
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            socket = QueryUtils.getSocket();
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String salida = Constants.COMPARTIR_LISTA_PETICION + Constants.SEPARATOR + QueryUtils.getUsuario().getId() + Constants.SEPARATOR + listaSeleccionada.getId() + Constants.SEPARATOR + usuario + Constants.SEPARATOR + rol;
                out.println(salida);
                String entrada = in.readLine();
                if (entrada.split(Constants.SEPARATOR)[1] != null) {
                    if (entrada.split(Constants.SEPARATOR)[1].equals(Constants.COMPARTIR_LISTA_CORRECTA)) {
                        peticion = true;
                    } else {
                        peticion = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return peticion;
        }

            @Override
        protected void onPostExecute(final Boolean aceptada) {
            if(aceptada){
                listaSeleccionada.añadirUsuario(new Usuario(usuario,rol));
            }else{
                Toast.makeText(getContext(), "No se ha podido compartir la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    public class CompartirListaTaskTest extends AsyncTask<Void, Void, Boolean> {

        private boolean peticion;
        private String usuario;
        private String rol;
        CompartirListaTaskTest(String usuario,String rol)
        {
            this.usuario=usuario;
            this.rol=rol;
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String salida=Constants.COMPARTIR_LISTA_PETICION+Constants.SEPARATOR+QueryUtils.getUsuario().getId()+Constants.SEPARATOR+listaSeleccionada.getId()+Constants.SEPARATOR+usuario+Constants.SEPARATOR+rol;
            String entrada=Constants.DUMMY_COMPARTIR_LISTA_ACEPTADA;
            if(entrada!=null) {
                if(entrada.equals(Constants.COMPARTIR_LISTA_FALLIDA)){
                    peticion=true;
                }else{
                    peticion=false;
                }
            }

            return peticion;
        }

        @Override
        protected void onPostExecute(final Boolean aceptada) {
            if(aceptada){
                listaSeleccionada.añadirUsuario(new Usuario(usuario,rol));
                ((MainActivity)getActivity()).actualizarListas();
            }else{
                Toast.makeText(getContext(), "No existe ese usuario", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Cambios.getInstance().existenCambios()){
            Cambios.getInstance().enviarCambios();
        }
    }
}