package com.practica.controller.dao.graphDao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.practica.controller.tda.graph.algoritmos.BFS;
import com.practica.controller.tda.graph.algoritmos.BellmanFord;
import com.practica.controller.tda.graph.algoritmos.Floyd;
import com.practica.controller.tda.graph.Adycencia;
import com.practica.controller.tda.graph.GrapLabelNoDirect;

import com.practica.models.Sintetica;
import com.practica.controller.dao.implement.AdapterDao;
import com.practica.controller.tda.list.LinkedList;



public class SinteticaDao extends AdapterDao<Sintetica> {
    private Sintetica Sintetica;
    private LinkedList<Sintetica> listAll;
    private GrapLabelNoDirect<String> grafo;
    private LinkedList<String> vertNombre;
    private String name = "GrafoSintetica.json";
    private Gson g = new Gson();

    public SinteticaDao() {
        super(Sintetica.class);
        this.listAll = new LinkedList<>();
    }

    public Sintetica getSintetica() {
        if (Sintetica == null) {
            Sintetica = new Sintetica();
        }
        return Sintetica;
    }

    public void setSintetica(Sintetica Sintetica) {
        this.Sintetica = Sintetica;
    }

    public LinkedList<Sintetica> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = listAll().getSize() + 1;
        Sintetica.setId(id);
        this.persist(this.Sintetica);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getSintetica(), getSintetica().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean deleteByIndex(Integer id) throws Exception {
        this.delete(id - 1);
        LinkedList<Sintetica> list = listAll();
        for (int i = 0; i < list.getSize(); i++) {
            list.get(i).setId((i + 1));
        }
        updateListFile(list);
        this.listAll = list;
        return true;
    }

    public LinkedList<Sintetica> order(String atribute, Integer type) throws Exception {
        LinkedList<Sintetica> listita = listAll();
        if (!listita.isEmpty()) {
            // Llamamos al método genérico `order` de la LinkedList
            listita = listita.order(atribute, type);
        }
        return listita;
    }

    public Sintetica getSinteticaByIndex(Integer Index) throws Exception {
        return get(Index);
    }

    public String getSinteticaJsonByIndex(Integer Index) throws Exception {
        return g.toJson(get(Index));
    }

    public void setListAll(LinkedList<Sintetica> listAll) {
        this.listAll = listAll;
    }
//Metodos para los grafos
public Boolean saveGraph(HashMap<String, Object> graphData, String filename) throws Exception {
    try {
        Gson gson = new Gson();
        String jsonString = gson.toJson(graphData);
        
        // Escribir en el archivo
        FileWriter file = new FileWriter(filename);
        file.write(jsonString);
        file.flush();
        file.close();
        
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

 
public GrapLabelNoDirect<String> loadGraph(String filename) throws Exception {
    // Leer el archivo JSON
    FileReader file = new FileReader(filename);
    JsonObject graphJson = new Gson().fromJson(file, JsonObject.class);

    // Crear el grafo
    JsonArray verticesArray = graphJson.getAsJsonArray("vertices");
    GrapLabelNoDirect<String> grafo = new GrapLabelNoDirect<>(verticesArray.size(), String.class);

    // Agregar vértices al grafo
    for (int i = 0; i < verticesArray.size(); i++) {
        JsonObject vertexJson = verticesArray.get(i).getAsJsonObject();
        String nombre = vertexJson.get("nombre").getAsString();
        grafo.labelsVertices(i + 1, nombre);
    }

    // Agregar aristas al grafo (si existen)
    JsonArray edgesArray = graphJson.getAsJsonArray("aristas");
    if (edgesArray != null) { // Verificar si el campo "aristas" existe
        for (int i = 0; i < edgesArray.size(); i++) {
            JsonObject edgeJson = edgesArray.get(i).getAsJsonObject();
            String origen = edgeJson.get("origen").getAsString();
            String destino = edgeJson.get("destino").getAsString();
            float peso = edgeJson.get("peso").getAsFloat();
            grafo.insertEdgeL(origen, destino, peso);
        }
    }

    return grafo;
}

public HashMap<String, Object> generateRandomEdges(GrapLabelNoDirect<String> grafo) throws Exception {
    Random random = new Random();
    HashMap<String, Object> graphData = new HashMap<>();
    
    // Cargar listAll si es nulo o está vacío
    if (listAll == null || listAll.isEmpty()) {
        listAll = getListAll();
    }
    
    // Verificar si hay vértices en el grafo
    if (grafo.nro_Ver() == 0) {
        System.out.println("No hay vértices en el grafo.");
        graphData.put("vertices", new ArrayList<>());
        graphData.put("aristas", new ArrayList<>());
        return graphData;
    }
    
    // Crear listas para vértices y aristas
    List<Map<String, Object>> vertices = new ArrayList<>();
    List<Map<String, Object>> aristas = new ArrayList<>();
    
    // Debug: Imprimir todos los Sintetica en listAll
    System.out.println("\nContenido de listAll:");
    for (int i = 0; i < listAll.getSize(); i++) {
        Sintetica s = listAll.get(i);
        System.out.println("Sintetica: " + s.getNombre() + 
                         " Alt: " + s.getAltitud() + 
                         " Long: " + s.getLongitud());
    }
    
    // Agregar vértices y crear un mapa de nombres a objetos Sintetica
    Map<String, Sintetica> sinteticaMap = new HashMap<>();
    for (int i = 1; i <= grafo.nro_Ver(); i++) {
        String nombreVertice = grafo.getLabelL(i);
        Map<String, Object> vertice = new HashMap<>();
        vertice.put("nombre", nombreVertice);
        vertices.add(vertice);
        
        // Buscar y almacenar el objeto Sintetica correspondiente
        Sintetica sintetica = findSinteticaByName(nombreVertice);
        if (sintetica != null) {
            sinteticaMap.put(nombreVertice, sintetica);
            System.out.println("Vértice encontrado: " + nombreVertice + 
                             " Alt: " + sintetica.getAltitud() + 
                             " Long: " + sintetica.getLongitud());
        } else {
            System.out.println("ADVERTENCIA: No se encontró datos para el vértice: " + nombreVertice);
        }
    }
    
    // Generar aristas aleatorias
    for (int i = 1; i <= grafo.nro_Ver(); i++) {
        String origen = grafo.getLabelL(i);
        Sintetica fromSintetica = sinteticaMap.get(origen);
        
        if (fromSintetica != null) {
            for (int j = i + 1; j <= grafo.nro_Ver(); j++) {
                String destino = grafo.getLabelL(j);
                Sintetica toSintetica = sinteticaMap.get(destino);
                
                if (toSintetica != null) {
                    try {
                        float weight = calculateWeight(fromSintetica, toSintetica);
                        
                        // Obtener los índices de los vértices
                        int origenIndex = grafo.getIndexByLabel(origen);
                        int destinoIndex = grafo.getIndexByLabel(destino);
                        
                        // Crear la arista
                        Map<String, Object> arista = new HashMap<>();
                        arista.put("origen", origen);
                        arista.put("destino", destino);
                        arista.put("peso", weight);
                        aristas.add(arista);
                        
                        // Agregar la arista al grafo usando índices enteros
                        grafo.insertEdgeL(origenIndex, destinoIndex, weight);
                        
                        System.out.println("Arista creada: " + origen + " -> " + destino + 
                                         " (peso: " + weight + ")");
                    } catch (Exception e) {
                        System.out.println("Error al crear arista entre " + origen + " y " + 
                                         destino + ": " + e.getMessage());
                    }
                }
            }
        }
    }
    
    graphData.put("vertices", vertices);
    graphData.put("aristas", aristas);
    
    System.out.println("\nResumen final:");
    System.out.println("Total de vértices: " + vertices.size());
    System.out.println("Total de aristas: " + aristas.size());
    
    return graphData;
}

private float calculateWeight(Sintetica from, Sintetica to) {
    if (from == null || to == null) {
        throw new IllegalArgumentException("Los vértices no pueden ser null");
    }
    
    Float fromAlt = from.getAltitud();
    Float fromLong = from.getLongitud();
    Float toAlt = to.getAltitud();
    Float toLong = to.getLongitud();
    
    if (fromAlt == null || fromLong == null || toAlt == null || toLong == null) {
        throw new IllegalArgumentException("Altitud o longitud faltante para " + 
                                         from.getNombre() + " o " + to.getNombre());
    }
    
    // Calcular la distancia euclidiana entre dos puntos
    float deltaX = fromLong - toLong;
    float deltaY = fromAlt - toAlt;
    return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
}


private Sintetica findSinteticaByName(String name) throws Exception {
    if (listAll == null) {
        listAll = getListAll();
    }
    
    for (int i = 0; i < listAll.getSize(); i++) {
        Sintetica s = listAll.get(i);
        if (s.getNombre().equals(name)) {
            return s;
        }
    }
    return null;
}

    

    public void generarYGuardarAristasAleatorias() throws Exception {
        // Cargar o crear el grafo
        SinteticaDao sinteticaDao = new SinteticaDao();
        GrapLabelNoDirect<String> grafo = sinteticaDao.loadGraph("grafo.json");
    
        // Generar aristas aleatorias
        HashMap<String, Object> graphData = sinteticaDao.generateRandomEdges(grafo);
    
        // Guardar las aristas en el archivo JSON
        sinteticaDao.saveGraph(graphData, "grafo.json");
    
        System.out.println("Aristas generadas y guardadas en grafo.json");
    }



    //Dibujar el grafo

    public void drawGraph(GrapLabelNoDirect<String> grafo, String filename) throws Exception {
        StringBuilder html = new StringBuilder();
    
        // Encabezado del HTML
        html.append("<!DOCTYPE html>\n")
            .append("<html>\n")
            .append("<head>\n")
            .append("  <title>Grafo</title>\n")
            .append("  <script type=\"text/javascript\" src=\"https://unpkg.com/vis-network/standalone/umd/vis-network.min.js\"></script>\n")
            .append("  <style type=\"text/css\">\n")
            .append("    #mynetwork {\n")
            .append("      width: 100%;\n")
            .append("      height: 600px;\n")
            .append("      border: 1px solid #ccc;\n")
            .append("    }\n")
            .append("  </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("  <div id=\"mynetwork\"></div>\n")
            .append("  <script type=\"text/javascript\">\n");
    
        // Datos de los nodos (vértices)
        html.append("    var nodes = new vis.DataSet([\n");
        for (int i = 1; i <= grafo.nro_Ver(); i++) {
            html.append("      { id: ").append(i).append(", label: '").append(grafo.getLabelL(i)).append("' },\n");
        }
        html.append("    ]);\n");
    
        // Datos de las aristas
        html.append("    var edges = new vis.DataSet([\n");
        for (int i = 1; i <= grafo.nro_Ver(); i++) {
            LinkedList<Adycencia> adyacencias = grafo.adyacencias(i);
            if (!adyacencias.isEmpty()) {
                Adycencia[] ady = adyacencias.toArray();
                for (Adycencia a : ady) {
                    html.append("      { from: ").append(i).append(", to: ").append(a.getDestination())
                        .append(", label: '").append(String.format("%.2f", a.getWeight())).append("' },\n");
                }
            }
        }
        html.append("    ]);\n");
    
        // Configuración de Vis.js
        html.append("    var container = document.getElementById('mynetwork');\n")
            .append("    var data = {\n")
            .append("      nodes: nodes,\n")
            .append("      edges: edges\n")
            .append("    };\n")
            .append("    var options = {};\n")
            .append("    var network = new vis.Network(container, data, options);\n")
            .append("  </script>\n")
            .append("</body>\n")
            .append("</html>");
    
        // Escribir el archivo HTML
        FileWriter file = new FileWriter(filename);
        file.write(html.toString());
        file.flush();
        file.close();
    }

    public void generarYMostrarGrafo() throws Exception {
        // Cargar o crear el grafo
        SinteticaDao sinteticaDao = new SinteticaDao();
        GrapLabelNoDirect<String> grafo = sinteticaDao.loadGraph("grafo.json");
    
        // Generar aristas aleatorias (si es necesario)
        sinteticaDao.generateRandomEdges(grafo);
    
        // Dibujar el grafo en un archivo HTML
        sinteticaDao.drawGraph(grafo, "grafo.html");
    
        System.out.println("Grafo generado y guardado en grafo.html");
    }

    //Algoritmos
    public LinkedList<String> aplicarBFS(String start) {
        try {
            if (grafo == null) {
                throw new IllegalStateException("El grafo no está inicializado.");
            }

            BFS<String> bfs = new BFS<>(grafo);
            return bfs.recorrerGrafo(start);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public double[] aplicarBellmanFord(String source) {
        try {
            if (grafo == null) {
                throw new IllegalStateException("El grafo no está inicializado.");
            }
    
            BellmanFord<String> bellmanFord = new BellmanFord<>(grafo);
            double[] distancias = bellmanFord.calcularDistancias(source);
    
            return distancias;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public double[][] aplicarFloyd() {
        try {
            if (grafo == null) {
                throw new IllegalStateException("El grafo no está inicializado.");
            }
    
            Floyd<String> floyd = new Floyd<>(grafo);
            double[][] distancias = floyd.calcularDistancias();
    
            return distancias;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

