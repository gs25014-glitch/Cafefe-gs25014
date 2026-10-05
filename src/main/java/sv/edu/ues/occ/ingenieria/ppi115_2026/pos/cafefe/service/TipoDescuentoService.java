/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

/**
 *
 * @author kelvi
 */

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoDescuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.TipoDescuentoRepository;

@ApplicationScoped
public class TipoDescuentoService {

    @Inject
    private TipoDescuentoRepository tipoDescuentoRepository;

    public List<TipoDescuento> listar() {
        return tipoDescuentoRepository.listar();
    }

    public List<TipoDescuento> listarActivos() {
        return tipoDescuentoRepository.listarActivos();
    }

    public TipoDescuento buscarPorId(UUID id) {
        return tipoDescuentoRepository.buscarPorId(id);
    }

    @Transactional
    public void guardar(TipoDescuento tipoDescuento) {
        tipoDescuentoRepository.guardar(tipoDescuento);
    }

    @Transactional
    public TipoDescuento actualizar(TipoDescuento tipoDescuento) {
        return tipoDescuentoRepository.actualizar(tipoDescuento);
    }

    @Transactional
    public void eliminar(UUID id) {
        tipoDescuentoRepository.eliminar(id);
    }
}