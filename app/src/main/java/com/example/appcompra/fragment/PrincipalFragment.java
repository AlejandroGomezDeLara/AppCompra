package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

public class PrincipalFragment extends Fragment {
    private ImageView imagenFondoLista;
    private TextView nombreLista;
    private CardView ultimaLista;
    private TextView usuariosLista;
    private TextView textoRecetaRecomendada;
    private ImageView imagenRecetaRecomendada;
    private CardView cardViewReceta;
    private Receta recetaActual;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.RECETA_ALEATORIA_PETICION,QueryUtils.getUsuario().getId(),10));
        imagenFondoLista=view.findViewById(R.id.fondo_lista);
        nombreLista=view.findViewById(R.id.nombre_lista);
        ultimaLista=view.findViewById(R.id.ultimaLista);
        usuariosLista=view.findViewById(R.id.usuarios);
        textoRecetaRecomendada=view.findViewById(R.id.nombre_receta);
        imagenRecetaRecomendada=view.findViewById(R.id.imagen_receta);
        cardViewReceta=view.findViewById(R.id.card_view_receta);
        cardViewReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentRecetas();
            }
        });
        rellenarUltimaLista();
        recetaActual=new Receta(2,"","");
        ultimaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentUltimaLista();
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        if(!Singleton.getInstance().existenListas())
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.LISTAS_PETICION,QueryUtils.getUsuario().getId(),4));
        if(Singleton.getInstance().existenNotificaciones())
            Log.e("not",Singleton.getInstance().mostrarNotificaciones());
        if(Cambios.getInstance().existenCambios()){
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.ENVIAR_NOTIFICACIONES,QueryUtils.getUsuario().getId(),Cambios.getInstance().getCambiosString(),1));
        }

        ((MainActivity)getActivity()).getRecetaAleatoriaViewModel().getRecetaAleatoria().observe(getActivity(), new Observer<Receta>() {
            @Override
            public void onChanged(@Nullable Receta r) {
                if(r!=null){
                    recetaActual=r;
                    updateRecetaAleatoria(recetaActual);
                }
            }
        });
    }

    public void rellenarUltimaLista(){
        nombreLista.setText(Singleton.getInstance().getSharedPreferences().getString("nombreUltimaLista","nombre de la ultima lista"));
        usuariosLista.setText("hay "+Singleton.getInstance().getSharedPreferences().getInt("usuariosUltimaLista",0)+" usuarios");
        String rolLista=Singleton.getInstance().getSharedPreferences().getString("rolUltimaLista","espectador");
        switch (rolLista){
            case "administrador":
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.gradient_lista_admin));
                break;
            case "espectador":
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.gradient_lista_espectador));
                break;
            case "participante":
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.gradient_lista_participante));
                break;
            default:
                imagenFondoLista.setImageDrawable(getResources().getDrawable(R.drawable.producto_seleccionado));
            break;
        }
    }

    public void intentUltimaLista(){
        Singleton.getInstance().setIdListaSeleccionada(Singleton.getInstance().getSharedPreferences().getInt("idUltimaLista",0));
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(5);
    }
    public void intentRecetas(){
        Singleton.getInstance().setIdRecetaSeleccionada(recetaActual.getId());
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.INTERIOR_RECETA_PETICION,QueryUtils.getUsuario().getId(),Singleton.getInstance().getIdRecetaSeleccionada()+"",5));
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(6);
    }

    public void updateRecetaAleatoria(Receta r){
        Picasso.get().load(r.getUrl()).into(imagenRecetaRecomendada);
        textoRecetaRecomendada.setText(r.getNombre());
    }

}