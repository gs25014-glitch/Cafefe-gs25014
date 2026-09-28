/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.EmpleadoRol;

@ApplicationScoped
public class EmpleadoRolRepository extends Repository<EmpleadoRol> {

    public EmpleadoRolRepository() {
        super(EmpleadoRol.class);
    }

    public EmpleadoRol buscarActivoPorEmpleado(UUID idEmpleado) {

        TypedQuery<EmpleadoRol> query = entityManager.createQuery(
                "SELECT er FROM EmpleadoRol er "
                + "WHERE er.idEmpleado = :idEmpleado "
                + "AND er.activo = true",
                EmpleadoRol.class
        );

        query.setParameter("idEmpleado", idEmpleado);

        return query.getResultStream().findFirst().orElse(null);
    }
}