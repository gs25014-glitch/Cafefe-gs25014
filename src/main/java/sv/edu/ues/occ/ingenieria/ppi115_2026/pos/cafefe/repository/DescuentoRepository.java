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
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Descuento;

@ApplicationScoped
public class DescuentoRepository extends Repository<Descuento> {

    public DescuentoRepository() {
        super(Descuento.class);
    }

    public List<Descuento> listarPorTipo(UUID idTipoDescuento) {

        TypedQuery<Descuento> query = entityManager.createQuery(
                "SELECT d FROM Descuento d "
                + "WHERE d.idTipoDescuento = :idTipo "
                + "ORDER BY d.nombre",
                Descuento.class
        );

        query.setParameter("idTipo", idTipoDescuento);

        return query.getResultList();
    }
}
