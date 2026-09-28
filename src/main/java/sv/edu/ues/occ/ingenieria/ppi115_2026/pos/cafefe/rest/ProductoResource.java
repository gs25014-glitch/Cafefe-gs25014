/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.rest;

/**
 *
 * @author kelvi
 */

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoService;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {

    @Inject
    private ProductoService productoService;

    @GET
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") UUID id) {

        Producto producto = productoService.buscarPorId(id);

        if (producto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(producto).build();
    }

    @POST
    public Response guardar(Producto producto) {

        if (producto.getIdProducto() == null) {
            producto.setIdProducto(UUID.randomUUID());
        }

        productoService.guardar(producto);

        return Response
                .status(Response.Status.CREATED)
                .entity(producto)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(
            @PathParam("id") UUID id,
            Producto producto) {

        Producto existente = productoService.buscarPorId(id);

        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        producto.setIdProducto(id);

        Producto actualizado = productoService.actualizar(producto);

        return Response.ok(actualizado).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {

        Producto existente = productoService.buscarPorId(id);

        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        productoService.eliminar(id);

        return Response.noContent().build();
    }
}