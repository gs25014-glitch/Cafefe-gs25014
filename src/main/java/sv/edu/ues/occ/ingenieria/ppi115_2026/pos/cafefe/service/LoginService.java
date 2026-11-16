package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Empleado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Rol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.LoginRepository;

/**
 * Login de la aplicacion web: el usuario es el nombre del empleado y la contrasena
 * es el identificador unico (UUID) del empleado.
 */
@ApplicationScoped
public class LoginService {

    /**
     * Un rol del empleado, tal como se le entrega a la aplicacion web.
     */
    public static class RolSesion {

        private final UUID idRol;
        private final String nombre;

        public RolSesion(UUID idRol, String nombre) {
            this.idRol = idRol;
            this.nombre = nombre;
        }

        public UUID getIdRol() {
            return idRol;
        }

        public String getNombre() {
            return nombre;
        }
    }

    /**
     * Los datos del empleado que inicio sesion (nunca incluye datos de mas).
     */
    public static class Sesion {

        private final UUID idEmpleado;
        private final String nombre;
        private final String apellido;
        private final List<RolSesion> roles;

        public Sesion(UUID idEmpleado, String nombre, String apellido, List<RolSesion> roles) {
            this.idEmpleado = idEmpleado;
            this.nombre = nombre;
            this.apellido = apellido;
            this.roles = roles;
        }

        public UUID getIdEmpleado() {
            return idEmpleado;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public List<RolSesion> getRoles() {
            return roles;
        }
    }

    @Inject
    LoginRepository repository;

    /**
     * @param usuario el nombre del empleado
     * @param contrasena el identificador unico (UUID) del empleado
     * @return los datos de la sesion, o null si el usuario o la contrasena no son correctos.
     * Si el empleado no tiene roles activos, la lista de roles viene vacia.
     */
    public Sesion iniciar(String usuario, String contrasena) {
        if (usuario == null || usuario.isBlank() || contrasena == null || contrasena.isBlank()) {
            return null;
        }

        UUID idEmpleado;
        try {
            idEmpleado = UUID.fromString(contrasena.trim());
        } catch (IllegalArgumentException ex) {
            return null; // la contrasena no tiene forma de UUID
        }

        Empleado empleado = repository.buscarActivo(idEmpleado, usuario.trim());
        if (empleado == null) {
            return null;
        }

        List<RolSesion> roles = new ArrayList<>();
        for (Rol rol : repository.rolesActivos(idEmpleado)) {
            roles.add(new RolSesion(rol.getIdRol(), rol.getNombre()));
        }
        return new Sesion(empleado.getIdEmpleado(), empleado.getNombre(), empleado.getApellido(), roles);
    }
}