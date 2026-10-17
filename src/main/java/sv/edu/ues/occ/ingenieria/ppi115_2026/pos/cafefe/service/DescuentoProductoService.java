package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

/**
 *
 * @author kelvi
 */

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Descuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.DescuentoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoDescuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.DescuentoProductoRepository;

@ApplicationScoped
public class DescuentoProductoService {

    @Inject
    private DescuentoProductoRepository repository;

    @Inject
    private DescuentoService descuentoService;

    @Inject
    private TipoDescuentoService tipoDescuentoService;

    public List<DescuentoProducto> listar() {
        return repository.listar();
    }

    public List<DescuentoProducto> listarPorProducto(UUID idProducto) {
        return repository.listarPorProducto(idProducto);
    }

    @Transactional
    public void eliminar(UUID id) {
        repository.eliminar(id);
    }

    // Busca si el producto tiene asignado ese descuento y si la fecha de la
    // orden cae dentro de su rango. Si no aplica devuelve null.
    public DescuentoProducto buscarAplicable(UUID idProducto, UUID idDescuento,
            OffsetDateTime fechaOrden) {

        for (DescuentoProducto dp : repository.listarPorProducto(idProducto)) {

            if (!dp.getIdDescuento().equals(idDescuento)) {
                continue;
            }

            if (dp.getFechaDesde() != null && fechaOrden.isBefore(dp.getFechaDesde())) {
                continue;
            }

            if (dp.getFechaHasta() != null && fechaOrden.isAfter(dp.getFechaHasta())) {
                continue;
            }

            return dp;
        }

        return null;
    }

    // Porcentaje que le corresponde a un descuento (el de su tipo)
    public Integer porcentaje(Descuento descuento) {
        return tipoDescuentoService.buscarPorId(descuento.getIdTipoDescuento())
                .getDescuentoMaximo();
    }

    // Explica por qué un descuento no se puede aplicar a un producto
    public String motivoNoAplica(UUID idProducto, UUID idDescuento,
            OffsetDateTime fechaOrden) {

        for (DescuentoProducto dp : repository.listarPorProducto(idProducto)) {

            if (!dp.getIdDescuento().equals(idDescuento)) {
                continue;
            }

            if (dp.getFechaDesde() != null && fechaOrden.isBefore(dp.getFechaDesde())) {
                return "la orden es antes del inicio de la asignación ("
                        + formato(dp.getFechaDesde()) + ")";
            }

            if (dp.getFechaHasta() != null && fechaOrden.isAfter(dp.getFechaHasta())) {
                return "la orden es después del fin de la asignación ("
                        + formato(dp.getFechaHasta()) + ")";
            }

            return "ya está aplicado";
        }

        // No tiene ese descuento: se dice cuáles sí tiene asignados
        List<DescuentoProducto> asignados = repository.listarPorProducto(idProducto);

        if (asignados.isEmpty()) {
            return "no tiene ningún descuento asignado (asígnelo en Productos)";
        }

        String nombres = "";

        for (DescuentoProducto dp : asignados) {
            Descuento d = descuentoService.buscarPorId(dp.getIdDescuento());
            nombres = nombres + " [" + d.getNombre() + " " + formato(d.getFechaDesde()) + "]";
        }

        return "no tiene asignado ese descuento, solo tiene:" + nombres;
    }

    // Solo la fecha: "2026-11-01"
    private String formato(OffsetDateTime fecha) {
        return fecha.atZoneSameInstant(ZoneId.of("America/El_Salvador")).toLocalDate().toString();
    }

    // Guarda la asignación. Si algo no cumple lanza una excepción con el
    // mensaje del error (la usa descuentoProductoBean).
    @Transactional
    public void guardar(DescuentoProducto dp) {

        String error = validar(dp);

        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Si no trae id se le pone uno nuevo
        if (dp.getIdDescuentoProducto() == null) {
            dp.setIdDescuentoProducto(UUID.randomUUID());
        }

        repository.guardar(dp);
    }

    // Igual que guardar, pero devuelve null si todo salió bien o el
    // mensaje del error (la usa productoBean).
    @Transactional
    public String asignar(DescuentoProducto dp) {

        String error = validar(dp);

        if (error != null) {
            return error;
        }

        if (dp.getIdDescuentoProducto() == null) {
            dp.setIdDescuentoProducto(UUID.randomUUID());
        }

        repository.guardar(dp);

        return null;
    }

    // Valida el valor máximo y las fechas. Devuelve null si está bien.
    private String validar(DescuentoProducto dp) {

        Descuento descuento = descuentoService.buscarPorId(dp.getIdDescuento());

        TipoDescuento tipo =
                tipoDescuentoService.buscarPorId(descuento.getIdTipoDescuento());

        if (!Boolean.TRUE.equals(tipo.getActivo())) {
            return "El tipo del descuento no está activo.";
        }

        // Si no se indica el valor, se usa el porcentaje del tipo de descuento
        if (dp.getValor() == null) {
            dp.setValor(tipo.getDescuentoMaximo());
        }

        // Límite de monto del tipo de descuento
        if (dp.getValor() == null || dp.getValor() <= 0) {
            return "El valor del descuento debe ser mayor que cero.";
        }

        if (tipo.getDescuentoMaximo() != null
                && dp.getValor() > tipo.getDescuentoMaximo()) {
            return "El valor " + dp.getValor() + "% sobrepasa el máximo del tipo ("
                    + tipo.getDescuentoMaximo() + "%).";
        }

        // Si no se ponen fechas se usan las del descuento
        if (dp.getFechaDesde() == null) {
            dp.setFechaDesde(descuento.getFechaDesde());
        }

        if (dp.getFechaHasta() == null) {
            dp.setFechaHasta(descuento.getFechaHasta());
        }

        // Fechas
        if (dp.getFechaHasta() != null
                && dp.getFechaHasta().isBefore(dp.getFechaDesde())) {
            return "La fecha hasta no puede ser antes de la fecha desde.";
        }

        if (dp.getFechaDesde().isBefore(descuento.getFechaDesde())) {
            return "La fecha desde es antes del inicio del descuento ("
                    + descuento.getFechaDesde() + ").";
        }

        if (descuento.getFechaHasta() != null
                && (dp.getFechaHasta() == null
                || dp.getFechaHasta().isAfter(descuento.getFechaHasta()))) {
            return "La fecha hasta pasa del fin del descuento ("
                    + descuento.getFechaHasta() + ").";
        }

        // No repetir el mismo descuento en el mismo producto
        for (DescuentoProducto otro : repository.listarPorProducto(dp.getIdProducto())) {
            if (otro.getIdDescuento().equals(dp.getIdDescuento())) {
                return "Ese descuento ya está asignado a este producto.";
            }
        }

        return null;
    }
}