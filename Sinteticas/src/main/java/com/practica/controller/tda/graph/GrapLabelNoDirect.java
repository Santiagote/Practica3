package com.practica.controller.tda.graph;

import static java.lang.Math.E;
import com.practica.controller.tda.list.LinkedList;

import com.practica.controller.exception.LabelException;


public class GrapLabelNoDirect<E> extends GraphLabelDirect<E> {
    protected LinkedList<Adycencia>[] listAdyacencias;

    public GrapLabelNoDirect(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices, clazz);
        listAdyacencias = new LinkedList[nro_vertices + 1];
        for (int i = 1; i <= nro_vertices; i++) {
            listAdyacencias[i] = new LinkedList<>();
        }
    }

    @Override
    public void add_edge(Integer v1, Integer v2, Float weight) throws Exception {
        if (v1.intValue() <= nro_Ver() && v2.intValue() <= nro_Ver()) {
            if (!is_Edge(v1, v2)) {
                // Agregar arista en ambas direcciones para grafo no dirigido
                matrizAdyacencia[v1][v2] = weight;
                matrizAdyacencia[v2][v1] = weight;
                
                // Actualizar listas de adyacencia en ambas direcciones
                Adycencia ady1 = new Adycencia(v2, weight);
                Adycencia ady2 = new Adycencia(v1, weight);
                listAdyacencias[v1].add(ady1);
                listAdyacencias[v2].add(ady2);
                
                nro_edges++;
            }
        } else {
            throw new Exception("Vértice no existe");
        }
    }

    @Override
    public LinkedList<Adycencia> adyacencias(Integer v1) {
        return listAdyacencias[v1];
    }

    public int getIndexByLabel(String label) {
        for (int i = 1; i <= nro_Ver(); i++) {
            if (getLabelL(i).equals(label)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Vértice no encontrado: " + label);
    }

    public void insertEdgeL(int origen, int destino, float peso) throws Exception {
        if (origen < 1 || origen > nro_Ver() || destino < 1 || destino > nro_Ver()) {
            throw new IllegalArgumentException("Índices de vértices fuera de rango.");
        }
        
        add_edge(origen, destino, peso);
    }
}
