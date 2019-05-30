package com.example.appcompra.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.MenuAdapter;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Cambios;

public class PrincipalFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Inicio");
        return view;
    }

    public void onResume() {
        super.onResume();
        if(Cambios.getInstance().existenCambios())
            Cambios.getInstance().enviarCambios();

    }
}