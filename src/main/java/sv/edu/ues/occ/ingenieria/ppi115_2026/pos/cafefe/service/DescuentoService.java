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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Descuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.DescuentoRepository;

@ApplicationScoped
public class DescuentoService {

    @Inject
    private DescuentoRepository descuentoRepository;

    @Inject
    private TipoDescuentoService tipoDescuentoService;

    public List<Descuento> listar() {
        return descuentoRepository.listar();
    }

    public Descuento buscarPorId(UUID id) {
        return descuentoRepository.buscarPorId(id);
    }

    public List<Descuento> listarPorTipo(UUID idTipoDescuento) {
        return descuentoRepository.listarPorTipo(idTipoDescuento);
    }

    public boolean tipoActivo(UUID idTipoDescuento) {

        if (idTipoDescuento == null) {
            return false;
        }

        return tipoDescuentoService.buscarPorId(idTipoDescuento) != null
                && Boolean.TRUE.equals(
                        tipoDescuentoService.buscarPorId(idTipoDescuento).getActivo()
                );
    }

    public boolean fechasValidas(
            OffsetDateTime fechaDesde,
            OffsetDateTime fechaHasta) {

        if (fechaDesde == null) {
            return false;
        }

        if (fechaHasta == null) {
            return true;
        }

        return !fechaHasta.isBefore(fechaDesde);
    }

    @Transactional
    public boolean guardar(Descuento descuento) {

        if (!tipoActivo(descuento.getIdTipoDescuento())) {
            return false;
        }

        if (!fechasValidas(
                descuento.getFechaDesde(),
                descuento.getFechaHasta())) {
            return false;
        }

        descuentoRepository.guardar(descuento);

        return true;
    }

    @Transactional
    public Descuento actualizar(Descuento descuento) {

        if (!tipoActivo(descuento.getIdTipoDescuento())) {
            return null;
        }

        if (!fechasValidas(
                descuento.getFechaDesde(),
                descuento.getFechaHasta())) {
            return null;
        }

        return descuentoRepository.actualizar(descuento);
    }

    @Transactional
    public void eliminar(UUID id) {
        descuentoRepository.eliminar(id);
    }
}