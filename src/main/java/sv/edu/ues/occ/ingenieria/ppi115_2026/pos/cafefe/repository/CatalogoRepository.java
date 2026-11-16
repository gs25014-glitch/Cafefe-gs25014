package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;

/**
 * Consultas del catalogo de productos que usa la pantalla de ordenes.
 */
@ApplicationScoped
public class CatalogoRepository extends Repository<Producto> {

    private static final String PRODUCTOS_DEL_TIPO
            = "FROM Producto p WHERE p.idProducto IN "
            + "(SELECT t.idProducto FROM ProductoTipoProducto t WHERE t.idTipoProducto = :idTipo)";

    public CatalogoRepository() {
        super(Producto.class);
    }

    /**
     * Productos que pertenecen a un tipo de producto, ordenados por nombre.
     */
    public List<Producto> productosPorTipo(UUID idTipoProducto, int first, int max) {
        return entityManager
                .createQuery("SELECT p " + PRODUCTOS_DEL_TIPO + " ORDER BY p.nombre", Producto.class)
                .setParameter("idTipo", idTipoProducto)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }

    /**
     * Cuantos productos tiene un tipo de producto.
     */
    public long contarProductosPorTipo(UUID idTipoProducto) {
        return entityManager
                .createQuery("SELECT COUNT(p) " + PRODUCTOS_DEL_TIPO, Long.class)
                .setParameter("idTipo", idTipoProducto)
                .getSingleResult();
    }
}