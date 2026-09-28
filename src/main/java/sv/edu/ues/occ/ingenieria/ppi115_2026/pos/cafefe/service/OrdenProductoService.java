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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.OrdenProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.OrdenProductoRepository;

@ApplicationScoped
public class OrdenProductoService {

    private OrdenProductoRepository ordenProductoRepository;

    protected OrdenProductoService() {
    }

    @Inject
    public OrdenProductoService(OrdenProductoRepository ordenProductoRepository) {
        this.ordenProductoRepository = ordenProductoRepository;
    }

    @Transactional
    public void guardar(OrdenProducto ordenProducto) {
        ordenProductoRepository.guardar(ordenProducto);
    }

    public List<OrdenProducto> listar() {
        return ordenProductoRepository.listar();
    }

    public OrdenProducto buscarPorId(UUID id) {
        return ordenProductoRepository.buscarPorId(id);
    }
    
    public void flush() {
        ordenProductoRepository.flush();
    }

    @Transactional
    public OrdenProducto actualizar(OrdenProducto ordenProducto) {
        return ordenProductoRepository.actualizar(ordenProducto);
    }

    @Transactional
    public void eliminar(UUID id) {
        ordenProductoRepository.eliminar(id);
    }
}