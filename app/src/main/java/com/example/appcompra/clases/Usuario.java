package com.example.appcompra.clases;

import com.example.appcompra.R;

public class Usuario {
    private String email;
    private String nombre;
    private int imagenPerfil;

    public Usuario(String nombre) {
        this.nombre = nombre;
        this.imagenPerfil= R.drawable.ic_account_circle_black_24dp;
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

    public int getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(int imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }
}
