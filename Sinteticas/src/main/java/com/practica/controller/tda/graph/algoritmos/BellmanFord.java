package com.practica.controller.tda.graph.algoritmos;

import com.practica.controller.tda.graph.GraphLabelDirect;
import com.practica.controller.tda.graph.Adycencia;
import com.practica.controller.tda.list.LinkedList;

public class BellmanFord<E> {
    private GraphLabelDirect<E> grafo;

    public BellmanFord(GraphLabelDirect<E> grafo) {
        this.grafo = grafo;
    }

    public double[] calcularDistancias(E source) {
        int n = grafo.nro_Ver();
        double[] distancias = new double[n];
        int sourceIndex = grafo.getVerticeL(source) - 1;

        // Inicializar las distancias
        for (int i = 0; i < n; i++) {
            distancias[i] = Double.POSITIVE_INFINITY;
        }
        distancias[sourceIndex] = 0;

        // Relajar las aristas repetidamente
        for (int i = 1; i < n; i++) {
            for (int u = 1; u <= n; u++) {
                LinkedList<Adycencia> adyacencias = grafo.adyacencias(u);
                if (!adyacencias.isEmpty()) {
                    Adycencia[] ady = adyacencias.toArray();
                    for (Adycencia a : ady) {
                        int v = a.getDestination() - 1;
                        double peso = a.getWeight();
                        if (distancias[u - 1] + peso < distancias[v]) {
                            distancias[v] = distancias[u - 1] + peso;
                        }
                    }
                }
            }
        }

        // Verificar si hay ciclos de peso negativo
        for (int u = 1; u <= n; u++) {
            LinkedList<Adycencia> adyacencias = grafo.adyacencias(u);
            if (!adyacencias.isEmpty()) {
                Adycencia[] ady = adyacencias.toArray();
                for (Adycencia a : ady) {
                    int v = a.getDestination() - 1;
                    double peso = a.getWeight();
                    if (distancias[u - 1] + peso < distancias[v]) {
                        throw new IllegalStateException("El grafo contiene un ciclo de peso negativo");
                    }
                }
            }
        }

        return distancias;
    }
}