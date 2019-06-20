package com.example.appcompra.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.appcompra.clases.Oferta;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.Singleton;

public class OfertaAleatoriaViewModel extends AndroidViewModel {
    private MutableLiveData<Oferta> ofertaAleatoria;
    private Application application;


    public OfertaAleatoriaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<Oferta> getOfertaAleatoria(){
        if(ofertaAleatoria ==null){
            ofertaAleatoria =new MutableLiveData<>();
            loadOferta();
        }

        return ofertaAleatoria;
    }
    public void loadOferta(){
        this.ofertaAleatoria.postValue(Singleton.getInstance().getOfertaActual());
    }

    public void setOfertaAleatoria(Oferta ofertaAleatoria) {
        if(this.ofertaAleatoria ==null){
            this.ofertaAleatoria =new MutableLiveData<>();
        }
        this.ofertaAleatoria.postValue(ofertaAleatoria);
    }

}
