package com.example.appcompra.clases;

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
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class ProductoViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Producto>> productos;
    private Application application;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<ArrayList<Producto>> getProductos(){
        if(productos==null){
            productos=new MutableLiveData<>();
        }

        return productos;
    }


    public void setProductos(MutableLiveData<ArrayList<Producto>> productos) {
        this.productos = productos;
    }



}
