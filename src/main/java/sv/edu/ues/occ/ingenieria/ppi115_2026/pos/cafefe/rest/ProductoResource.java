package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.rest;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CatalogoService;

@Path("producto")
public class ProductoResource implements Serializable {

    @Inject
    CatalogoService cService;

    /**
     * Productos de un tipo de producto: GET producto?tipoProducto={id}&first=0&max=50
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findRange(
            @QueryParam("tipoProducto") UUID tipoProducto,

            @QueryParam("first")
            @DefaultValue("0") int first,

            @QueryParam("max")
            @Max(50)
            @DefaultValue("50") int max) {

        if (tipoProducto != null && first >= 0 && max > 0 && max <= 50) {
            try {
                return Response.ok(cService.productosPorTipo(tipoProducto, first, max))
                        .type(MediaType.APPLICATION_JSON)
                        .header("Total-registros", cService.contarProductosPorTipo(tipoProducto))
                        .build();

            } catch (Exception ex) {
                Logger.getLogger(getClass().getName())
                        .log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(500)
                        .header("App-Error", ex.getMessage())
                        .build();
            }
        }

        return Response.status(422)
                .header("Wrong-Param", "tipoProducto,first,max")
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(
            @PathParam("id") UUID id) {

        if (id != null) {
            try {
                Producto encontrado = cService.buscarProducto(id);

                if (encontrado != null) {
                    return Response.ok(encontrado)
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }

                return Response.status(404).build();

            } catch (Exception ex) {
                Logger.getLogger(getClass().getName())
                        .log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(500)
                        .header("App-Error", ex.getMessage())
                        .build();
            }
        }

        return Response.status(422)
                .header("Wrong-Param", "id")
                .build();
    }
}