package com.example.appcompra.clases;

import com.example.appcompra.R;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String email;
    private String nombre;
    private String urlImagenPerfil;

    public Usuario(int id,String nombre,String email,String urlImagenPerfil) {
        this.id=id;
        this.nombre = nombre;
        this.email=email;
        this.urlImagenPerfil=urlImagenPerfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUrlImagenPerfil() { return urlImagenPerfil; }

    public void setUrlImagenPerfil(String urlImagenPerfil) {this.urlImagenPerfil = urlImagenPerfil;}
}
