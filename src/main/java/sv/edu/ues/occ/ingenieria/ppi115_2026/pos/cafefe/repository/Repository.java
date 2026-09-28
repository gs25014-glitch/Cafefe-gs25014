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
import jakarta.persistence.PersistenceContext;
import java.util.List;

public abstract class Repository<T> {

    @PersistenceContext(unitName = "CafefePU")
    protected EntityManager entityManager;

    private final Class<T> tipoEntidad;

    protected Repository(Class<T> tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public void guardar(T entidad) {
        entityManager.persist(entidad);
    }

    public T actualizar(T entidad) {
        return entityManager.merge(entidad);
    }

    public List<T> listar() {
        return entityManager
                .createQuery(
                        "SELECT e FROM "
                        + tipoEntidad.getSimpleName()
                        + " e",
                        tipoEntidad
                )
                .getResultList();
    }

    public T buscarPorId(Object id) {
        return entityManager.find(tipoEntidad, id);
    }

    public void eliminar(Object id) {
        T entidad = entityManager.find(tipoEntidad, id);

        if (entidad != null) {
            entityManager.remove(entidad);
        }
    }
    
    public void flush() {
        entityManager.flush();
    }
}