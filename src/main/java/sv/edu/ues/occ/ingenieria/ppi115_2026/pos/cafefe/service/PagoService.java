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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Pago;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.PagoRepository;

@ApplicationScoped
public class PagoService {

    private PagoRepository pagoRepository;

    protected PagoService() {
    }

    @Inject
    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Transactional
    public void guardar(Pago pago) {
        pagoRepository.guardar(pago);
    }

    public List<Pago> listar() {
        return pagoRepository.listar();
    }

    public Pago buscarPorId(UUID id) {
        return pagoRepository.buscarPorId(id);
    }

    @Transactional
    public Pago actualizar(Pago pago) {
        return pagoRepository.actualizar(pago);
    }

    @Transactional
    public void eliminar(UUID id) {
        pagoRepository.eliminar(id);
    }
}