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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.PagoDetalle;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.PagoDetalleRepository;

@ApplicationScoped
public class PagoDetalleService {

    private PagoDetalleRepository pagoDetalleRepository;

    protected PagoDetalleService() {
    }

    @Inject
    public PagoDetalleService(PagoDetalleRepository pagoDetalleRepository) {
        this.pagoDetalleRepository = pagoDetalleRepository;
    }

    @Transactional
    public void guardar(PagoDetalle pagoDetalle) {
        pagoDetalleRepository.guardar(pagoDetalle);
    }

    public List<PagoDetalle> listar() {
        return pagoDetalleRepository.listar();
    }

    public PagoDetalle buscarPorId(UUID id) {
        return pagoDetalleRepository.buscarPorId(id);
    }

    @Transactional
    public PagoDetalle actualizar(PagoDetalle pagoDetalle) {
        return pagoDetalleRepository.actualizar(pagoDetalle);
    }

    @Transactional
    public void eliminar(UUID id) {
        pagoDetalleRepository.eliminar(id);
    }
}