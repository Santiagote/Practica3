package com.practica.controller.tda.graph.algoritmos;

import com.practica.controller.tda.graph.GraphLabelDirect;
import com.practica.controller.tda.graph.Adycencia;
import com.practica.controller.tda.list.LinkedList;
import com.practica.controller.exception.ListEmptyException;

public class BFS<E> {
    private GraphLabelDirect<E> grafo;

    public BFS(GraphLabelDirect<E> grafo) {
        this.grafo = grafo;
    }

    public LinkedList<E> recorrerGrafo(E start) {
        int n = grafo.nro_Ver();
        boolean[] visitado = new boolean[n];
        LinkedList<E> resultado = new LinkedList<>();
        LinkedList<Integer> lista = new LinkedList<>(); // Usamos una lista como cola

        int startIndex = grafo.getVerticeL(start) - 1;
        visitado[startIndex] = true;
        lista.add(startIndex + 1); // Agregamos el nodo inicial a la lista

        while (!lista.isEmpty()) {
            try {
                int vertice = lista.delete(0); // Sacamos el primer elemento de la lista
                resultado.add(grafo.getLabelL(vertice)); // Lo agregamos al resultado

                LinkedList<Adycencia> adyacencias = grafo.adyacencias(vertice);
                if (!adyacencias.isEmpty()) {
                    Adycencia[] ady = adyacencias.toArray();
                    for (Adycencia a : ady) {
                        int vecino = a.getDestination() - 1;
                        if (!visitado[vecino]) {
                            visitado[vecino] = true;
                            lista.add(vecino + 1); // Agregamos los vecinos no visitados a la lista
                        }
                    }
                }
            } catch (ListEmptyException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }
}