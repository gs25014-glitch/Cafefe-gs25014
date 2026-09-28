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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.FacturaRepository;

@ApplicationScoped
public class FacturaService {

    private FacturaRepository facturaRepository;

    protected FacturaService() {
    }

    @Inject
    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Transactional
    public void guardar(Factura factura) {
        facturaRepository.guardar(factura);
    }

    public List<Factura> listar() {
        return facturaRepository.listar();
    }

    public Factura buscarPorId(UUID id) {
        return facturaRepository.buscarPorId(id);
    }
    
    public List<Factura> listarPendientes() {
        return facturaRepository.listarPendientes();
    }

    @Transactional
    public Factura actualizar(Factura factura) {
        return facturaRepository.actualizar(factura);
    }

    @Transactional
    public void eliminar(UUID id) {
        facturaRepository.eliminar(id);
    }
}