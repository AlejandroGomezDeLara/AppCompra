package com.example.appcompra.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.appcompra.Constants;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class Peticion implements Comparable<Peticion> {
    private int idUsuario;
    private String codPeticion;
    private String parametros;
    private boolean directa;

    public Peticion(String codPeticion,int idUsuario){
        this.codPeticion=codPeticion;
        this.idUsuario=idUsuario;
    }

    public Peticion(String codPeticion,int idUsuario,String parametros){
        this.idUsuario=idUsuario;
        this.codPeticion=codPeticion;
        this.parametros=parametros;
    }

    public Peticion( String codPeticion,int idUsuario, String parametros, boolean directa) {
        this.idUsuario = idUsuario;
        this.codPeticion = codPeticion;
        this.parametros = parametros;
        this.directa = directa;
    }

    public Peticion( String codPeticion,int idUsuario, boolean directa) {
        this.idUsuario = idUsuario;
        this.codPeticion = codPeticion;
        this.parametros = parametros;
        this.directa = directa;
    }

    public String getStringPeticion(){
        if(parametros!=null)
            return codPeticion+Constants.SEPARATOR+idUsuario+Constants.SEPARATOR+parametros;
        else
            return codPeticion+Constants.SEPARATOR+idUsuario;

    }

    public boolean isDirecta() {
        return directa;
    }

    public void setDirecta(boolean directa) {
        this.directa = directa;
    }

    @Override
    public int compareTo(Peticion o) {
        return 0;
    }
}
