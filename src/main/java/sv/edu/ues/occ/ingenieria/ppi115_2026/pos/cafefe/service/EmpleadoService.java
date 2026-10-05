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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Empleado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.EmpleadoRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.repository.EmpleadoRepository;

@ApplicationScoped
public class EmpleadoService {

    private EmpleadoRepository empleadoRepository;

    @Inject
    private EmpleadoRolService empleadoRolService;

    protected EmpleadoService() {
    }

    @Inject
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional
    public void guardar(Empleado empleado) {
        empleadoRepository.guardar(empleado);
    }

    @Transactional
    public void guardarConRol(Empleado empleado, UUID idRol) {

        empleadoRepository.guardar(empleado);

        EmpleadoRol empleadoRol = new EmpleadoRol();

        empleadoRol.setIdEmpleadoRol(UUID.randomUUID());
        empleadoRol.setIdEmpleado(empleado.getIdEmpleado());
        empleadoRol.setIdRol(idRol);
        empleadoRol.setActivo(true);

        empleadoRolService.guardar(empleadoRol);
    }

    public List<Empleado> listar() {
        return empleadoRepository.listar();
    }

    public Empleado buscarPorId(UUID id) {
        return empleadoRepository.buscarPorId(id);
    }

    @Transactional
    public Empleado actualizar(Empleado empleado) {
        return empleadoRepository.actualizar(empleado);
    }

    @Transactional
    public void eliminar(UUID id) {
        empleadoRepository.eliminar(id);
    }
}