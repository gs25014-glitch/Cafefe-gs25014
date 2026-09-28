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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.FacturaOrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.FacturaOrdenProductoRepository;

@ApplicationScoped
public class FacturaOrdenProductoService {

    private FacturaOrdenProductoRepository facturaOrdenProductoRepository;

    protected FacturaOrdenProductoService() {
    }

    @Inject
    public FacturaOrdenProductoService(
            FacturaOrdenProductoRepository facturaOrdenProductoRepository) {

        this.facturaOrdenProductoRepository = facturaOrdenProductoRepository;
    }

    @Transactional
    public void guardar(FacturaOrdenProducto facturaOrdenProducto) {
        facturaOrdenProductoRepository.guardar(facturaOrdenProducto);
    }

    public List<FacturaOrdenProducto> listar() {
        return facturaOrdenProductoRepository.listar();
    }

    public FacturaOrdenProducto buscarPorId(UUID id) {
        return facturaOrdenProductoRepository.buscarPorId(id);
    }

    @Transactional
    public FacturaOrdenProducto actualizar(
            FacturaOrdenProducto facturaOrdenProducto) {

        return facturaOrdenProductoRepository.actualizar(facturaOrdenProducto);
    }

    @Transactional
    public void eliminar(UUID id) {
        facturaOrdenProductoRepository.eliminar(id);
    }
}