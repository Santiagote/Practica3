package com.practica.controller.tda.graph;

public class Adycencia {
    private Integer destination; // Vértice de destino
    private float weight;        // Peso de la arista

    // Constructor
    public Adycencia(Integer destination, float weight) {
        this.destination = destination;
        this.weight = weight;
    }

    // Constructor por defecto
    public Adycencia() {}

    // Getters y Setters
    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}