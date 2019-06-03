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
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class DespensaViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<ProductoLista>> productos;
    private Application application;
    protected PeticionProductosTask peticionProductosTask=null;


    public DespensaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public LiveData<ArrayList<ProductoLista>> getProductosDespensa(){
        if(productos==null){
            productos=new MutableLiveData<>();
            pedirProductos();
        }

        return productos;
    }

    public void setProductosDespensa(MutableLiveData<ArrayList<ProductoLista>> productos) {
        this.productos = productos;
    }

    public class PeticionProductosTask extends AsyncTask<Void, Void, ArrayList<ProductoLista>> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private ArrayList<ProductoLista> p=new ArrayList<>();

        PeticionProductosTask() {
        }
        @Override
        protected ArrayList<ProductoLista> doInBackground(Void... params) {
            socket=QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.PRODUCTOS_DESPENSA_PETICION+Constants.SEPARATOR+QueryUtils.getUsuario().getId());
                String entrada=in.readLine();
                if(entrada!=null && !entrada.isEmpty()){
                    Log.e("respuesta",entrada.split(Constants.SEPARATOR)[1]);
                    if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.PRODUCTOS_DESPENSA_CORRECTA)){
                        json=entrada.split(Constants.SEPARATOR)[1];
                        p=QueryUtils.productosLista(json);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return p;
        }

        @Override
        protected void onPostExecute(final ArrayList<ProductoLista> p) {
            productos.setValue(p);
        }

        @Override
        protected void onCancelled() {
        }
    }

    public void pedirProductos() {
        peticionProductosTask = new PeticionProductosTask();
        peticionProductosTask.execute((Void) null);
    }
}
