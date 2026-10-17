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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.DescuentoProducto;

@ApplicationScoped
public class DescuentoProductoRepository
        extends Repository<DescuentoProducto> {

    public DescuentoProductoRepository() {
        super(DescuentoProducto.class);
    }

    public List<DescuentoProducto> listarPorProducto(UUID idProducto) {

        TypedQuery<DescuentoProducto> query = entityManager.createQuery(
                "SELECT d FROM DescuentoProducto d "
                + "WHERE d.idProducto = :idProducto",
                DescuentoProducto.class
        );

        query.setParameter("idProducto", idProducto);

        return query.getResultList();
    }

    public List<DescuentoProducto> listarPorDescuento(UUID idDescuento) {

        TypedQuery<DescuentoProducto> query = entityManager.createQuery(
                "SELECT d FROM DescuentoProducto d "
                + "WHERE d.idDescuento = :idDescuento",
                DescuentoProducto.class
        );

        query.setParameter("idDescuento", idDescuento);

        return query.getResultList();
    }
}