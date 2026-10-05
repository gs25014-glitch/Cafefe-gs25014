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
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoTipoProducto;

@ApplicationScoped
public class ProductoTipoProductoRepository
        extends Repository<ProductoTipoProducto> {

    public ProductoTipoProductoRepository() {
        super(ProductoTipoProducto.class);
    }

    public ProductoTipoProducto buscarPorProducto(UUID idProducto) {

        TypedQuery<ProductoTipoProducto> query = entityManager.createQuery(
                "SELECT p FROM ProductoTipoProducto p "
                + "WHERE p.idProducto = :idProducto",
                ProductoTipoProducto.class
        );

        query.setParameter("idProducto", idProducto);

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}