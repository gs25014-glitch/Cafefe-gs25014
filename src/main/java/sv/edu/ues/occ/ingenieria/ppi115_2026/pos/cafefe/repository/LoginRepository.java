package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Empleado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Rol;

/**
 * Consultas que necesita el login de la aplicacion web.
 */
@ApplicationScoped
public class LoginRepository extends Repository<Empleado> {

    public LoginRepository() {
        super(Empleado.class);
    }

    /**
     * Busca al empleado activo que tenga ese id y ese nombre (sin importar mayusculas).
     *
     * @return el empleado, o null si no existe o no esta activo
     */
    public Empleado buscarActivo(UUID idEmpleado, String nombre) {
        List<Empleado> lista = entityManager
                .createQuery("SELECT e FROM Empleado e WHERE e.idEmpleado = :id "
                        + "AND e.activo = true AND LOWER(e.nombre) = :nombre", Empleado.class)
                .setParameter("id", idEmpleado)
                .setParameter("nombre", nombre.toLowerCase(Locale.ROOT))
                .getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }

    /**
     * Los roles activos que tiene asignados el empleado (asignacion activa y rol activo).
     */
    public List<Rol> rolesActivos(UUID idEmpleado) {
        return entityManager
                .createQuery("SELECT r FROM Rol r WHERE r.activo = true AND r.idRol IN "
                        + "(SELECT er.idRol FROM EmpleadoRol er WHERE er.idEmpleado = :id AND er.activo = true) "
                        + "ORDER BY r.nombre", Rol.class)
                .setParameter("id", idEmpleado)
                .getResultList();
    }
}