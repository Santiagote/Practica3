package com.practica.models;

public class Sintetica {
    private Integer id;
    private String nombre; 
    private Float precio;
    private Float altitud;
    private Float longitud;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
    	return this.nombre;
    }
    public void setNombre(String nombre) {
    	this.nombre = nombre;
    }

    public Float getPrecio() {
    	return this.precio;
    }
    public void setPrecio(Float precio) {
    	this.precio = precio;
    }

    public Float getAltitud() {
        return this.altitud;
    }

    public void setAltitud(Float altitud) {
        this.altitud = altitud;
    }

    public Float getLongitud() {
        return this.longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }


}
