/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;

public class RepositoryTest {

    // Repository necesita un EntityManager.
    // Aquí lo colocamos manualmente para la prueba.
    static class ProductoRepositoryTestable
            extends ProductoRepository {

        public void setEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
        }
    }

    @Test
    void guardarProducto() {

        EntityManager entityManager =
                mock(EntityManager.class);

        ProductoRepositoryTestable repository =
                new ProductoRepositoryTestable();

        repository.setEntityManager(entityManager);

        Producto producto = new Producto();

        producto.setIdProducto(UUID.randomUUID());
        producto.setNombre("Café americano");
        producto.setActivo(true);
        producto.setPrecioSugerido(
                new BigDecimal("2.00")
        );

        repository.guardar(producto);

        verify(entityManager).persist(producto);
    }

    @Test
    void buscarProductoPorId() {

        EntityManager entityManager =
                mock(EntityManager.class);

        ProductoRepositoryTestable repository =
                new ProductoRepositoryTestable();

        repository.setEntityManager(entityManager);

        UUID id = UUID.randomUUID();

        Producto producto = new Producto();

        producto.setIdProducto(id);
        producto.setNombre("Café con leche");
        producto.setActivo(true);
        producto.setPrecioSugerido(
                new BigDecimal("2.50")
        );

        when(entityManager.find(Producto.class, id))
                .thenReturn(producto);

        Producto resultado =
                repository.buscarPorId(id);

        assertEquals(
                id,
                resultado.getIdProducto()
        );

        assertEquals(
                "Café con leche",
                resultado.getNombre()
        );

        verify(entityManager)
                .find(Producto.class, id);
    }

    @Test
    void actualizarProducto() {

        EntityManager entityManager =
                mock(EntityManager.class);

        ProductoRepositoryTestable repository =
                new ProductoRepositoryTestable();

        repository.setEntityManager(entityManager);

        Producto producto = new Producto();

        producto.setIdProducto(UUID.randomUUID());
        producto.setNombre("Café actualizado");
        producto.setActivo(true);
        producto.setPrecioSugerido(
                new BigDecimal("3.00")
        );

        when(entityManager.merge(producto))
                .thenReturn(producto);

        Producto resultado =
                repository.actualizar(producto);

        assertEquals(
                "Café actualizado",
                resultado.getNombre()
        );

        verify(entityManager)
                .merge(producto);
    }

    @Test
    void eliminarProducto() {

        EntityManager entityManager =
                mock(EntityManager.class);

        ProductoRepositoryTestable repository =
                new ProductoRepositoryTestable();

        repository.setEntityManager(entityManager);

        UUID id = UUID.randomUUID();

        Producto producto = new Producto();

        producto.setIdProducto(id);
        producto.setNombre("Producto a eliminar");

        when(entityManager.find(Producto.class, id))
                .thenReturn(producto);

        repository.eliminar(id);

        verify(entityManager)
                .find(Producto.class, id);

        verify(entityManager)
                .remove(producto);
    }

    @Test
    void listarProductos() {

        EntityManager entityManager =
                mock(EntityManager.class);

        TypedQuery<Producto> query =
                mock(TypedQuery.class);

        ProductoRepositoryTestable repository =
                new ProductoRepositoryTestable();

        repository.setEntityManager(entityManager);

        Producto producto1 = new Producto();
        producto1.setIdProducto(UUID.randomUUID());
        producto1.setNombre("Café americano");

        Producto producto2 = new Producto();
        producto2.setIdProducto(UUID.randomUUID());
        producto2.setNombre("Café con leche");

        List<Producto> productos =
                List.of(producto1, producto2);

        when(entityManager.createQuery(
                "SELECT e FROM Producto e",
                Producto.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(productos);

        List<Producto> resultado =
                repository.listar();

        assertEquals(2, resultado.size());

        assertEquals(
                "Café americano",
                resultado.get(0).getNombre()
        );

        assertEquals(
                "Café con leche",
                resultado.get(1).getNombre()
        );

        verify(entityManager).createQuery(
                "SELECT e FROM Producto e",
                Producto.class
        );

        verify(query).getResultList();
    }
    
    @Test
    void crearTipoProductoRepository() {

        TipoProductoRepository repository =
                new TipoProductoRepository();

        assertEquals(
                TipoProductoRepository.class,
                repository.getClass()
        );
    }
    
    @Test
    void eliminarProductoCuandoNoExiste() {

        EntityManager entityManager =
                mock(EntityManager.class);

        ProductoRepositoryTestable repository =
                new ProductoRepositoryTestable();

        repository.setEntityManager(entityManager);

        UUID id = UUID.randomUUID();

        when(entityManager.find(Producto.class, id))
                .thenReturn(null);

        repository.eliminar(id);

        verify(entityManager)
                .find(Producto.class, id);

        verify(entityManager, never())
                .remove(any(Producto.class));
    }
}