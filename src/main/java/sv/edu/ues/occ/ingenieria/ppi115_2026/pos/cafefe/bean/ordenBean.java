/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caja;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Descuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.DescuentoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Empleado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Rol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CajaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.DescuentoProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.DescuentoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.EmpleadoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.OrdenProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.OrdenService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.RolService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.EmpleadoRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.EmpleadoRolService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Factura;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;

@Named("ordenBean")
@ViewScoped
public class ordenBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // El calendario manda la fecha sin zona, se toma como hora de El Salvador
    @Inject
    private OrdenService ordenService;

    @Inject
    private OrdenProductoService ordenProductoService;

    @Inject
    private EmpleadoService empleadoService;

    @Inject
    private ProductoService productoService;

    @Inject
    private EmpleadoRolService empleadoRolService;

    @Inject
    private RolService rolService;

    @Inject
    private CajaService cajaService;

    @Inject
    private DescuentoService descuentoService;

    @Inject
    private DescuentoProductoService descuentoProductoService;

    private static final ZoneId ZONA = ZoneId.of("America/El_Salvador");

    private List<Empleado> empleados;
    private List<Producto> productos;
    private List<Caja> cajas;
    private List<Descuento> descuentos;
    private List<OrdenProducto> productosOrden;

    // Órdenes ya guardadas y sus productos (para la tabla de abajo)
    private List<Orden> ordenesGuardadas;
    private Map<UUID, List<OrdenProducto>> detallesPorOrden;

    // Guarda "idDetalle|idDescuento" de los descuentos ya aplicados
    private List<String> descuentosAplicados;

    private Orden orden;
    private OrdenProducto ordenProducto;

    private UUID empleadoSeleccionado;
    private UUID productoSeleccionado;
    private UUID cajaSeleccionada;
    private UUID descuentoSeleccionado;

    private String fechaOrdenTexto;

    @PostConstruct
    public void inicializar() {
        cargarMeseros();
        productos = productoService.listar();
        cajas = cajaService.listar();
        descuentos = descuentoService.listar();

        orden = new Orden();
        ordenProducto = new OrdenProducto();

        productosOrden = new ArrayList<>();
        descuentosAplicados = new ArrayList<>();

        fechaOrdenTexto = fechaActual();

        recargarOrdenes();
    }

    // Carga las órdenes guardadas (la más reciente primero) con sus productos
    private void recargarOrdenes() {

        ordenesGuardadas = new ArrayList<>(ordenService.listar());
        ordenesGuardadas.sort((a, b) -> b.getFechaCreacion().compareTo(a.getFechaCreacion()));

        detallesPorOrden = new HashMap<>();

        for (OrdenProducto detalle : ordenProductoService.listar()) {
            detallesPorOrden
                    .computeIfAbsent(detalle.getIdOrden(), k -> new ArrayList<>())
                    .add(detalle);
        }
    }

    public List<Orden> getOrdenesGuardadas() {
        return ordenesGuardadas;
    }

    public List<OrdenProducto> detallesDeOrden(Orden o) {
        return detallesPorOrden.getOrDefault(o.getIdOrden(), new ArrayList<>());
    }

    public BigDecimal totalDeOrden(Orden o) {

        BigDecimal total = BigDecimal.ZERO;

        for (OrdenProducto detalle : detallesDeOrden(o)) {
            total = total.add(detalle.getPrecio());
        }

        return total;
    }

    public String fechaDeOrden(Orden o) {
        return textoFecha(o.getFechaCreacion());
    }

    // "Nombre Apellido - Rol" del empleado que tomó la orden
    public String empleadoDeOrden(Orden o) {

        EmpleadoRol empleadoRol = empleadoRolService.buscarPorId(o.getIdEmpleadoRol());

        if (empleadoRol == null) {
            return "";
        }

        Empleado empleado = empleadoService.buscarPorId(empleadoRol.getIdEmpleado());
        Rol rol = rolService.buscarPorId(empleadoRol.getIdRol());

        return empleado.getNombre() + " " + empleado.getApellido() + " - " + rol.getNombre();
    }

    // Solo los empleados activos que tienen el rol de camarero
    private void cargarMeseros() {

        empleados = new ArrayList<>();

        for (Empleado e : empleadoService.listar()) {

            if (Boolean.TRUE.equals(e.getActivo()) && esCamarero(e.getIdEmpleado())) {
                empleados.add(e);
            }
        }
    }

    private boolean esCamarero(UUID idEmpleado) {

        EmpleadoRol empleadoRol =
                empleadoRolService.buscarActivoPorEmpleado(idEmpleado);

        if (empleadoRol == null) {
            return false;
        }

        Rol rol = rolService.buscarPorId(empleadoRol.getIdRol());

        return rol != null
                && Boolean.TRUE.equals(rol.getActivo())
                && rol.getNombre() != null
                && rol.getNombre().trim().equalsIgnoreCase("camarero");
    }

    // Nombre del rol del empleado, para mostrarlo en la lista
    public String rolDeEmpleado(Empleado empleado) {

        EmpleadoRol empleadoRol =
                empleadoRolService.buscarActivoPorEmpleado(empleado.getIdEmpleado());

        if (empleadoRol == null) {
            return "Sin rol";
        }

        return rolService.buscarPorId(empleadoRol.getIdRol()).getNombre();
    }

    private String fechaActual() {
        return LocalDate.now(ZONA).toString();
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<OrdenProducto> getProductosOrden() {
        return productosOrden;
    }

    public List<Caja> getCajas() {
        return cajas;
    }

    public List<Descuento> getDescuentos() {
        return descuentos;
    }

    public Orden getOrden() {
        return orden;
    }

    public OrdenProducto getOrdenProducto() {
        return ordenProducto;
    }

    public UUID getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(UUID empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public UUID getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public UUID getCajaSeleccionada() {
        return cajaSeleccionada;
    }

    public void setCajaSeleccionada(UUID cajaSeleccionada) {
        this.cajaSeleccionada = cajaSeleccionada;
    }

    public void setProductoSeleccionado(UUID productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public UUID getDescuentoSeleccionado() {
        return descuentoSeleccionado;
    }

    public void setDescuentoSeleccionado(UUID descuentoSeleccionado) {
        this.descuentoSeleccionado = descuentoSeleccionado;
    }

    public String getFechaOrdenTexto() {
        return fechaOrdenTexto;
    }

    public void setFechaOrdenTexto(String fechaOrdenTexto) {
        this.fechaOrdenTexto = fechaOrdenTexto;
    }

    public String nombreProducto(UUID idProducto) {

        Producto producto = productoService.buscarPorId(idProducto);

        return producto != null ? producto.getNombre() : "";
    }

    public void agregarProducto() {

        if (productoSeleccionado == null) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Seleccione un producto.");
            return;
        }

        Producto producto = productoService.buscarPorId(productoSeleccionado);

        if (producto == null) {
            return;
        }

        OrdenProducto detalle = new OrdenProducto();

        detalle.setIdOrdenProducto(UUID.randomUUID());
        detalle.setIdProducto(producto.getIdProducto());
        detalle.setPrecio(producto.getPrecioSugerido());

        productosOrden.add(detalle);

        productoSeleccionado = null;
    }

    // Aplica el descuento elegido a los productos de la orden que lo tengan
    // asignado y cuyo rango de fechas incluya la fecha de la orden
    public void aplicarDescuento() {

        if (descuentoSeleccionado == null) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Seleccione un descuento.");
            return;
        }

        if (productosOrden.isEmpty()) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Agregue productos a la orden primero.");
            return;
        }

        OffsetDateTime fechaOrden;

        try {
            fechaOrden = textoAFecha(fechaOrdenTexto);
        } catch (DateTimeParseException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Seleccione la fecha de la orden con el calendario.");
            return;
        }

        try {

            Descuento descuento = descuentoService.buscarPorId(descuentoSeleccionado);

            int aplicados = 0;
            int yaAplicados = 0;
            String motivos = "";

            for (OrdenProducto detalle : productosOrden) {

                String clave = detalle.getIdOrdenProducto() + "|" + descuentoSeleccionado;

                // No aplicar el mismo descuento dos veces al mismo producto
                if (descuentosAplicados.contains(clave)) {
                    yaAplicados++;
                    continue;
                }

                DescuentoProducto dp = descuentoProductoService.buscarAplicable(
                        detalle.getIdProducto(), descuentoSeleccionado, fechaOrden);

                if (dp == null) {
                    motivos = motivos + " " + nombreProducto(detalle.getIdProducto()) + ": "
                            + descuentoProductoService.motivoNoAplica(
                                    detalle.getIdProducto(), descuentoSeleccionado, fechaOrden) + ".";
                    continue;
                }

                // El porcentaje se calcula sobre el precio actual de la linea, es decir,
                // sobre el precio que quedo despues de los descuentos anteriores
                BigDecimal monto = detalle.getPrecio()
                        .multiply(BigDecimal.valueOf(dp.getValor()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                // El precio no puede quedar negativo
                if (monto.compareTo(detalle.getPrecio()) > 0) {
                    monto = detalle.getPrecio();
                }

                detalle.setPrecio(detalle.getPrecio().subtract(monto));

                String comentario = "Descuento " + descuento.getNombre() + " "
                        + dp.getValor() + "%: -$" + monto;

                if (detalle.getObservaciones() == null) {
                    detalle.setObservaciones(comentario);
                } else {
                    detalle.setObservaciones(detalle.getObservaciones() + " | " + comentario);
                }

                descuentosAplicados.add(clave);
                aplicados++;
            }

            if (aplicados == 0 && yaAplicados > 0) {
                mensaje(FacesMessage.SEVERITY_WARN,
                        "El descuento \"" + descuento.getNombre() + "\" ya estaba aplicado.");
            } else if (aplicados == 0) {
                mensaje(FacesMessage.SEVERITY_WARN,
                        "El descuento \"" + descuento.getNombre()
                        + "\" no se aplicó a ningún producto (fecha de la orden: "
                        + fechaOrdenTexto + ")." + motivos);
            } else {
                mensaje(FacesMessage.SEVERITY_INFO,
                        "Descuento \"" + descuento.getNombre() + "\" aplicado a "
                        + aplicados + " producto(s).");

                if (!motivos.isEmpty()) {
                    mensaje(FacesMessage.SEVERITY_WARN, "No se aplicó a:" + motivos);
                }
            }

        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR,
                    "No se pudo aplicar el descuento. Motivo: " + e.getMessage());
        }
    }

    // Nombre del descuento con su rango, para distinguir descuentos parecidos
    public String etiquetaDescuento(Descuento d) {

        String desde = d.getFechaDesde() == null ? "-" : textoFecha(d.getFechaDesde());
        String hasta = d.getFechaHasta() == null ? "sin fin" : textoFecha(d.getFechaHasta());

        return d.getNombre() + " (" + desde + " a " + hasta + ")";
    }

    private String textoFecha(OffsetDateTime fecha) {
        return fecha.atZoneSameInstant(ZONA).toLocalDate().toString();
    }

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (OrdenProducto detalle : productosOrden) {

            if (detalle.getPrecio() != null) {
                total = total.add(detalle.getPrecio());
            }
        }

        return total;
    }

    public void guardarOrden() {

        if (empleadoSeleccionado == null) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Seleccione un empleado.");
            return;
        }

        // Solo el camarero puede tomar la orden
        if (!esCamarero(empleadoSeleccionado)) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Solo un empleado con rol camarero puede tomar la orden.");
            return;
        }

        if (cajaSeleccionada == null) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Seleccione una caja.");
            return;
        }

        if (productosOrden.isEmpty()) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Agregue productos a la orden.");
            return;
        }

        OffsetDateTime fechaOrden;

        try {
            fechaOrden = textoAFecha(fechaOrdenTexto);
        } catch (DateTimeParseException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Seleccione la fecha de la orden con el calendario.");
            return;
        }

        EmpleadoRol empleadoRol =
                empleadoRolService.buscarActivoPorEmpleado(empleadoSeleccionado);

        if (empleadoRol == null) {
            return;
        }

        // 1. Armar la orden con sus productos
        orden.setIdOrden(UUID.randomUUID());
        orden.setIdEmpleadoRol(empleadoRol.getIdEmpleadoRol());
        orden.setFechaCreacion(fechaOrden);

        for (OrdenProducto detalle : productosOrden) {
            detalle.setIdOrden(orden.getIdOrden());
        }

        // 2. Armar la factura
        Factura factura = new Factura();

        factura.setIdFactura(UUID.randomUUID());
        factura.setIdCaja(cajaSeleccionada);
        factura.setIdEmpleadoRol(empleadoRol.getIdEmpleadoRol());
        factura.setCliente("Consumidor final");
        factura.setFechaFacturacion(OffsetDateTime.now());
        factura.setEstado("PENDIENTE");
        factura.setObservaciones(null);

        // 3. Relacionar la factura con cada producto de la orden
        List<FacturaOrdenProducto> detallesFactura = new ArrayList<>();

        for (OrdenProducto detalle : productosOrden) {

            FacturaOrdenProducto facturaDetalle =
                    new FacturaOrdenProducto();

            facturaDetalle.setIdFacturaOrdenProducto(UUID.randomUUID());
            facturaDetalle.setIdFactura(factura.getIdFactura());
            facturaDetalle.setIdOrdenProducto(
                    detalle.getIdOrdenProducto()
            );
            facturaDetalle.setPrecio(detalle.getPrecio());
            facturaDetalle.setObservaciones(detalle.getObservaciones());

            detallesFactura.add(facturaDetalle);
        }

        // 4. Guardar todo junto: si algo falla no se guarda nada
        try {
            ordenService.guardarCompleta(orden, productosOrden, factura, detallesFactura);
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR,
                    "No se pudo guardar la orden, no se guardó nada. Motivo: " + e.getMessage());
            return;
        }

        // 5. Limpiar la pantalla
        orden = new Orden();
        productosOrden = new ArrayList<>();
        descuentosAplicados = new ArrayList<>();
        empleadoSeleccionado = null;
        cajaSeleccionada = null;
        productoSeleccionado = null;
        descuentoSeleccionado = null;
        fechaOrdenTexto = fechaActual();

        recargarOrdenes();

        mensaje(FacesMessage.SEVERITY_INFO, "Orden guardada correctamente.");
    }

    // "2026-10-01" (calendario) -> OffsetDateTime en hora de El Salvador
    private OffsetDateTime textoAFecha(String texto) {

        if (texto == null || texto.isBlank()) {
            return OffsetDateTime.now();
        }

        LocalDate dia = LocalDate.parse(texto.trim());

        // Si es hoy se guarda con la hora actual (para ordenar las del día),
        // si es otro día se guarda al inicio de ese día
        if (dia.equals(LocalDate.now(ZONA))) {
            return OffsetDateTime.now();
        }

        return dia.atStartOfDay(ZONA).toOffsetDateTime();
    }

    private void mensaje(FacesMessage.Severity tipo, String texto) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(tipo, texto, null));
    }
}