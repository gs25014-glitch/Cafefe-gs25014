/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.TipoProductoRepository;

public class TipoProductoServiceTest {

    @Test
    void guardarTipoProducto() {

        TipoProductoRepository repository =
                mock(TipoProductoRepository.class);

        TipoProductoService service =
                new TipoProductoService(repository);

        TipoProducto tipoProducto = new TipoProducto();

        tipoProducto.setIdTipoProducto(UUID.randomUUID());
        tipoProducto.setNombre("Bebidas calientes");
        tipoProducto.setActivo(true);
        tipoProducto.setObservaciones(
                "Tipos de productos calientes"
        );

        service.guardar(tipoProducto);

        verify(repository).guardar(tipoProducto);

        assertEquals(
                "Bebidas calientes",
                tipoProducto.getNombre()
        );
    }

    @Test
    void buscarTipoProductoPorId() {

        TipoProductoRepository repository =
                mock(TipoProductoRepository.class);

        TipoProductoService service =
                new TipoProductoService(repository);

        UUID id = UUID.randomUUID();

        TipoProducto tipoProducto = new TipoProducto();

        tipoProducto.setIdTipoProducto(id);
        tipoProducto.setNombre("Postres");
        tipoProducto.setActivo(true);
        tipoProducto.setObservaciones(
                "Productos dulces"
        );

        when(repository.buscarPorId(id))
                .thenReturn(tipoProducto);

        TipoProducto resultado =
                service.buscarPorId(id);

        assertEquals(
                id,
                resultado.getIdTipoProducto()
        );

        assertEquals(
                "Postres",
                resultado.getNombre()
        );

        assertEquals(
                true,
                resultado.getActivo()
        );

        verify(repository).buscarPorId(id);
    }

    @Test
    void actualizarTipoProducto() {

        TipoProductoRepository repository =
                mock(TipoProductoRepository.class);

        TipoProductoService service =
                new TipoProductoService(repository);

        TipoProducto tipoProducto = new TipoProducto();

        tipoProducto.setIdTipoProducto(UUID.randomUUID());
        tipoProducto.setNombre("Comidas");
        tipoProducto.setActivo(true);
        tipoProducto.setObservaciones(
                "Productos de comida"
        );

        when(repository.actualizar(tipoProducto))
                .thenReturn(tipoProducto);

        TipoProducto resultado =
                service.actualizar(tipoProducto);

        assertEquals(
                "Comidas",
                resultado.getNombre()
        );

        assertEquals(
                "Productos de comida",
                resultado.getObservaciones()
        );

        verify(repository).actualizar(tipoProducto);
    }

    @Test
    void eliminarTipoProducto() {

        TipoProductoRepository repository =
                mock(TipoProductoRepository.class);

        TipoProductoService service =
                new TipoProductoService(repository);

        UUID id = UUID.randomUUID();

        service.eliminar(id);

        verify(repository).eliminar(id);
    }
    
    @Test
    void listarTiposProducto() {

        TipoProductoRepository repository =
                mock(TipoProductoRepository.class);

        TipoProductoService service =
                new TipoProductoService(repository);

        TipoProducto tipo1 = new TipoProducto();
        tipo1.setIdTipoProducto(UUID.randomUUID());
        tipo1.setNombre("Bebidas");
        tipo1.setActivo(true);

        TipoProducto tipo2 = new TipoProducto();
        tipo2.setIdTipoProducto(UUID.randomUUID());
        tipo2.setNombre("Postres");
        tipo2.setActivo(true);

        when(repository.listar())
                .thenReturn(java.util.List.of(tipo1, tipo2));

        var resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals(
                "Bebidas",
                resultado.get(0).getNombre()
        );
        assertEquals(
                "Postres",
                resultado.get(1).getNombre()
        );

        verify(repository).listar();
    }
}