package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;

/**
 * Guarda una orden junto con sus productos.
 */
@ApplicationScoped
public class OrdenClienteRepository extends Repository<Orden> {

    public OrdenClienteRepository() {
        super(Orden.class);
    }

    /**
     * Debe llamarse dentro de una transaccion.
     */
    public void guardarConProductos(Orden orden, List<OrdenProducto> productos) {
        guardar(orden);
        flush(); // la orden debe existir en la base antes que sus productos (llave foranea)
        for (OrdenProducto producto : productos) {
            entityManager.persist(producto);
        }
    }
}