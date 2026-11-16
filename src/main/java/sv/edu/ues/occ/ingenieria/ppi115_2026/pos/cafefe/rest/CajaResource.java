/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caja;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CajaService;

/**
 *
 * @author kelvi
 */
@Path("caja")
public class CajaResource implements Serializable {

    @Inject
    CajaService cService;

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
                return Response.ok(cService.findRange(first, max))
                        .type(MediaType.APPLICATION_JSON)
                        .header("Total-registros", cService.contar())
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
                Caja encontrado = cService.buscarPorId(id);

                if (encontrado != null) { // 200
                    return Response.ok(encontrado)
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }

                return Response.status(404).build(); // no existe

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