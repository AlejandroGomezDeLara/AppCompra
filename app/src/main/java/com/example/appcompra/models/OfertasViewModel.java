package com.example.appcompra.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.appcompra.clases.Oferta;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;

import java.util.ArrayList;
import java.util.TreeSet;

public class OfertasViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Oferta>> ofertas;
    private Application application;


    public OfertasViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<ArrayList<Oferta>> getOfertas(){
        if(ofertas==null){
            ofertas=new MutableLiveData<>();
            loadOfertas();
        }

        return ofertas;
    }
    public void loadOfertas(){
        this.ofertas.postValue(Singleton.getInstance().getOfertas());
    }

    public void setOfertas(ArrayList<Oferta> oferta) {
        if(this.ofertas==null){
            this.ofertas=new MutableLiveData<>();
        }
        this.ofertas.postValue(oferta);
    }

}
