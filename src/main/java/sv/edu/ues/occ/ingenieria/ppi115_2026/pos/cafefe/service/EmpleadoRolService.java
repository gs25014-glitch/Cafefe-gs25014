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
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.EmpleadoRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.EmpleadoRolRepository;

@ApplicationScoped
public class EmpleadoRolService {

    private EmpleadoRolRepository empleadoRolRepository;

    protected EmpleadoRolService() {
    }

    @Inject
    public EmpleadoRolService(EmpleadoRolRepository empleadoRolRepository) {
        this.empleadoRolRepository = empleadoRolRepository;
    }

    public EmpleadoRol buscarActivoPorEmpleado(UUID idEmpleado) {
        return empleadoRolRepository.buscarActivoPorEmpleado(idEmpleado);
    }
}