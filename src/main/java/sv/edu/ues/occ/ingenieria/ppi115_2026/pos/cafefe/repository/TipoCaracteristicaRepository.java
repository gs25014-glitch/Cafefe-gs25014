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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;

@ApplicationScoped
public class TipoCaracteristicaRepository extends Repository<TipoCaracteristica> {

    public TipoCaracteristicaRepository() {
        super(TipoCaracteristica.class);
    }

    public List<TipoCaracteristica> listarActivos() {

        TypedQuery<TipoCaracteristica> query = entityManager.createQuery(
                "SELECT t FROM TipoCaracteristica t "
                + "WHERE t.activo = true "
                + "ORDER BY t.nombre",
                TipoCaracteristica.class
        );

        return query.getResultList();
    }
}