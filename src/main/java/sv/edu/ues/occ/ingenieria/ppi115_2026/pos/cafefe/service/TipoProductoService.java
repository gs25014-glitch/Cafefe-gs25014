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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.TipoProductoRepository;

@ApplicationScoped
public class TipoProductoService {

    private TipoProductoRepository tipoProductoRepository;

    protected TipoProductoService() {
    }

    @Inject
    public TipoProductoService(
            TipoProductoRepository tipoProductoRepository) {
        this.tipoProductoRepository = tipoProductoRepository;
    }

    @Transactional
    public void guardar(TipoProducto tipoProducto) {
        tipoProductoRepository.guardar(tipoProducto);
    }

    public List<TipoProducto> listar() {
        return tipoProductoRepository.listar();
    }

    public TipoProducto buscarPorId(UUID id) {
        return tipoProductoRepository.buscarPorId(id);
    }

    @Transactional
    public TipoProducto actualizar(TipoProducto tipoProducto) {
        return tipoProductoRepository.actualizar(tipoProducto);
    }

    @Transactional
    public void eliminar(UUID id) {
        tipoProductoRepository.eliminar(id);
    }
}