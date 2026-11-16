package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.LoginService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.LoginService.Sesion;

@Path("login")
public class LoginResource implements Serializable {

    @Inject
    LoginService lService;

    /**
     * Cuerpo del POST: {"usuario": "Juan", "contrasena": "id-unico-del-empleado"}
     */
    public static class SolicitudLogin {

        private String usuario;
        private String contrasena;

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }

    /**
     * 200: datos del empleado y sus roles.
     * 401: usuario o contrasena incorrectos.
     * 403: datos correctos, pero el empleado no tiene roles activos.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response iniciar(SolicitudLogin solicitud) {

        if (solicitud == null) {
            return Response.status(422)
                    .header("Wrong-Param", "cuerpo")
                    .build();
        }

        try {
            Sesion sesion = lService.iniciar(solicitud.getUsuario(), solicitud.getContrasena());

            if (sesion == null) {
                return Response.status(401).build();
            }
            if (sesion.getRoles().isEmpty()) {
                return Response.status(403).build();
            }

            return Response.ok(sesion)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (Exception ex) {
            // No se escribe la solicitud en el log: trae la contrasena
            Logger.getLogger(getClass().getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(500)
                    .header("App-Error", ex.getMessage())
                    .build();
        }
    }
}