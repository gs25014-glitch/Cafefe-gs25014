package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.OrdenClienteService;

@Path("orden")
public class OrdenResource implements Serializable {

    @Inject
    OrdenClienteService oService;

    /**
     * Cuerpo del POST: {"nombreCliente": "Ana", "productos": ["id1", "id1", "id2"]}
     * Un id repetido significa mas de una unidad de ese producto.
     */
    public static class NuevaOrden {

        private String nombreCliente;
        private List<UUID> productos;

        public String getNombreCliente() {
            return nombreCliente;
        }

        public void setNombreCliente(String nombreCliente) {
            this.nombreCliente = nombreCliente;
        }

        public List<UUID> getProductos() {
            return productos;
        }

        public void setProductos(List<UUID> productos) {
            this.productos = productos;
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crear(NuevaOrden nueva, @Context UriInfo uriInfo) {

        if (nueva == null) {
            return Response.status(422)
                    .header("Wrong-Param", "cuerpo")
                    .build();
        }

        try {
            Orden creada = oService.crear(nueva.getNombreCliente(), nueva.getProductos());
            URI ubicacion = uriInfo.getAbsolutePathBuilder()
                    .path(creada.getIdOrden().toString())
                    .build();

            return Response.created(ubicacion)
                    .entity(creada)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (Exception ex) {
            // El servicio es transaccional: la excepcion puede llegar envuelta
            Throwable causa = ex instanceof IllegalArgumentException ? ex : ex.getCause();
            if (causa instanceof IllegalArgumentException) {
                return Response.status(422)
                        .header("Wrong-Param", causa.getMessage())
                        .build();
            }
            Logger.getLogger(getClass().getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(500)
                    .header("App-Error", ex.getMessage())
                    .build();
        }
    }
}