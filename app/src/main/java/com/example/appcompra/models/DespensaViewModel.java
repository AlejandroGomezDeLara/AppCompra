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

public class DespensaViewModel extends AndroidViewModel {
    private MutableLiveData<TreeSet<ProductoLista>> productos;
    private Application application;


    public DespensaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<TreeSet<ProductoLista>> getProductosDespensa(){
        if(productos==null){
            productos=new MutableLiveData<>();
        }
        loadDespensa();
        return productos;
    }

    public void loadDespensa(){
        productos.setValue(Singleton.getInstance().getDespensa());
    }
    public void setDespensa(TreeSet<ProductoLista> p){
        this.productos.postValue(p);
    }

}
