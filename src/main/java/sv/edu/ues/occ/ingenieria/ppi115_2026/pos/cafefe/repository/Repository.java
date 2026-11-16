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
import jakarta.persistence.PersistenceException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Repository<T> {

    private static final Logger LOG = Logger.getLogger(Repository.class.getName());

    @PersistenceContext(unitName = "CafefePU")
    protected EntityManager entityManager;

    private final Class<T> tipoEntidad;

    protected Repository(Class<T> tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public void guardar(T entidad) {
        try {
            entityManager.persist(entidad);
        } catch (PersistenceException e) {
            throw error("guardar", e);
        }
    }

    public T actualizar(T entidad) {
        try {
            return entityManager.merge(entidad);
        } catch (PersistenceException e) {
            throw error("actualizar", e);
        }
    }

    public List<T> listar() {
        try {
            return entityManager
                    .createQuery(
                            "SELECT e FROM "
                            + tipoEntidad.getSimpleName()
                            + " e",
                            tipoEntidad
                    )
                    .getResultList();
        } catch (PersistenceException e) {
            throw error("consultar", e);
        }
    }

    public T buscarPorId(Object id) {
        try {
            return entityManager.find(tipoEntidad, id);
        } catch (PersistenceException e) {
            throw error("consultar", e);
        }
    }

    public void eliminar(Object id) {
        try {
            T entidad = entityManager.find(tipoEntidad, id);

            if (entidad != null) {
                entityManager.remove(entidad);
            }
        } catch (PersistenceException e) {
            throw error("eliminar", e);
        }
    }

    public void flush() {
        try {
            entityManager.flush();
        } catch (PersistenceException e) {
            throw error("guardar", e);
        }
    }

    // Guarda el error en el log y lanza una excepción que explica el motivo
    private PersistenceException error(String operacion, PersistenceException e) {

        LOG.log(Level.SEVERE, "Error al " + operacion + " "
                + tipoEntidad.getSimpleName(), e);

        return new PersistenceException(
                "No se pudo " + operacion + " (" + tipoEntidad.getSimpleName()
                + "). Motivo: " + motivo(e), e);
    }

    // Revisa la causa original del error y la explica en palabras simples
    private String motivo(Throwable error) {

        Throwable causa = error;
        String texto = "";

        while (causa != null) {

            String mensaje = causa.getMessage() == null ? "" : causa.getMessage().toLowerCase();
            texto = texto + " " + mensaje + " " + causa.getClass().getSimpleName().toLowerCase();

            if (causa.getCause() == null || causa.getCause() == causa) {
                break;
            }
            causa = causa.getCause();
        }

        if (texto.contains("connection refused") || texto.contains("connect timed out")
                || texto.contains("check that the hostname")) {
            return "no se pudo conectar con el servidor de la base de datos "
                    + "(revise que el contenedor cafefe-db esté encendido).";
        }

        if (texto.contains("unknownhost")) {
            return "no se encuentra el servidor de la base de datos (revise el host).";
        }

        if (texto.contains("password authentication failed")) {
            return "el usuario o la contraseña de la base de datos son incorrectos.";
        }

        if (texto.contains("database") && texto.contains("does not exist")) {
            return "la base de datos no existe (revise que se haya creado con cafefe.sql).";
        }

        if (texto.contains("relation") && texto.contains("does not exist")) {
            return "falta una tabla en la base de datos (revise que se haya cargado cafefe.sql).";
        }

        // Si no es un caso conocido, se muestra el mensaje de la causa original
        return causa.getMessage() == null ? causa.getClass().getSimpleName() : causa.getMessage();
    }
}