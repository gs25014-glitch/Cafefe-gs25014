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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoTipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.ProductoRepository;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.ProductoCaracteristicaRepository;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.ProductoTipoProductoRepository;

@ApplicationScoped
public class ProductoService {

    private ProductoRepository productoRepository;

    @Inject
    private ProductoTipoProductoRepository productoTipoProductoRepository;

    @Inject
    private ProductoCaracteristicaRepository productoCaracteristicaRepository;

    protected ProductoService() {
    }

    @Inject
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public void guardarProductoCompleto(
            Producto producto,
            UUID idTipoProducto,
            List<ProductoCaracteristica> caracteristicas) {

        // Guardar producto
        productoRepository.guardar(producto);

        // Guardar tipo de producto
        ProductoTipoProducto relacionTipo =
                new ProductoTipoProducto();

        relacionTipo.setIdProductoTipoProducto(UUID.randomUUID());
        relacionTipo.setIdProducto(producto.getIdProducto());
        relacionTipo.setIdTipoProducto(idTipoProducto);

        productoTipoProductoRepository.guardar(relacionTipo);

        // Guardar características
        if (caracteristicas != null) {

            for (ProductoCaracteristica caracteristica : caracteristicas) {

                caracteristica.setIdProductoCaracteristica(
                        UUID.randomUUID()
                );

                caracteristica.setIdProducto(
                        producto.getIdProducto()
                );

                productoCaracteristicaRepository.guardar(
                        caracteristica
                );
            }
        }
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