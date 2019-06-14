package com.example.appcompra.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.appcompra.Constants;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class Peticion implements Comparable {
    private int idUsuario;
    private String codPeticion;
    private String parametros;
    private int importancia;

    public Peticion(String codPeticion,int idUsuario){
        this.codPeticion=codPeticion;
        this.idUsuario=idUsuario;
    }

    public Peticion(String codPeticion,int idUsuario,String parametros){
        this.idUsuario=idUsuario;
        this.codPeticion=codPeticion;
        this.parametros=parametros;
    }

    public Peticion( String codPeticion,int idUsuario, String parametros, int importancia) {
        this.idUsuario = idUsuario;
        this.codPeticion = codPeticion;
        this.parametros = parametros;
        this.importancia = importancia;
    }

    public Peticion( String codPeticion,int idUsuario, int importancia) {
        this.idUsuario = idUsuario;
        this.codPeticion = codPeticion;
        this.importancia = importancia;
    }

    public String getStringPeticion(){
        if(parametros!=null)
            return codPeticion+Constants.SEPARATOR+idUsuario+Constants.SEPARATOR+parametros;
        else
            return codPeticion+Constants.SEPARATOR+idUsuario;

    }

    public int getImportancia() {
        return importancia;
    }


    @Override
    public int compareTo(Object o) {
        if(((Peticion)o).getCodPeticion().equals(this.getCodPeticion())){
            return 0;

        }
        if(((Peticion)o).getImportancia() > this.getImportancia())
            return 1;
        else
            return -1;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getCodPeticion() {
        return codPeticion;
    }

    public String getParametros() {
        return parametros;
    }
}
