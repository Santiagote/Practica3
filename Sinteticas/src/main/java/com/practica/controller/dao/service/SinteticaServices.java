package com.practica.controller.dao.service;

import java.util.HashMap;

import com.practica.controller.dao.graphDao.SinteticaDao;
import com.practica.models.Sintetica;
import com.practica.controller.tda.graph.GrapLabelNoDirect;
import com.practica.controller.tda.graph.algoritmos.BFS;
import com.practica.controller.tda.graph.algoritmos.BellmanFord;
import com.practica.controller.tda.graph.algoritmos.Floyd;
import com.practica.controller.tda.list.LinkedList;


public class SinteticaServices {
    private SinteticaDao obj;

    public SinteticaServices() {
        this.obj = new SinteticaDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    @SuppressWarnings("rawtypes")
    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public Sintetica getSintetica() {
        return obj.getSintetica();
    }

    public void setSintetica(Sintetica Sintetica) {
        obj.setSintetica(Sintetica);
    }

    public Sintetica getSinteticaByIndex(Integer index) throws Exception {
        return obj.getSinteticaByIndex(index);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean delete(Integer index) throws Exception {
        return obj.deleteByIndex(index);
    }

    public String getSinteticaJsonByIndex(Integer index) throws Exception {
        return obj.getSinteticaJsonByIndex(index);
    }

    public Sintetica get(Integer id) throws Exception {
        return obj.get(id);
    }

    public LinkedList<Sintetica> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    //grafos
    public Boolean saveGraph(HashMap<String, Object> graphData) throws Exception {
        return obj.saveGraph(graphData, "grafo.json");
    }

    public GrapLabelNoDirect<String> loadGraph() throws Exception {
        return obj.loadGraph("grafo.json");
    }

    public void generateRandomEdges(GrapLabelNoDirect<String> grafo) throws Exception {
        obj.generateRandomEdges(grafo);
    }

    public void drawGraph(GrapLabelNoDirect<String> grafo) throws Exception {
        obj.drawGraph(grafo, "grafo.html");
    }

    public LinkedList<String> aplicarBFS(String start) throws Exception {
        GrapLabelNoDirect<String> grafo = obj.loadGraph("grafo.json");
        BFS<String> bfs = new BFS<>(grafo);
        return bfs.recorrerGrafo(start);
    }

    public double[][] aplicarFloyd() throws Exception {
        GrapLabelNoDirect<String> grafo = obj.loadGraph("grafo.json");
        Floyd<String> floyd = new Floyd<>(grafo);
        return floyd.calcularDistancias();
    }

    public double[] aplicarBellmanFord(String source) throws Exception {
        GrapLabelNoDirect<String> grafo = obj.loadGraph("grafo.json");
        BellmanFord<String> bellmanFord = new BellmanFord<>(grafo);
        return bellmanFord.calcularDistancias(source);
    }


    public HashMap<String, Object> generarAristasAleatorias() throws Exception {
        GrapLabelNoDirect<String> grafo = obj.loadGraph("grafo.json");
        HashMap<String, Object> graphData = obj.generateRandomEdges(grafo);
        obj.saveGraph(graphData, "grafo.json"); 
        return graphData;
    }

    public GrapLabelNoDirect<String> cargarGrafo() throws Exception {
        return obj.loadGraph("grafo.json");
    }
}
