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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.CaracteristicaRepository;

@ApplicationScoped
public class CaracteristicaService {

    @Inject
    private CaracteristicaRepository caracteristicaRepository;

    @Inject
    private TipoCaracteristicaService tipoCaracteristicaService;

    public List<Caracteristica> listar() {
        return caracteristicaRepository.listar();
    }

    public Caracteristica buscarPorId(UUID id) {
        return caracteristicaRepository.buscarPorId(id);
    }

    public List<Caracteristica> listarPorTipo(UUID idTipoCaracteristica) {
        return caracteristicaRepository.listarPorTipo(idTipoCaracteristica);
    }

    public boolean tipoActivo(UUID idTipoCaracteristica) {

        if (idTipoCaracteristica == null) {
            return false;
        }

        TipoCaracteristica tipo =
                tipoCaracteristicaService.buscarPorId(idTipoCaracteristica);

        return tipo != null && Boolean.TRUE.equals(tipo.getActivo());
    }

    @Transactional
    public boolean guardar(Caracteristica caracteristica) {

        if (!tipoActivo(caracteristica.getIdTipoCaracteristica())) {
            return false;
        }

        caracteristicaRepository.guardar(caracteristica);

        return true;
    }

    @Transactional
    public Caracteristica actualizar(Caracteristica caracteristica) {
        return caracteristicaRepository.actualizar(caracteristica);
    }

    @Transactional
    public void eliminar(UUID id) {
        caracteristicaRepository.eliminar(id);
    }
}