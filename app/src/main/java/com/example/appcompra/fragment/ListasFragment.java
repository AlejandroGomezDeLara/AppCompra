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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;
import java.util.TreeSet;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ListasFragment extends Fragment {

    protected ArrayList<Lista> listas;
    protected RecyclerView recyclerView;
    protected ListaAdapter adapter;
    protected ProgressBar loadingIndicator;
    protected Usuario usuario;
    protected Button addLista;
    protected Button addListaCentro;
    protected String nombreNuevaLista;
    protected TextView mEmptyStateTextView;
    protected Lista listaSeleccionada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        recyclerView=view.findViewById(R.id.recyclerView);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        listas=new ArrayList<>();
        usuario=QueryUtils.getUsuario();
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
        return view;
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
            ((MainActivity)getActivity()).getListasViewModel().getListas().observe(getActivity(), new Observer<TreeSet<Lista>>() {
                @Override
                public void onChanged(@Nullable TreeSet<Lista> l) {
                    if(l!=null){
                        updateUI(l);
                        ((MainActivity)getActivity()).updateUI(l);
                    }
                }
            });
        }else{
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        if(!Singleton.getInstance().existenListas())
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.LISTAS_PETICION,QueryUtils.getUsuario().getId(),4));
    }

    public void updateUI(TreeSet<Lista> m){
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
                    Singleton.getInstance().enviarPeticion(new Peticion(Constants.CREACION_NUEVA_LISTA,QueryUtils.getUsuario().getId(),nombreNuevaLista,4));

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

    @Override
    public void onStop() {
        super.onStop();
    }
}