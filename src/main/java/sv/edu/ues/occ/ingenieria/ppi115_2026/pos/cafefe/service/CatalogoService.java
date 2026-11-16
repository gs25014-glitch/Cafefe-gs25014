package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.CatalogoRepository;

@ApplicationScoped
public class CatalogoService {

    @Inject
    CatalogoRepository repository;

    public List<Producto> productosPorTipo(UUID idTipoProducto, int first, int max) {
        return repository.productosPorTipo(idTipoProducto, first, max);
    }

    public long contarProductosPorTipo(UUID idTipoProducto) {
        return repository.contarProductosPorTipo(idTipoProducto);
    }

    public Producto buscarProducto(UUID id) {
        return repository.buscarPorId(id);
    }
}