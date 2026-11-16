package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.CatalogoRepository;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.OrdenClienteRepository;

/**
 * Crea la orden que hace un cliente desde la aplicacion web (sin iniciar sesion).
 */
@ApplicationScoped
public class OrdenClienteService {

    public static final int MAX_PRODUCTOS = 50;
    public static final int MAX_NOMBRE = 100;

    @Inject
    OrdenClienteRepository ordenRepository;

    @Inject
    CatalogoRepository catalogoRepository;

    /**
     * Guarda la orden y sus productos. Si algo falla, no se guarda nada.
     *
     * @param nombreCliente nombre del cliente; se agrega a las observaciones de cada producto
     * @param idsProductos ids de los productos; si un id se repite, se pidio mas de una unidad
     * @return la orden creada
     * @throws IllegalArgumentException si el nombre o los productos no son validos;
     * el mensaje dice cual dato fallo ("nombreCliente" o "productos")
     */
    @Transactional
    public Orden crear(String nombreCliente, List<UUID> idsProductos) {
        if (nombreCliente == null || nombreCliente.isBlank() || nombreCliente.trim().length() > MAX_NOMBRE) {
            throw new IllegalArgumentException("nombreCliente");
        }
        if (idsProductos == null || idsProductos.isEmpty() || idsProductos.size() > MAX_PRODUCTOS) {
            throw new IllegalArgumentException("productos");
        }

        Orden orden = new Orden();
        orden.setIdOrden(UUID.randomUUID());
        orden.setFechaCreacion(OffsetDateTime.now());
        // idEmpleadoRol queda vacio: la orden la hace un cliente, no un empleado

        List<OrdenProducto> productos = new ArrayList<>();
        for (UUID idProducto : idsProductos) {
            Producto producto = idProducto == null ? null : catalogoRepository.buscarPorId(idProducto);
            if (producto == null || !Boolean.TRUE.equals(producto.getActivo())) {
                throw new IllegalArgumentException("productos");
            }
            OrdenProducto linea = new OrdenProducto();
            linea.setIdOrdenProducto(UUID.randomUUID());
            linea.setIdOrden(orden.getIdOrden());
            linea.setIdProducto(producto.getIdProducto());
            linea.setPrecio(producto.getPrecioSugerido()); // el precio lo pone el servidor, no el cliente
            linea.setObservaciones("Cliente: " + nombreCliente.trim());
            productos.add(linea);
        }

        ordenRepository.guardarConProductos(orden, productos);
        return orden;
    }
}