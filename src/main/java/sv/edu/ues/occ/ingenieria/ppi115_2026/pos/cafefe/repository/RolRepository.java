/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

/**
 *
 * @author kelvi
 */

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Rol;

@ApplicationScoped
public class RolRepository extends Repository<Rol> {

    public RolRepository() {
        super(Rol.class);
    }

    public List<Rol> listarActivos() {

        TypedQuery<Rol> query = entityManager.createQuery(
                "SELECT r FROM Rol r "
                + "WHERE r.activo = true "
                + "ORDER BY r.nombre",
                Rol.class
        );

        return query.getResultList();
    }
}