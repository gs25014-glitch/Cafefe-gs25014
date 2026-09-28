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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.ProductoRepository;

@ApplicationScoped
public class ProductoService {

    private ProductoRepository productoRepository;

    protected ProductoService() {
    }

    @Inject
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public void guardar(Producto producto) {
        productoRepository.guardar(producto);
    }

    public List<Producto> listar() {
        return productoRepository.listar();
    }

    public Producto buscarPorId(UUID id) {
        return productoRepository.buscarPorId(id);
    }

    @Transactional
    public Producto actualizar(Producto producto) {
        return productoRepository.actualizar(producto);
    }

    @Transactional
    public void eliminar(UUID id) {
        productoRepository.eliminar(id);
    }
}