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
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class CategoriaViewModel extends AndroidViewModel {
    private MutableLiveData<TreeSet<Categoria>> categorias;
    private Application application;


    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<TreeSet<Categoria>> getCategorias(){
        if(categorias==null){
            categorias=new MutableLiveData<>();
            loadCategorias();
        }

        return categorias;
    }
    public void loadCategorias(){
        this.categorias.postValue(Singleton.getInstance().getCategorias());
    }

    public void setCategorias(TreeSet<Categoria> categorias) {
        this.categorias.postValue(categorias);
    }

    public class EsperarRespuestaTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while(!Singleton.getInstance().existenCategorias()){}
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            categorias.setValue(Singleton.getInstance().getCategorias());
        }
    }
}
