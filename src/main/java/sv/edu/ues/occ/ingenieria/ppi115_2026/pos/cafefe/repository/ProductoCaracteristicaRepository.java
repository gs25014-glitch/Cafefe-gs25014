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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoCaracteristica;

@ApplicationScoped
public class ProductoCaracteristicaRepository
        extends Repository<ProductoCaracteristica> {

    public ProductoCaracteristicaRepository() {
        super(ProductoCaracteristica.class);
    }

    public List<ProductoCaracteristica> listarPorProducto(UUID idProducto) {

        TypedQuery<ProductoCaracteristica> query = entityManager.createQuery(
                "SELECT p FROM ProductoCaracteristica p "
                + "WHERE p.idProducto = :idProducto",
                ProductoCaracteristica.class
        );

        query.setParameter("idProducto", idProducto);

        return query.getResultList();
    }
}