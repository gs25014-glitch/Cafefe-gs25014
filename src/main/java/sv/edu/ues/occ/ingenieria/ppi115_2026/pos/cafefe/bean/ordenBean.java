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
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Empleado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.EmpleadoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.OrdenProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.OrdenService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.EmpleadoRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.EmpleadoRolService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caja;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CajaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Factura;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.FacturaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.FacturaOrdenProductoService;
import jakarta.transaction.Transactional;

@Named("ordenBean")
@ViewScoped
public class ordenBean implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private CajaService cajaService;
    
    @Inject
    private FacturaService facturaService;

    @Inject
    private FacturaOrdenProductoService facturaOrdenProductoService;

    private List<Empleado> empleados;
    private List<Producto> productos;
    private List<Caja> cajas;
    private List<OrdenProducto> productosOrden;

    private Orden orden;
    private OrdenProducto ordenProducto;

    private UUID empleadoSeleccionado;
    private UUID productoSeleccionado;
    private UUID cajaSeleccionada;

    @PostConstruct
    public void inicializar() {
        empleados = empleadoService.listar();
        productos = productoService.listar();
        cajas = cajaService.listar();

        orden = new Orden();
        ordenProducto = new OrdenProducto();

        productosOrden = new ArrayList<>();
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

    public void agregarProducto() {

        if (productoSeleccionado == null) {
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

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (OrdenProducto detalle : productosOrden) {

            if (detalle.getPrecio() != null) {
                total = total.add(detalle.getPrecio());
            }
        }

        return total;
    }

    @Transactional
    public void guardarOrden() {

        if (empleadoSeleccionado == null) {
            return;
        }

        if (cajaSeleccionada == null) {
            return;
        }

        if (productosOrden.isEmpty()) {
            return;
        }

        EmpleadoRol empleadoRol =
                empleadoRolService.buscarActivoPorEmpleado(empleadoSeleccionado);

        if (empleadoRol == null) {
            return;
        }

        // 1. Crear la orden
        orden.setIdOrden(UUID.randomUUID());
        orden.setIdEmpleadoRol(empleadoRol.getIdEmpleadoRol());
        orden.setFechaCreacion(OffsetDateTime.now());

        ordenService.guardar(orden);

        // 2. Guardar los productos de la orden
        for (OrdenProducto detalle : productosOrden) {

            detalle.setIdOrden(orden.getIdOrden());

            ordenProductoService.guardar(detalle);
        }
        
        ordenProductoService.flush();

        // 3. Crear la factura
        Factura factura = new Factura();

        factura.setIdFactura(UUID.randomUUID());
        factura.setIdCaja(cajaSeleccionada);
        factura.setIdEmpleadoRol(empleadoRol.getIdEmpleadoRol());
        factura.setCliente("Consumidor final");
        factura.setFechaFacturacion(OffsetDateTime.now());
        factura.setEstado("PENDIENTE");
        factura.setObservaciones(null);

        facturaService.guardar(factura);

        // 4. Relacionar la factura con cada producto de la orden
        for (OrdenProducto detalle : productosOrden) {

            FacturaOrdenProducto facturaDetalle =
                    new FacturaOrdenProducto();

            facturaDetalle.setIdFacturaOrdenProducto(UUID.randomUUID());
            facturaDetalle.setIdFactura(factura.getIdFactura());
            facturaDetalle.setIdOrdenProducto(
                    detalle.getIdOrdenProducto()
            );
            facturaDetalle.setPrecio(detalle.getPrecio());
            facturaDetalle.setObservaciones(null);

            facturaOrdenProductoService.guardar(facturaDetalle);
        }

        // 5. Limpiar la pantalla
        orden = new Orden();
        productosOrden = new ArrayList<>();
        empleadoSeleccionado = null;    
        cajaSeleccionada = null;
        productoSeleccionado = null;
    }
}