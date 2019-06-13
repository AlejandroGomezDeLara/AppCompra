package com.example.appcompra.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.appcompra.clases.Producto;
import com.example.appcompra.clases.Singleton;

import java.util.TreeSet;

public class ProductoViewModel extends AndroidViewModel {
    private MutableLiveData<TreeSet<Producto>> productos;
    private Application application;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<TreeSet<Producto>> getProductos(){
        if(productos==null){
            productos=new MutableLiveData<>();
        }
        loadProductos();
        return productos;
    }

    public void setProductos(TreeSet<Producto> productos) {
        this.productos.postValue(productos);
    }

    public void loadProductos(){
        productos.setValue(Singleton.getInstance().getProductosCategoriaCategoria(Singleton.getInstance().getIdCategoriaSelecionada()));
    }




}
