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

public class ProductosListaViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<ProductoLista>> productos;
    private Application application;
    protected PeticionProductosTask peticionProductosTask=null;
    protected PeticionProductosTaskTest peticionProductosTaskTest=null;


    public ProductosListaViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        productos=new MutableLiveData<>();
    }

    public LiveData<ArrayList<ProductoLista>> getProductosLista(int idLista){
        if(productos==null){
            pedirProductos(idLista);
        }else{
            if(!Singleton.getInstance().getProductosLista().containsKey(idLista))
                pedirProductos(idLista);
            else{
                productos.setValue(Singleton.getInstance().getProductosLista().get(idLista));
            }
        }


        return productos;
    }

    public void setProductosLista(MutableLiveData<ArrayList<ProductoLista>> productos) {
        this.productos = productos;
    }

    public class PeticionProductosTask extends AsyncTask<Void, Void, ArrayList<ProductoLista>> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private int idLista;
        private ArrayList<ProductoLista> p=new ArrayList<>();

        PeticionProductosTask(int idLista) {
            this.idLista=idLista;
        }

        @Override
        protected ArrayList<ProductoLista> doInBackground(Void... params) {
            socket=QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.PRODUCTOS_LISTA_PETICION+Constants.SEPARATOR+QueryUtils.getUsuario().getId()+Constants.SEPARATOR+idLista);
                String entrada=Constants.DUMMY_PRODUCTO_LISTA_1;
                if(entrada!=null && !entrada.isEmpty()){
                    Log.e("rp",entrada.split(Constants.SEPARATOR)[1]);
                    if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.PRODUCTOS_LISTA_CORRECTA)){
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
            Singleton.getInstance().getProductosLista().put(idLista,p);
            productos.setValue(p);

        }

        @Override
        protected void onCancelled() {
        }
    }
    public class PeticionProductosTaskTest extends AsyncTask<Void, Void, ArrayList<ProductoLista>> {

        private String json;
        private int idLista;
        private ArrayList<ProductoLista> p=new ArrayList<>();

        PeticionProductosTaskTest(int idLista) {
            this.idLista=idLista;
        }

        @Override
        protected ArrayList<ProductoLista> doInBackground(Void... params) {
            if(idLista==1){
                json=Constants.DUMMY_PRODUCTO_LISTA_1;
            }else{
                json=Constants.DUMMY_PRODUCTO_LISTA_2;
            }
            Log.e("respuesta",json);
            p=QueryUtils.productosLista(json);

            return p;
        }

        @Override
        protected void onPostExecute(final ArrayList<ProductoLista> p) {
            productos.setValue(p);
            Singleton.getInstance().getProductosLista().put(idLista,p);
        }

        @Override
        protected void onCancelled() {
        }
    }

    public void pedirProductos(int idLista) {
        peticionProductosTaskTest = new PeticionProductosTaskTest(idLista);
        peticionProductosTaskTest.execute((Void) null);
        /*peticionProductosTask = new PeticionProductosTask(idLista);
        peticionProductosTask.execute((Void) null);*/
    }
}
