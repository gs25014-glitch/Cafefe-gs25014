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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caja;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.CajaRepository;

@ApplicationScoped
public class CajaService {

    private CajaRepository cajaRepository;

    protected CajaService() {
    }

    @Inject
    public CajaService(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    @Transactional
    public void guardar(Caja caja) {
        cajaRepository.guardar(caja);
    }

    public List<Caja> listar() {
        return cajaRepository.listar();
    }

    public Caja buscarPorId(UUID id) {
        return cajaRepository.buscarPorId(id);
    }

    @Transactional
    public Caja actualizar(Caja caja) {
        return cajaRepository.actualizar(caja);
    }

    @Transactional
    public void eliminar(UUID id) {
        cajaRepository.eliminar(id);
    }
}