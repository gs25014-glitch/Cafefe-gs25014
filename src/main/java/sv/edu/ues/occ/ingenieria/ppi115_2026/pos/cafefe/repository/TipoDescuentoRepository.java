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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoDescuento;

@ApplicationScoped
public class TipoDescuentoRepository extends Repository<TipoDescuento> {

    public TipoDescuentoRepository() {
        super(TipoDescuento.class);
    }

    public List<TipoDescuento> listarActivos() {

        TypedQuery<TipoDescuento> query = entityManager.createQuery(
                "SELECT t FROM TipoDescuento t "
                + "WHERE t.activo = true "
                + "ORDER BY t.nombre",
                TipoDescuento.class
        );

        return query.getResultList();
    }
}