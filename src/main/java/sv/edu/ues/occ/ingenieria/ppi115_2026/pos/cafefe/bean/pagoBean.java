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
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Factura;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Pago;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.PagoDetalle;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.FacturaOrdenProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.FacturaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.OrdenProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.PagoDetalleService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.PagoService;

@Named("pagoBean")
@ViewScoped
public class pagoBean implements Serializable {

    @Inject
    private FacturaService facturaService;

    @Inject
    private FacturaOrdenProductoService facturaOrdenProductoService;

    @Inject
    private OrdenProductoService ordenProductoService;

    @Inject
    private PagoService pagoService;

    @Inject
    private PagoDetalleService pagoDetalleService;

    private List<Factura> facturasPendientes;
    private Factura facturaSeleccionada;
    private String tipoPago;
    private BigDecimal monto;

    @PostConstruct
    public void inicializar() {
        cargarFacturas();
    }

    public void cargarFacturas() {
        facturasPendientes = facturaService.listarPendientes();
    }

    public void seleccionarFactura(Factura factura) {
        facturaSeleccionada = factura;
        monto = calcularTotalFactura(factura);
    }

    private BigDecimal calcularTotalFactura(Factura factura) {

        BigDecimal total = BigDecimal.ZERO;

        List<FacturaOrdenProducto> detalles =
                facturaOrdenProductoService.listar();

        for (FacturaOrdenProducto detalle : detalles) {

            if (detalle.getIdFactura().equals(factura.getIdFactura())) {

                OrdenProducto ordenProducto =
                        ordenProductoService.buscarPorId(
                                detalle.getIdOrdenProducto()
                        );

                if (ordenProducto != null && ordenProducto.getPrecio() != null) {
                    total = total.add(ordenProducto.getPrecio());
                }
            }
        }

        return total;
    }

    public void registrarPago() {

        if (facturaSeleccionada == null) {
            return;
        }

        if (tipoPago == null || tipoPago.isBlank()) {
            return;
        }

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        Pago pago = new Pago();

        pago.setIdPago(UUID.randomUUID());
        pago.setIdFactura(facturaSeleccionada.getIdFactura());
        pago.setEstado("PAGADO");
        pago.setObservaciones(null);

        pagoService.guardar(pago);

        PagoDetalle detalle = new PagoDetalle();

        detalle.setIdPagoDetalle(UUID.randomUUID());
        detalle.setIdPago(pago.getIdPago());
        detalle.setMonto(monto);
        detalle.setTipoPago(tipoPago);
        detalle.setObservaciones(null);
        detalle.setReferenciaExterna(null);

        pagoDetalleService.guardar(detalle);

        facturaSeleccionada.setEstado("PAGADA");
        facturaService.actualizar(facturaSeleccionada);

        facturaSeleccionada = null;
        tipoPago = null;
        monto = null;

        cargarFacturas();
    }

    public List<Factura> getFacturasPendientes() {
        return facturasPendientes;
    }

    public Factura getFacturaSeleccionada() {
        return facturaSeleccionada;
    }

    public void setFacturaSeleccionada(Factura facturaSeleccionada) {
        this.facturaSeleccionada = facturaSeleccionada;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}