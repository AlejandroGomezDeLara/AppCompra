package com.example.appcompra.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appcompra.Constants;
import com.example.appcompra.R;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

public class PrincipalFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    public void onResume() {
        super.onResume();
        if(Singleton.getInstance().existenNotificaciones())
            Log.e("not",Singleton.getInstance().mostrarNotificaciones());
        if(Cambios.getInstance().existenCambios()){
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.ENVIAR_NOTIFICACIONES,QueryUtils.getUsuario().getId(),Cambios.getInstance().getCambiosString(),1));
        }
    }
}