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
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Rol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.RolRepository;

@ApplicationScoped
public class RolService {

    @Inject
    private RolRepository rolRepository;

    public List<Rol> listar() {
        return rolRepository.listar();
    }

    public List<Rol> listarActivos() {
        return rolRepository.listarActivos();
    }

    public Rol buscarPorId(UUID id) {
        return rolRepository.buscarPorId(id);
    }

    @Transactional
    public void guardar(Rol rol) {
        rolRepository.guardar(rol);
    }

    @Transactional
    public Rol actualizar(Rol rol) {
        return rolRepository.actualizar(rol);
    }

    @Transactional
    public void eliminar(UUID id) {
        rolRepository.eliminar(id);
    }
}