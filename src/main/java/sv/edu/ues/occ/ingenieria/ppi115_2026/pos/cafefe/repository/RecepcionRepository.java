package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;

/**
 * Consultas de la pantalla de recepcion: ordenes pendientes (o listas) con sus productos.
 */
@ApplicationScoped
public class RecepcionRepository extends Repository<Orden> {

    public RecepcionRepository() {
        super(Orden.class);
    }

    /**
     * Ordenes pendientes (lista = false) o listas (lista = true), de la mas antigua a la mas nueva.
     */
    public List<Orden> ordenesPorEstado(boolean lista, int max) {
        return entityManager
                .createQuery("SELECT o FROM Orden o WHERE o.lista = :lista ORDER BY o.fechaCreacion", Orden.class)
                .setParameter("lista", lista)
                .setMaxResults(max)
                .getResultList();
    }

    /**
     * Ordenes listas creadas desde una fecha, de la mas antigua a la mas nueva (las ve el cajero).
     */
    public List<Orden> listasDesde(OffsetDateTime desde, int max) {
        return entityManager
                .createQuery("SELECT o FROM Orden o WHERE o.lista = true AND o.fechaCreacion >= :desde ORDER BY o.fechaCreacion", Orden.class)
                .setParameter("desde", desde)
                .setMaxResults(max)
                .getResultList();
    }

    /**
     * Los productos de varias ordenes a la vez. La lista de ids no puede estar vacia.
     */
    public List<OrdenProducto> lineasDeOrdenes(Collection<UUID> idsOrden) {
        return entityManager
                .createQuery("SELECT l FROM OrdenProducto l WHERE l.idOrden IN :ids", OrdenProducto.class)
                .setParameter("ids", idsOrden)
                .getResultList();
    }

    /**
     * Los datos de varios productos a la vez (para mostrar sus nombres). La lista de ids no puede estar vacia.
     */
    public List<Producto> productosPorIds(Collection<UUID> idsProducto) {
        return entityManager
                .createQuery("SELECT p FROM Producto p WHERE p.idProducto IN :ids", Producto.class)
                .setParameter("ids", idsProducto)
                .getResultList();
    }

    /**
     * Marca una orden como lista. Debe llamarse dentro de una transaccion.
     *
     * @return false si la orden no existe
     */
    public boolean marcarLista(UUID idOrden) {
        Orden orden = entityManager.find(Orden.class, idOrden);
        if (orden == null) {
            return false;
        }
        orden.setLista(true);
        entityManager.merge(orden);
        return true;
    }
}