package com.example.appcompra.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;

import java.util.TreeSet;

public class RecetaAleatoriaViewModel extends AndroidViewModel {
    private MutableLiveData<Receta> recetaAaleatoria;
    private Application application;


    public RecetaAleatoriaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<Receta> getRecetaAleatoria(){
        if(recetaAaleatoria==null){
            recetaAaleatoria=new MutableLiveData<>();
            loadReceta();
        }

        return recetaAaleatoria;
    }
    public void loadReceta(){
        this.recetaAaleatoria.postValue(Singleton.getInstance().getRecetaActual());
    }

    public void setReceta(Receta receta) {
        if(this.recetaAaleatoria==null){
            this.recetaAaleatoria=new MutableLiveData<>();
        }
        this.recetaAaleatoria.postValue(receta);
    }

}
