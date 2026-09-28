/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Empleado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.EmpleadoService;

@Named("empleadoBean")
@ViewScoped
public class empleadoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmpleadoService empleadoService;

    private List<Empleado> empleados;
    private Empleado empleado;
    private Empleado empleadoSeleccionado;

    @PostConstruct
    public void inicializar() {
        empleados = empleadoService.listar();
        empleado = new Empleado();
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Empleado getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void guardar() {
        empleado.setIdEmpleado(UUID.randomUUID());

        empleadoService.guardar(empleado);

        empleados = empleadoService.listar();

        empleado = new Empleado();
    }

    public void editar(Empleado empleado) {
        empleadoSeleccionado = empleado;
    }

    public void actualizar() {
        empleadoService.actualizar(empleadoSeleccionado);

        empleados = empleadoService.listar();

        empleadoSeleccionado = null;
    }

    public void eliminar(Empleado empleado) {
        empleadoService.eliminar(empleado.getIdEmpleado());

        empleados = empleadoService.listar();
    }
}