package com.example.appcompra.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.appcompra.Constants;
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

public class RecetaViewModel extends AndroidViewModel {
    private MutableLiveData<TreeSet<Receta>> recetas;
    private Application application;


    public RecetaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<TreeSet<Receta>> getRecetas(){
        if(recetas==null){
            recetas=new MutableLiveData<>();
            loadRecetas();
        }

        return recetas;
    }
    public void loadRecetas(){
        this.recetas.postValue(Singleton.getInstance().getRecetasCategoriaSeleccionada());
    }

    public void setRecetas(TreeSet<Receta> recetas) {
        this.recetas.postValue(recetas);
    }

}
