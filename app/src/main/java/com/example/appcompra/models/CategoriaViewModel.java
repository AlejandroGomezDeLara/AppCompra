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
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class CategoriaViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Categoria>> categorias;
    private Application application;
    protected PeticionCategoriasTask peticionTask = null;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<ArrayList<Categoria>> getCategorias(){
        if(categorias==null){
            categorias=new MutableLiveData<>();
            loadCategorias();
        }
        return categorias;
    }

    public void loadCategorias() {
        peticionTask = new PeticionCategoriasTask();
        peticionTask.execute((Void) null);
    }

    public void setCategorias(MutableLiveData<ArrayList<Categoria>> categorias) {
        this.categorias = categorias;
    }


    public class PeticionCategoriasTask extends AsyncTask<Void, Void, ArrayList<Categoria>> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private ArrayList<Categoria> c=new ArrayList<>();
        PeticionCategoriasTask() {
        }

        @Override
        protected ArrayList<Categoria> doInBackground(Void... params) {

            socket= QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.CATEGORIAS_PETICION+Constants.SEPARATOR+QueryUtils.getUsuario().getId());
                String entrada=in.readLine();
                if(entrada!=null && !entrada.isEmpty()) {
                    Log.e("respuesta", entrada.split(Constants.SEPARATOR)[1]);
                    if (entrada.split(Constants.SEPARATOR)[0].equals(Constants.CATEGORIAS_RESPUESTA_CORRECTA)) {
                        json = entrada.split(Constants.SEPARATOR)[1];
                        c = QueryUtils.categoriasJson(json);
                    }
                }
            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }
            return c;
        }

        @Override
        protected void onPostExecute(final ArrayList<Categoria> ca) {
            categorias.setValue(ca);
        }

        @Override
        protected void onCancelled() {

        }
    }

}
