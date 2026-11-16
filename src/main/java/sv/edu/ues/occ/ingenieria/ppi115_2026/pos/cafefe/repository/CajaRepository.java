/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caja;

@ApplicationScoped
public class CajaRepository extends Repository<Caja> {

    public CajaRepository() {
        super(Caja.class);
    }

    public List<Caja> findRange(int first, int max) {
        return entityManager
                .createQuery("SELECT c FROM Caja c ORDER BY c.nombre", Caja.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }

    public long contar() {
        return entityManager
                .createQuery("SELECT COUNT(c) FROM Caja c", Long.class)
                .getSingleResult();
    }
}   