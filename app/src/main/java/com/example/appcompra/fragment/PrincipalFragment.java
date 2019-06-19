package com.example.appcompra.fragment;

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
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

public class PrincipalFragment extends Fragment {
    private ImageView imagenFondoLista;
    private TextView nombreLista;
    private CardView ultimaLista;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.RECETA_ALEATORIA_PETICION,QueryUtils.getUsuario().getId(),10));
        imagenFondoLista=view.findViewById(R.id.fondo_lista);
        nombreLista=view.findViewById(R.id.nombre_lista);
        ultimaLista=view.findViewById(R.id.ultimaLista);
        rellenarUltimaLista();
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
    }

    public void rellenarUltimaLista(){
        nombreLista.setText(Singleton.getInstance().getSharedPreferences().getString("nombreUltimaLista","nombre de la ultima lista"));
        String rolLista=Singleton.getInstance().getSharedPreferences().getString("rolUltimaLista","espectador");
        switch (rolLista){
            case "administrador":
                Picasso.get().load(R.drawable.gradient_lista_admin).into(imagenFondoLista);
                break;
            case "espectador":
                Picasso.get().load(R.drawable.gradient_lista_espectador).into(imagenFondoLista);
                break;
            case "participante":
                Picasso.get().load(R.drawable.gradient_lista_participante).into(imagenFondoLista);
                break;
        }
    }

    public void intentUltimaLista(){
        Singleton.getInstance().setIdListaSeleccionada(Singleton.getInstance().getSharedPreferences().getInt("idUltimaLista",0));
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(5);
    }
}