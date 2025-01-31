package com.practica.controller.tda.graph;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.HashMap;

import com.practica.controller.exception.LabelException;
import com.practica.controller.exception.OverFlowException;
import com.practica.controller.tda.list.LinkedList;


public class GraphLabelDirect<E> extends GrahpDirect {
    protected E labels[];
    protected HashMap<E, Integer> dictVertices;
    private Class<E> clazz;

    public GraphLabelDirect(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices);
        this.clazz = clazz;
        labels = (E[]) Array.newInstance(clazz, nro_vertices + 1);
        dictVertices = new HashMap<>();
    }

    public Boolean is_EdgeL(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
            return is_Edge(getVerticeL(v1), getVerticeL(v2));
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
    }

    public void insertEdgeL(E v1, E v2, Float weight) throws Exception {
        if (isLabelsGraph()) {
            add_edge(getVerticeL(v1), getVerticeL(v2), weight);
        } else {
            throw new LabelException("Grafo no etiquetado");
        }

    }

    public void insertEdgeL(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
            insertEdgeL(v1, v2, Float.NaN);
            // add_edge(getVerticeL(v1), getVerticeL(v2), Float.NaN);
        } else {
            throw new LabelException("Grafo no etiquetado");
        }

    }

    public LinkedList<Adycencia> adyacenciasL(E v) throws Exception {
        if (isLabelsGraph()) {
            return adyacencias(getVerticeL(v));
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
    }

    public void labelsVertices(Integer v, E data) {
        labels[v] = data;
        dictVertices.put(data, v);
    }

    public Boolean isLabelsGraph() {
        Boolean band = true;
        for (int i = 1; i < labels.length; i++) {
            if (labels[i] == null) {
                band = false;
                break;
            }

        }
        return band;
    }

    public Integer getVerticeL(E label) {
        return dictVertices.get(label);
    }

    public E getLabelL(Integer v1) {
        return labels[v1];
    }

    @Override
    public String toString() {
        String grafo = "";
        try {
            for (int i = 1; i <= this.nro_Ver(); i++) {
                grafo += "V" + i + " [" + getLabelL(i).toString() + "]" + "\n";
                LinkedList<Adycencia> lista = adyacencias(i);
                if (!lista.isEmpty()) {
                    Adycencia[] ady = lista.toArray();
                    for (int j = 0; j < ady.length; j++) {
                        Adycencia a = ady[j];
                        grafo += "ady " + "V" + a.getDestination() + " weight:" + a.getWeight() + " ["
                                + getLabelL(a.getDestination()).toString() + "]" + "\n";
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Error Graph:" + e);
        }

        return grafo;
    }

    public String drawGraph() {
        String grafo = " var nodes = new vis.DataSet([" + "\n";
        try {
            for (int i = 1; i <= this.nro_Ver(); i++) {
                grafo += "{ id: " + i + ", label: " + '"' + getLabelL(i).toString() + '"' + "},";
                // grafo += "V" + i + " [" + getLabelL(i).toString() + "]" + "\n";
                // LinkedList<Adyacencia> lista = adyacencias(i);
                // if (!lista.isEmpty()) {
                // Adyacencia[] ady = lista.toArray();
                // for (int j = 0; j < ady.length; j++) {
                // Adyacencia a = ady[j];
                // grafo += "ady " + "V" + a.getDestination() + " weight:" + a.getWeight() + "
                // ["
                // + getLabelL(i) + a.getDestination().toString() + "]" + "\n";
                // }
                // }

            }
            grafo += "]);" + "\n";

            grafo += "var edges = new vis.DataSet([" + "\n";

            for (int i = 1; i <= this.nro_Ver(); i++) {
                LinkedList<Adycencia> lista = adyacencias(i);
                if (!lista.isEmpty()) {
                    Adycencia[] ady = lista.toArray();
                    for (int j = 0; j < ady.length; j++) {
                        Adycencia a = ady[j];
                        grafo += "{ from: " + i + ", label: " + a.getDestination() + "}," + "\n";
                        // grafo += "ady " + "V" + a.getDestination() + " weight:" + a.getWeight() + "["
                        // + getLabelL(i)
                        // + a.getDestination().toString() + "]" + "\n";
                    }
                }

            }

            grafo += "]);" + "\n";

            grafo += "var container = document.getElementById(\"mynetwork\"):" + "\n";
            grafo += "var data = {" + "\n";
            grafo += "nodes: nodes," + "\n";
            grafo += "edges: edges," + "\n";
            grafo += "};" + "\n";
            grafo += "var options = {};" + "\n";
            grafo += "var network = new vis.Network(container, data, options);";

            FileWriter file = new FileWriter(
                    "resources" + File.separatorChar + "graph" + File.pathSeparatorChar +
                            "graph.js");
            file.write(grafo);
            file.flush();
            file.close();
        } catch (Exception e) {
            System.out.println("Error Graph:" + e);
        }

        return grafo;
    }
}
