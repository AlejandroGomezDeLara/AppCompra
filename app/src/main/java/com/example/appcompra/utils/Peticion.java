package com.example.appcompra.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.appcompra.Constants;

import java.util.Arrays;
import java.util.LinkedList;

public class Peticion {
    private int idUsuario;
    private String codPeticion;
    private LinkedList<String> parametros;

    Peticion(String codPeticion,int idUsuario){
        this.codPeticion=codPeticion;
        this.idUsuario=idUsuario;
    }

    Peticion(String codPeticion,int idUsuario,String... parametros){
        this.idUsuario=idUsuario;
        this.codPeticion=codPeticion;
        if(parametros.length>0){
            this.parametros=new LinkedList();
            this.parametros.addAll(Arrays.asList(parametros));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStringPeticion(){
        return codPeticion+Constants.SEPARATOR+idUsuario+Constants.SEPARATOR+String.join(Constants.SEPARATOR,parametros);
    }

}
