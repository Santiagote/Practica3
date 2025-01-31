package com.practica.controller.tda.graph.algoritmos;

import com.practica.controller.tda.graph.GraphLabelDirect;
import com.practica.controller.tda.graph.Adycencia;
import com.practica.controller.tda.list.LinkedList;

public class Floyd<E> {
    private GraphLabelDirect<E> grafo;

    public Floyd(GraphLabelDirect<E> grafo) {
        this.grafo = grafo;
    }

    public double[][] calcularDistancias() {
        int n = grafo.nro_Ver();
        double[][] distancias = new double[n][n];

        // Inicializar la matriz de distancias
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                } else {
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // Llenar la matriz con las distancias conocidas
        for (int i = 1; i <= n; i++) {
            LinkedList<Adycencia> adyacencias = grafo.adyacencias(i);
            if (!adyacencias.isEmpty()) {
                Adycencia[] ady = adyacencias.toArray();
                for (Adycencia a : ady) {
                    distancias[i - 1][a.getDestination() - 1] = a.getWeight();
                }
            }
        }

        // Aplicar el algoritmo de Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }

        return distancias;
    }
}