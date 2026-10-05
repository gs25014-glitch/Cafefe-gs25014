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
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoTipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.ProductoTipoProductoRepository;

@ApplicationScoped
public class ProductoTipoProductoService {

    @Inject
    private ProductoTipoProductoRepository repository;

    @Transactional
    public void guardar(ProductoTipoProducto relacion) {
        repository.guardar(relacion);
    }

    public ProductoTipoProducto buscarPorProducto(UUID idProducto) {
        return repository.buscarPorProducto(idProducto);
    }

    @Transactional
    public void actualizar(ProductoTipoProducto relacion) {
        repository.actualizar(relacion);
    }

    @Transactional
    public void eliminar(UUID id) {
        repository.eliminar(id);
    }
}