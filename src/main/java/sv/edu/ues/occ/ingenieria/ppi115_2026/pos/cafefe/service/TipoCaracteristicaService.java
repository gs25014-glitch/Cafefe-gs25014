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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.TipoCaracteristicaRepository;

@ApplicationScoped
public class TipoCaracteristicaService {

    @Inject
    private TipoCaracteristicaRepository tipoCaracteristicaRepository;

    public List<TipoCaracteristica> listar() {
        return tipoCaracteristicaRepository.listar();
    }

    public List<TipoCaracteristica> listarActivos() {
        return tipoCaracteristicaRepository.listarActivos();
    }

    public TipoCaracteristica buscarPorId(UUID id) {
        return tipoCaracteristicaRepository.buscarPorId(id);
    }

    @Transactional
    public void guardar(TipoCaracteristica tipoCaracteristica) {
        tipoCaracteristicaRepository.guardar(tipoCaracteristica);
    }

    @Transactional
    public TipoCaracteristica actualizar(TipoCaracteristica tipoCaracteristica) {
        return tipoCaracteristicaRepository.actualizar(tipoCaracteristica);
    }

    @Transactional
    public void eliminar(UUID id) {
        tipoCaracteristicaRepository.eliminar(id);
    }
}