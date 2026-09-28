/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.ProductoRepository;

public class ProductoServiceTest {

    @Test
    void guardarProducto() {

        ProductoRepository repository =
                mock(ProductoRepository.class);

        ProductoService service =
                new ProductoService(repository);

        Producto producto = new Producto();

        producto.setIdProducto(UUID.randomUUID());
        producto.setNombre("Café de prueba");
        producto.setActivo(true);
        producto.setComentarios("Producto para prueba");
        producto.setPrecioSugerido(
                new BigDecimal("2.50")
        );

        service.guardar(producto);

        verify(repository).guardar(producto);

        assertEquals(
                "Café de prueba",
                producto.getNombre()
        );
    }
    
    @Test
    void buscarProductoPorId() {

        ProductoRepository repository =
                mock(ProductoRepository.class);

        ProductoService service =
                new ProductoService(repository);

        UUID id = UUID.randomUUID();

        Producto producto = new Producto();

        producto.setIdProducto(id);
        producto.setNombre("Café americano");
        producto.setActivo(true);
        producto.setComentarios("Producto de prueba");
        producto.setPrecioSugerido(
                new BigDecimal("2.00")
        );

        when(repository.buscarPorId(id))
                .thenReturn(producto);

        Producto resultado =
                service.buscarPorId(id);

        assertEquals(id, resultado.getIdProducto());
        assertEquals("Café americano", resultado.getNombre());
        assertEquals(
                new BigDecimal("2.00"),
                resultado.getPrecioSugerido()
        );

        verify(repository).buscarPorId(id);
    }
    
    @Test
    void actualizarProducto() {

        ProductoRepository repository =
                mock(ProductoRepository.class);

        ProductoService service =
                new ProductoService(repository);

        Producto producto = new Producto();

        producto.setIdProducto(UUID.randomUUID());
        producto.setNombre("Café con leche");
        producto.setActivo(true);
        producto.setComentarios("Producto actualizado");
        producto.setPrecioSugerido(
                new BigDecimal("2.50")
        );

        when(repository.actualizar(producto))
                .thenReturn(producto);

        Producto resultado =
                service.actualizar(producto);

        assertEquals(
                "Café con leche",
                resultado.getNombre()
        );

        assertEquals(
                new BigDecimal("2.50"),
                resultado.getPrecioSugerido()
        );

        verify(repository).actualizar(producto);
    }
    
    @Test
    void eliminarProducto() {

        ProductoRepository repository =
                mock(ProductoRepository.class);

        ProductoService service =
                new ProductoService(repository);

        UUID id = UUID.randomUUID();

        service.eliminar(id);

        verify(repository).eliminar(id);
    }
    
    @Test
    void listarProductos() {

        ProductoRepository repository =
                mock(ProductoRepository.class);

        ProductoService service =
                new ProductoService(repository);

        Producto producto1 = new Producto();
        producto1.setIdProducto(UUID.randomUUID());
        producto1.setNombre("Café americano");
        producto1.setActivo(true);
        producto1.setPrecioSugerido(
                new BigDecimal("2.00")
        );

        Producto producto2 = new Producto();
        producto2.setIdProducto(UUID.randomUUID());
        producto2.setNombre("Café con leche");
        producto2.setActivo(true);
        producto2.setPrecioSugerido(
                new BigDecimal("2.50")
        );

        when(repository.listar())
                .thenReturn(java.util.List.of(producto1, producto2));

        var resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals(
                "Café americano",
                resultado.get(0).getNombre()
        );
        assertEquals(
                "Café con leche",
                resultado.get(1).getNombre()
        );

        verify(repository).listar();
    }
}