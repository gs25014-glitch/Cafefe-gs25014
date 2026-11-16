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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoProductoService;

@Path("tipoProducto")
public class TipoProductoResource implements Serializable {

    @Inject
    TipoProductoService tService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findRange(
            @QueryParam("first")
            @DefaultValue("0") int first,

            @QueryParam("max")
            @Max(50)
            @DefaultValue("50") int max) {

        if (first >= 0 && max > 0 && max <= 50) {
            try {
                // Hay pocos tipos de producto, por eso se ordenan y se cortan aqui
                List<TipoProducto> todos = new ArrayList<>(tService.listar());
                todos.sort(Comparator.comparing(TipoProducto::getNombre,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

                int desde = Math.min(first, todos.size());
                int hasta = Math.min(desde + max, todos.size());

                return Response.ok(new ArrayList<>(todos.subList(desde, hasta)))
                        .type(MediaType.APPLICATION_JSON)
                        .header("Total-registros", todos.size())
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
                .header("Wrong-Param", "first,max")
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(
            @PathParam("id") UUID id) {

        if (id != null) {
            try {
                TipoProducto encontrado = tService.buscarPorId(id);

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