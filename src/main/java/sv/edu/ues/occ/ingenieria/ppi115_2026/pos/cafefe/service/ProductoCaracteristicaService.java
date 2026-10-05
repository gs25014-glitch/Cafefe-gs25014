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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.ProductoCaracteristicaRepository;

@ApplicationScoped
public class ProductoCaracteristicaService {

    @Inject
    private ProductoCaracteristicaRepository repository;

    @Transactional
    public void guardar(ProductoCaracteristica relacion) {
        repository.guardar(relacion);
    }

    public List<ProductoCaracteristica> listarPorProducto(UUID idProducto) {
        return repository.listarPorProducto(idProducto);
    }

    @Transactional
    public void eliminar(UUID id) {
        repository.eliminar(id);
    }
}