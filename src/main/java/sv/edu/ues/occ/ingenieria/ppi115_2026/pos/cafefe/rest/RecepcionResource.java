package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.RecepcionService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.RecepcionService.OrdenDetalle;

@Path("recepcion")
public class RecepcionResource implements Serializable {

    @Inject
    RecepcionService rService;

    /**
     * 200: ordenes pendientes con sus productos, de la mas antigua a la mas nueva.
     */
    @GET
    @Path("pendientes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response pendientes() {
        try {
            List<OrdenDetalle> ordenes = rService.pendientes();
            return Response.ok(ordenes)
                    .header("Total-registros", ordenes.size())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(500)
                    .header("App-Error", ex.getMessage())
                    .build();
        }
    }

    /**
     * 200: ordenes listas de las ultimas 24 horas (las que el cajero puede cobrar), de la mas antigua a la mas nueva.
     */
    @GET
    @Path("listas")
    @Produces({MediaType.APPLICATION_JSON})
    public Response listas() {
        try {
            List<OrdenDetalle> ordenes = rService.listas();
            return Response.ok(ordenes)
                    .header("Total-registros", ordenes.size())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(500)
                    .header("App-Error", ex.getMessage())
                    .build();
        }
    }

    /**
     * 204: la orden quedo marcada como lista (tambien si ya lo estaba).
     * 404: la orden no existe.
     */
    @POST
    @Path("{idOrden}/lista")
    public Response marcarLista(@PathParam("idOrden") UUID idOrden) {
        try {
            if (!rService.marcarLista(idOrden)) {
                return Response.status(404).build();
            }
            return Response.status(204).build();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(500)
                    .header("App-Error", ex.getMessage())
                    .build();
        }
    }
}