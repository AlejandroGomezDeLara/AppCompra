package com.example.appcompra.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.appcompra.Constants;
import com.example.appcompra.R;
import com.example.appcompra.adapters.MenuAdapter;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

public class RecetasFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recetas, container, false);

        return view;
    }

    public void onResume() {
        super.onResume();
    }


}