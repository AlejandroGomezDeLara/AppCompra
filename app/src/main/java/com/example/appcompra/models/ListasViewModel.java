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
import com.example.appcompra.clases.Lista;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class ListasViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Lista>> listas;
    private Application application;

    public ListasViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<ArrayList<Lista>> getListas(){
        if(listas==null){
            listas=new MutableLiveData<>();
        }

        return listas;
    }


    public void setListas(MutableLiveData<ArrayList<Lista>> listas) {
        this.listas = listas;
    }



}
