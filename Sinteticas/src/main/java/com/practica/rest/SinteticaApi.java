package com.practica.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.practica.controller.exception.ListEmptyException;
import com.practica.models.Sintetica;
import com.practica.controller.dao.graphDao.SinteticaDao;
import com.practica.controller.dao.service.SinteticaServices;
import com.practica.controller.tda.graph.GrapLabelNoDirect;
import com.practica.controller.tda.list.LinkedList;
import javax.ws.rs.core.Response.Status;

@Path("/Sintetica")
public class SinteticaApi {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap map = new HashMap<>();
        SinteticaServices ps = new SinteticaServices();
        map.put("msg", "OK");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }
        return Response.ok(map).build();
    }

    /*
     * @GET
     * 
     * @Produces(MediaType.APPLICATION_JSON)
     * 
     * @Path("/list/search/{texto}")
     * public Response getName(@PathParam("texto") String texto) throws
     * ListEmptyException, Exception {
     * HashMap map = new HashMap<>();
     * SinteticaServices ps = new SinteticaServices();
     * map.put("msg", "OK");
     * LinkedList lista = ps.buscar_apellidos(texto);
     * map.put("data", lista.toArray());
     * if (lista.isEmpty()) {
     * map.put("data", new Object[] {});
     * }
     * return Response.ok(map).build();
     * }
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{attribute}/{type}")
    public Response getOrder(@PathParam("attribute") String attribute, @PathParam("type") Integer type) {
        HashMap<String, Object> map = new HashMap<>();
        SinteticaServices ps = new SinteticaServices();

        try {
            // Llamar al nuevo método 'order' en el servicio
            LinkedList<Sintetica> lista = ps.order(attribute, type);

            map.put("msg", "OK");
            map.put("data", lista.toArray());

            if (lista.isEmpty()) {
                map.put("data", new Object[] {});
            }
        } catch (Exception e) {
            map.put("msg", "Error");
            map.put("data", e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }

        return Response.ok(map).build();
    }


    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSintetica(@PathParam("id") Integer id) {
        HashMap map = new HashMap<>();
        SinteticaServices ps = new SinteticaServices();
        try {
            ps.setSintetica(ps.get(id));
        } catch (Exception e) {

        }

        map.put("msg", "OK");
        map.put("data", ps.getSintetica());
        if (ps.getSintetica().getId() == null) {
            map.put("data", "No existe la persona con ese identificador");
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }
        return Response.ok(map).build();
    }

    // @DELETE
    // @Path("/delete{id}")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response deleteSinteticaByIndex(@PathParam("id") Integer id){
    // SinteticaServices ps = new SinteticaServices();
    // HashMap map = new HashMap<>();

    // try {
    // if (ps.delete(id)) {
    // map.put("status", "success");
    // map.put("message", "Familia eliminada con éxito.");
    // return Response.ok(map).build();
    // } else {
    // map.put("status", "error");
    // map.put("message", "No se pudo eliminar la familia.");
    // return Response.ok(map).build();
    // }
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {

        HashMap res = new HashMap<>();
        Gson g = new Gson();
        String a = g.toJson(map);
        System.out.println("Datos recibidos: " + a);

        try {
            SinteticaServices ps = new SinteticaServices();
            ps.getSintetica().setNombre(map.get("nombre").toString());
            ps.getSintetica().setPrecio(Float.parseFloat(map.get("precio").toString()));
            ps.getSintetica().setAltitud(Float.parseFloat(map.get("precio").toString()));
            ps.getSintetica().setLongitud(Float.parseFloat(map.get("precio").toString()));
            ps.save();
            res.put("msg", "OK");
            res.put("data", "Sintetica registrada correctamente");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Error en sav data: " + e.toString());
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap<String, Object> map) {
        HashMap<String, String> res = new HashMap<>();
    
        try {
            // Validar que el mapa contenga los campos necesarios
            if (!map.containsKey("id") || !map.containsKey("nombre")) {
                res.put("status", "error");
                res.put("message", "Los campos 'id' y 'nombre' son obligatorios.");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }
    
            // Obtener el ID de la entidad a actualizar
            int id = Integer.parseInt(map.get("id").toString());
    
            // Crear el servicio y buscar la entidad
            SinteticaServices ps = new SinteticaServices();
            Sintetica sintetica = ps.get(id); // Obtener la entidad por ID
    
            // Verificar si la entidad existe
            if (sintetica == null) {
                res.put("status", "error");
                res.put("message", "No existe una entidad Sintetica con el ID proporcionado.");
                return Response.status(Status.NOT_FOUND).entity(res).build();
            }
    
            // Actualizar los campos de la entidad
            sintetica.setNombre(map.get("nombre").toString());
    
            // Si hay otros campos, actualízalos aquí
            if (map.containsKey("precio")) {
                sintetica.setPrecio(Float.parseFloat(map.get("precio").toString()));
            }
            if (map.containsKey("altitud")) {
                sintetica.setAltitud(Float.parseFloat(map.get("altitud").toString()));
            }
            if (map.containsKey("longitud")) {
                sintetica.setLongitud(Float.parseFloat(map.get("longitud").toString()));
            }
    
            // Configurar la entidad en el servicio
            ps.setSintetica(sintetica);
    
            // Guardar los cambios
            boolean isUpdated = ps.update(); // Llamar al método update sin parámetros
    
            if (!isUpdated) {
                res.put("status", "error");
                res.put("message", "No se pudo actualizar la entidad Sintetica.");
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
            }
    
            // Respuesta exitosa
            res.put("status", "success");
            res.put("message", "Sintetica actualizada con éxito.");
            return Response.ok(res).build();
    
        } catch (NumberFormatException e) {
            res.put("status", "error");
            res.put("message", "El campo 'id' debe ser un número válido.");
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }
/*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/{type_order}/{atributo}")
    public Response getAllPersons(@PathParam("type_order") Integer typeOrder,
            @PathParam("atributo") String atributo) throws ListEmptyException {
        String responseJson = "";
        SinteticaServices ps = new SinteticaServices();
        Gson gson = new Gson();

        try {
            if (typeOrder == null || (typeOrder != 1 && typeOrder != 2)) {
                throw new IllegalArgumentException("El tipo de orden debe ser 1 (ascendente) o 2 (descendente).");
            }
            if (!atributo.equalsIgnoreCase("apellidos") && !atributo.equalsIgnoreCase("nombres")) {
                throw new IllegalArgumentException("El atributo debe ser 'apellidos' o 'nombres'.");
            }
            LinkedList<Sintetica> orderedList = ps.order_type(typeOrder, atributo);
            responseJson = "{\"data\":\"succes!\",\"info\":" +
                    gson.toJson(orderedList.toArray()) + "}";
            // ps.listAll() //orderedList
        } catch (Exception e) {
            e.printStackTrace();
            responseJson = "{\"data\":\"ErrorMsg\",\"info\":\"" +
                    e.getMessage() + "\"}";
        }

        return Response.ok(responseJson).build();
    }
*/

//Para los grafos
@Path("/saveGraph")
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response saveGraph(HashMap<String, Object> graphData) {
    HashMap<String, String> response = new HashMap<>();

    try {
        SinteticaServices services = new SinteticaServices();
        services.saveGraph(graphData);

        response.put("status", "success");
        response.put("message", "Grafo guardado correctamente.");
        return Response.ok(response).build();
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", "Error al guardar el grafo: " + e.getMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}

@Path("/loadGraph")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response loadGraph() {
    HashMap<String, Object> response = new HashMap<>();

    try {
        SinteticaServices services = new SinteticaServices();
        GrapLabelNoDirect<String> grafo = services.loadGraph();

        response.put("status", "success");
        response.put("data", grafo.toString());
        return Response.ok(response).build();
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", "Error al cargar el grafo: " + e.getMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}

@Path("/generateRandomEdges")
@POST
@Produces(MediaType.APPLICATION_JSON)
public Response generateRandomEdges() {
    HashMap<String, String> response = new HashMap<>();

    try {
        SinteticaServices services = new SinteticaServices();
        GrapLabelNoDirect<String> grafo = services.loadGraph();
        services.generateRandomEdges(grafo);

        response.put("status", "success");
        response.put("message", "Aristas generadas aleatoriamente.");
        return Response.ok(response).build();
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", "Error al generar aristas: " + e.getMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}

@Path("/drawGraph")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response drawGraph() {
    HashMap<String, String> response = new HashMap<>();

    try {
        SinteticaServices services = new SinteticaServices();
        GrapLabelNoDirect<String> grafo = services.loadGraph();
        services.drawGraph(grafo);

        response.put("status", "success");
        response.put("message", "Grafo generado y guardado en grafo.html");
        return Response.ok(response).build();
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", "Error al generar el grafo: " + e.getMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}

//Algoritmos

    @GET
    @Path("/bfs/{start}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response aplicarBFS(@PathParam("start") String start) {
        HashMap<String, Object> response = new HashMap<>();
        SinteticaServices services = new SinteticaServices();

        try {
            LinkedList<String> recorrido = services.aplicarBFS(start);
            response.put("status", "success");
            response.put("data", recorrido.toArray());
            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }


    @GET
    @Path("/floyd")
    @Produces(MediaType.APPLICATION_JSON)
    public Response aplicarFloyd() {
        HashMap<String, Object> response = new HashMap<>();
        SinteticaServices services = new SinteticaServices();

        try {
            double[][] distancias = services.aplicarFloyd();
            response.put("status", "success");
            response.put("data", distancias);
            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }


    @GET
    @Path("/bellman-ford/{source}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response aplicarBellmanFord(@PathParam("source") String source) {
        HashMap<String, Object> response = new HashMap<>();
        SinteticaServices services = new SinteticaServices();

        try {
            double[] distancias = services.aplicarBellmanFord(source);
            response.put("status", "success");
            response.put("data", distancias);
            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    //Metodos de prueba

    @POST
    @Path("/generar-aristas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarAristasAleatorias() {
        HashMap<String, Object> response = new HashMap<>();
        SinteticaServices services = new SinteticaServices();
    
        try {
            HashMap<String, Object> graphData = services.generarAristasAleatorias();
            
            // Convertir el resultado a un formato más simple para la respuesta
            response.put("status", "success");
            response.put("message", "Aristas generadas y guardadas correctamente.");
            response.put("data", graphData);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al generar aristas: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
    


    @GET
    @Path("/cargar-grafo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cargarGrafo() {
        HashMap<String, Object> response = new HashMap<>();
        SinteticaServices services = new SinteticaServices();

        try {
            GrapLabelNoDirect<String> grafo = services.cargarGrafo();
            response.put("status", "success");
            response.put("data", grafo.toString());
            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al cargar el grafo: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
