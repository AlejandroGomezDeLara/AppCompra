package com.example.appcompra.clases;

public class ProductoLista extends Producto{
    private int unidades;
    private int receta;
    private int cadena;
    private String marca;
    private String cantidad;
    private boolean comprado;

    public ProductoLista(int id, String nombre, int unidades, int receta, int cadena, boolean comprado, String url,String marca,String cantidad) {
        super(id, nombre, url);
        this.unidades =unidades;
        this.receta=receta;
        this.cadena=cadena;
        this.comprado=comprado;
        this.marca=marca;
        this.cantidad=cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public int getReceta() {
        return receta;
    }

    public void setReceta(int receta) {
        this.receta = receta;
    }

    public int getCadena() {
        return cadena;
    }

    public void setCadena(int cadena) {
        this.cadena = cadena;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    public void sumarUnidades(int u) {
        this.unidades+=u;
    }

    @Override
    public int compareTo(Object o) {
        if(((ProductoLista)o).getId() == this.getId()){
            return 0;
        }
        else return 1;
    }
}
