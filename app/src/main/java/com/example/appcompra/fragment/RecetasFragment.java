package com.example.appcompra.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appcompra.Constants;
import com.example.appcompra.MainActivity;
import com.example.appcompra.R;
import com.example.appcompra.adapters.MenuAdapter;
import com.example.appcompra.adapters.RecetaAdapter;
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class RecetasFragment extends Fragment {
    private TreeSet<Receta> recetas;
    private RecyclerView recyclerView;
    private RecetaAdapter recetaAdapter;
    private TextView mEmptyStateTextView;
    protected ProgressBar loadingIndicator;
    private Spinner categoriasSpinner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recetas, container, false);
        mEmptyStateTextView=view.findViewById(R.id.emptyStateView);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        loadingIndicator=view.findViewById(R.id.loading_indicator);
        categoriasSpinner=view.findViewById(R.id.spinner_categorias);
        recetas=new TreeSet<>();
        recyclerView=view.findViewById(R.id.recyclerView);
        recetaAdapter=new RecetaAdapter();
        recyclerView.setAdapter(recetaAdapter);
        //rellenarRecetas();
        updateUI(recetas);



        categoriasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, final long id) {
                if(position==0)
                    Singleton.getInstance().setIdCategoriaRecetaSeleccionada(position+1);
                else
                    Singleton.getInstance().setIdCategoriaRecetaSeleccionada(position);
                Singleton.getInstance().setPosicionSpinnerCategoriasRecetas(position);
                int idCategoria=Singleton.getInstance().getIdCategoriaRecetaSeleccionada();
                if(!Singleton.getInstance().existenRecetasCategoria()){
                    if(Singleton.getInstance().getIdCategoriaRecetaSeleccionada()!=0)
                        Singleton.getInstance().enviarPeticion(new Peticion(Constants.RECETAS_CATEGORIA_PETICION,QueryUtils.getUsuario().getId(),idCategoria+"",3));
                    else
                        Singleton.getInstance().enviarPeticion(new Peticion(Constants.RECETA_ALEATORIA_PETICION,QueryUtils.getUsuario().getId()));
                    updateUI(new TreeSet<Receta>());
                    loadingIndicator.setVisibility(View.VISIBLE);
                }else{
                    recetas=Singleton.getInstance().getRecetasCategoriaSelecionada();
                    updateUI(recetas);
                }
                mEmptyStateTextView.setVisibility(View.GONE);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        if(!Singleton.getInstance().existenCategoriasRecetas())
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.CATEGORIAS_RECETAS_PETICION,QueryUtils.getUsuario().getId(),3));
        return view;

    }


    public void rellenarRecetas(){
        recetas.add(new Receta(1,"Pasta con berenjenas","Esta es la descripcion","Esta es la preparacion","https://www.recetasderechupete.com/wp-content/uploads/2018/10/Pasta-con-berenjenas.jpg"));
        recetas.add(new Receta(2,"Ensalada con arroz","Esta es la descripcion","Esta es la preparacion","https://www.recetasderechupete.com/wp-content/uploads/2018/07/Ensalada-campera-525x360.jpg"));
        recetas.add(new Receta(3,"Spaguetti a la carbonara","Esta es la descripcion","Esta es la preparacion","https://www.pequerecetas.com/wp-content/uploads/2010/10/espaguetis-carbonara1.jpg"));
        recetas.add(new Receta(4,"Receta standard 4","Esta es la descripcion","Esta es la preparacion","https://www.pequerecetas.com/wp-content/uploads/2010/10/espaguetis-carbonara1.jpg"));
        recetas.add(new Receta(5,"Receta standard 5","Esta es la descripcion","Esta es la preparacion","https://www.pequerecetas.com/wp-content/uploads/2010/10/espaguetis-carbonara1.jpg"));
    }



    public void updateUI(TreeSet<Receta> r){
        recetaAdapter=new RecetaAdapter(r,getActivity(),R.layout.item_row_recetas,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recetaAdapter);
        recetaAdapter.notifyDataSetChanged();
        if(r.isEmpty()){
            mEmptyStateTextView.setText("No hay recetas en esa categoria");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        loadingIndicator.setVisibility(View.GONE);
    }

    public void onResume() {
        super.onResume();
        if(Singleton.getInstance().existenNotificaciones())
            Log.e("not",Singleton.getInstance().mostrarNotificaciones());
        if(Cambios.getInstance().existenCambios()){
            Singleton.getInstance().enviarPeticion(new Peticion(Constants.ENVIAR_NOTIFICACIONES,QueryUtils.getUsuario().getId(),Cambios.getInstance().getCambiosString(),1));
        }
        ((MainActivity)getActivity()).getCategoriaViewModel().getCategoriasRecetas().observe(getActivity(),new Observer<TreeSet<Categoria>>(){
            @Override
            public void onChanged(@Nullable TreeSet<Categoria> c) {
                if(c!=null)
                    updateSpinnerCategorias(c);
            }
        });

        ((MainActivity) getActivity()).getRecetaViewModel().getRecetas().observe(getActivity(), new Observer<TreeSet<Receta>>() {
            @Override
            public void onChanged(@Nullable TreeSet<Receta> r) {
                if(r!=null)
                    updateUI(r);
            }
        });
    }

    private void updateSpinnerCategorias(TreeSet<Categoria> c) {
        List<String> valoresSpinner=new ArrayList<>();
        List<Categoria> ci=new ArrayList<>(c);
        valoresSpinner.add("Recetas recomendadas");
        for (int i=0;i<ci.size();i++){
            valoresSpinner.add(ci.get(i).getNombre());
        }
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                valoresSpinner
        );
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item,valoresSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriasSpinner.setAdapter(adapter);
        categoriasSpinner.setSelection(Singleton.getInstance().getPosicionSpinnerCategoriasRecetas());
    }
}
