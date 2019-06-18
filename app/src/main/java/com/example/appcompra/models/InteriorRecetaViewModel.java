package com.example.appcompra.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;

import java.util.TreeSet;

public class InteriorRecetaViewModel extends AndroidViewModel {
    private MutableLiveData<Receta> recetaActual;
    private Application application;


    public InteriorRecetaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<Receta> getRecetaActual(){
        if(recetaActual==null){
            recetaActual=new MutableLiveData<>();
            loadReceta();
        }

        return recetaActual;
    }
    public void loadReceta(){
        this.recetaActual.postValue(Singleton.getInstance().getRecetaActual());
    }

    public void setReceta(Receta receta) {
        this.recetaActual.postValue(receta);
    }

}
