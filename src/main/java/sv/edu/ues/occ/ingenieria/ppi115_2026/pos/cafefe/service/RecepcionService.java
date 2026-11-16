package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.RecepcionRepository;

/**
 * Ordenes que ve el barista o cocinero: las pendientes, con sus productos, y marcarlas como listas.
 */
@ApplicationScoped
public class RecepcionService {

    public static final int MAX_ORDENES = 100;
    // El cajero solo ve las ordenes listas de las ultimas horas (nada se marca como cobrado)
    public static final int HORAS_CAJA = 24;
    private static final String PREFIJO_CLIENTE = "Cliente: ";

    @Inject
    RecepcionRepository recepcionRepository;

    /**
     * Un producto de una orden (si un producto se pidio dos veces, salen dos lineas).
     */
    public static class LineaOrden {

        private final UUID idOrdenProducto;
        private final UUID idProducto;
        private final String nombre;
        private final BigDecimal precio;

        public LineaOrden(UUID idOrdenProducto, UUID idProducto, String nombre, BigDecimal precio) {
            this.idOrdenProducto = idOrdenProducto;
            this.idProducto = idProducto;
            this.nombre = nombre;
            this.precio = precio;
        }

        public UUID getIdOrdenProducto() {
            return idOrdenProducto;
        }

        public UUID getIdProducto() {
            return idProducto;
        }

        public String getNombre() {
            return nombre;
        }

        public BigDecimal getPrecio() {
            return precio;
        }
    }

    /**
     * Una orden con su cliente y sus productos.
     */
    public static class OrdenDetalle {

        private final UUID idOrden;
        private final OffsetDateTime fechaCreacion;
        private final String cliente;
        private final List<LineaOrden> productos;

        public OrdenDetalle(UUID idOrden, OffsetDateTime fechaCreacion, String cliente, List<LineaOrden> productos) {
            this.idOrden = idOrden;
            this.fechaCreacion = fechaCreacion;
            this.cliente = cliente;
            this.productos = productos;
        }

        public UUID getIdOrden() {
            return idOrden;
        }

        public OffsetDateTime getFechaCreacion() {
            return fechaCreacion;
        }

        public String getCliente() {
            return cliente;
        }

        public List<LineaOrden> getProductos() {
            return productos;
        }
    }

    /**
     * Ordenes pendientes (que todavia no estan listas), de la mas antigua a la mas nueva.
     */
    @Transactional
    public List<OrdenDetalle> pendientes() {
        return ordenesPorEstado(false);
    }

    /**
     * Ordenes listas de las ultimas 24 horas, de la mas antigua a la mas nueva: las que el cajero puede cobrar.
     */
    @Transactional
    public List<OrdenDetalle> listas() {
        return armarDetalles(recepcionRepository.listasDesde(OffsetDateTime.now().minusHours(HORAS_CAJA), MAX_ORDENES));
    }

    /**
     * Marca la orden como lista. Si ya estaba lista no pasa nada.
     *
     * @return false si la orden no existe
     */
    @Transactional
    public boolean marcarLista(UUID idOrden) {
        return recepcionRepository.marcarLista(idOrden);
    }

    List<OrdenDetalle> ordenesPorEstado(boolean lista) {
        return armarDetalles(recepcionRepository.ordenesPorEstado(lista, MAX_ORDENES));
    }

    // Agrega a cada orden sus productos y el nombre del cliente
    private List<OrdenDetalle> armarDetalles(List<Orden> ordenes) {
        List<OrdenDetalle> salida = new ArrayList<>();
        if (ordenes.isEmpty()) {
            return salida;
        }

        Set<UUID> idsOrden = new HashSet<>();
        for (Orden orden : ordenes) {
            idsOrden.add(orden.getIdOrden());
        }

        // Tres consultas en total (ordenes, productos de las ordenes y nombres), sin importar cuantas ordenes haya
        Map<UUID, List<OrdenProducto>> lineasPorOrden = new HashMap<>();
        Set<UUID> idsProducto = new HashSet<>();
        for (OrdenProducto linea : recepcionRepository.lineasDeOrdenes(idsOrden)) {
            lineasPorOrden.computeIfAbsent(linea.getIdOrden(), k -> new ArrayList<>()).add(linea);
            idsProducto.add(linea.getIdProducto());
        }

        Map<UUID, String> nombres = new HashMap<>();
        if (!idsProducto.isEmpty()) {
            for (Producto producto : recepcionRepository.productosPorIds(idsProducto)) {
                nombres.put(producto.getIdProducto(), producto.getNombre());
            }
        }

        for (Orden orden : ordenes) {
            List<OrdenProducto> lineas = lineasPorOrden.getOrDefault(orden.getIdOrden(), new ArrayList<>());
            List<LineaOrden> productos = new ArrayList<>();
            for (OrdenProducto linea : lineas) {
                productos.add(new LineaOrden(linea.getIdOrdenProducto(), linea.getIdProducto(),
                        nombres.getOrDefault(linea.getIdProducto(), "Producto"), linea.getPrecio()));
            }
            salida.add(new OrdenDetalle(orden.getIdOrden(), orden.getFechaCreacion(),
                    clienteDe(lineas), productos));
        }
        return salida;
    }

    /**
     * El nombre del cliente se guardo en las observaciones como "Cliente: Ana".
     * Si la orden no la hizo un cliente (o no tiene productos), devuelve null.
     */
    static String clienteDe(List<OrdenProducto> lineas) {
        for (OrdenProducto linea : lineas) {
            String observaciones = linea.getObservaciones();
            if (observaciones != null && observaciones.startsWith(PREFIJO_CLIENTE)) {
                return observaciones.substring(PREFIJO_CLIENTE.length()).trim();
            }
        }
        return null;
    }
}