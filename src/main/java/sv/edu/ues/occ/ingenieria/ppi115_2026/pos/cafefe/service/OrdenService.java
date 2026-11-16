/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */ 

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Factura;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Orden;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.OrdenRepository;

@ApplicationScoped
public class OrdenService {

    private OrdenRepository ordenRepository;

    @Inject
    private OrdenProductoService ordenProductoService;

    @Inject
    private FacturaService facturaService;

    @Inject
    private FacturaOrdenProductoService facturaOrdenProductoService;

    protected OrdenService() {
    }

    @Inject
    public OrdenService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Transactional
    public void guardar(Orden orden) {
        ordenRepository.guardar(orden);
    }

    // Guarda la orden, sus productos y la factura en una sola transacción:
    // si algo falla no se guarda nada
    @Transactional
    public void guardarCompleta(Orden orden, List<OrdenProducto> detalles,
            Factura factura, List<FacturaOrdenProducto> detallesFactura) {

        ordenRepository.guardar(orden);

        for (OrdenProducto detalle : detalles) {
            ordenProductoService.guardar(detalle);
        }

        ordenProductoService.flush();

        facturaService.guardar(factura);

        for (FacturaOrdenProducto detalleFactura : detallesFactura) {
            facturaOrdenProductoService.guardar(detalleFactura);
        }
    }

    public List<Orden> listar() {
        return ordenRepository.listar();
    }

    public Orden buscarPorId(UUID id) {
        return ordenRepository.buscarPorId(id);
    }

    @Transactional
    public Orden actualizar(Orden orden) {
        return ordenRepository.actualizar(orden);
    }

    @Transactional
    public void eliminar(UUID id) {
        ordenRepository.eliminar(id);
    }
}