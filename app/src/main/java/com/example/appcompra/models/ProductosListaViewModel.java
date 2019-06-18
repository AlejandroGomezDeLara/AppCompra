package com.example.appcompra.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appcompra.Constants;
import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.ProductoLista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class ProductosListaViewModel extends AndroidViewModel {
    private MutableLiveData<TreeSet<ProductoLista>> productos;
    private Application application;


    public ProductosListaViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        if(productos==null){
            productos=new MutableLiveData<>();
        }
    }


    public LiveData<TreeSet<ProductoLista>> getProductosLista(){
        loadProductosLista();
        return productos;
    }

    public void setProductosLista(TreeSet<ProductoLista> p){
        this.productos.postValue(p);
    }

    public void loadProductosLista(){
        productos.setValue(Singleton.getInstance().getProductosListaLista(Singleton.getInstance().getIdListaSeleccionada()));
    }

}
