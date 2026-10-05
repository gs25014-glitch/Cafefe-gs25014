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
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caracteristica;

@ApplicationScoped
public class CaracteristicaRepository extends Repository<Caracteristica> {

    public CaracteristicaRepository() {
        super(Caracteristica.class);
    }

    public List<Caracteristica> listarPorTipo(UUID idTipoCaracteristica) {

        TypedQuery<Caracteristica> query = entityManager.createQuery(
                "SELECT c FROM Caracteristica c "
                + "WHERE c.idTipoCaracteristica = :idTipo "
                + "ORDER BY c.nombre",
                Caracteristica.class
        );

        query.setParameter("idTipo", idTipoCaracteristica);

        return query.getResultList();
    }
}