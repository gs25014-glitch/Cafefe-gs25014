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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Factura;

@ApplicationScoped
public class FacturaRepository extends Repository<Factura> {

    public FacturaRepository() {
        super(Factura.class);
    }

    public List<Factura> listarPendientes() {
        TypedQuery<Factura> query = entityManager.createQuery(
                "SELECT f FROM Factura f "
                + "WHERE f.estado = :estado "
                + "ORDER BY f.fechaFacturacion DESC",
                Factura.class
        );

        query.setParameter("estado", "PENDIENTE");

        return query.getResultList();
    }
}