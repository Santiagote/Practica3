package com.practica.controller.tda.graph;

import com.practica.controller.exception.OverFlowException;
import com.practica.controller.tda.list.LinkedList;



public class GrahpDirect extends Graph {
    protected Integer nro_vertices;
    protected Integer nro_edges;
    protected float[][] matrizAdyacencia;

    public GrahpDirect(Integer nro_vertices) {
        this.nro_vertices = nro_vertices;
        this.nro_edges = 0;
        this.matrizAdyacencia = new float[nro_vertices + 1][nro_vertices + 1];
        for (int i = 1; i <= nro_vertices; i++) {
            for (int j = 1; j <= nro_vertices; j++) {
                matrizAdyacencia[i][j] = Float.NaN;
            }
        }
    }

    @Override
    public Integer nro_Ver() {
        return nro_vertices;
    }

    @Override
    public Integer nro_Edges() {
        return nro_edges;
    }

    @Override
    public Boolean is_Edge(Integer v1, Integer v2) throws Exception {
        if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
            return !Float.isNaN(matrizAdyacencia[v1][v2]);
        } else {
            throw new Exception("Vértice no existe");
        }
    }

    @Override
    public Float weight_edge(Integer v1, Integer v2) throws Exception {
        if (is_Edge(v1, v2)) {
            return matrizAdyacencia[v1][v2];
        } else {
            return Float.NaN;
        }
    }

    @Override
    public void add_edge(Integer v1, Integer v2) throws Exception {
        add_edge(v1, v2, Float.NaN);
    }

    @Override
    public void add_edge(Integer v1, Integer v2, Float weight) throws Exception {
        if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
            if (!is_Edge(v1, v2)) {
                matrizAdyacencia[v1][v2] = weight;
                nro_edges++;
            }
        } else {
            throw new Exception("Vértice no existe");
        }
    }

    @Override
    public LinkedList<Adycencia> adyacencias(Integer v1) {
        LinkedList<Adycencia> adyacencias = new LinkedList<>();
        for (int j = 1; j <= nro_vertices; j++) {
            if (!Float.isNaN(matrizAdyacencia[v1][j])) {
                Adycencia adyacencia = new Adycencia(j, matrizAdyacencia[v1][j]);
                try {
                    adyacencias.add(adyacencia);
                } catch (Exception e) {
                    // Manejar excepción
                }
            }
        }
        return adyacencias;
    }
}