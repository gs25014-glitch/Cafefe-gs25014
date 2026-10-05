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
import jakarta.faces.model.SelectItem;
import java.util.ArrayList;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Empleado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.EmpleadoRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Rol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.EmpleadoRolService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.EmpleadoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.RolService;

@Named("empleadoBean")
@ViewScoped
public class empleadoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmpleadoService empleadoService;

    @Inject
    private EmpleadoRolService empleadoRolService;

    @Inject
    private RolService rolService;

    private List<Empleado> empleados;
    
    private List<Rol> rolesActivos;
    private List<SelectItem> opcionesRoles;

    private Empleado empleado;
    private Empleado empleadoSeleccionado;

    private UUID idRolSeleccionado;

    @PostConstruct
    public void inicializar() {
        empleados = empleadoService.listar();
        rolesActivos = rolService.listar();

        opcionesRoles = new ArrayList<>();

        for (Rol rol : rolesActivos) {

            if (Boolean.TRUE.equals(rol.getActivo())) {

                opcionesRoles.add(
                    new SelectItem(
                        rol.getIdRol(),
                        rol.getNombre()
                    )
                );
            }
        }

        nuevoEmpleado();
    }  

    public void nuevoEmpleado() {

        empleado = new Empleado();

        empleado.setIdEmpleado(UUID.randomUUID());

        empleado.setActivo(true);

        idRolSeleccionado = null;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public List<Rol> getRolesActivos() {
        return rolesActivos;
    }
    
    public List<SelectItem> getOpcionesRoles() {
        return opcionesRoles;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Empleado getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public UUID getIdRolSeleccionado() {
        return idRolSeleccionado;
    }

    public void setIdRolSeleccionado(UUID idRolSeleccionado) {
        this.idRolSeleccionado = idRolSeleccionado;
    }

    public void guardar() {

        if (idRolSeleccionado == null) {
            return;
        }

        Rol rol = rolService.buscarPorId(idRolSeleccionado);

        if (rol == null || !Boolean.TRUE.equals(rol.getActivo())) {
            return;
        }

        empleadoService.guardarConRol(
                empleado,
                idRolSeleccionado
        );

        empleados = empleadoService.listar();

        nuevoEmpleado();
    }

    public void editar(Empleado empleado) {

        empleadoSeleccionado = empleado;
    }

    public void actualizar() {

        empleadoService.actualizar(
                empleadoSeleccionado
        );

        empleados = empleadoService.listar();

        empleadoSeleccionado = null;
    }

    public String obtenerNombreRol(UUID idEmpleado) {

        EmpleadoRol empleadoRol =
                empleadoRolService.buscarActivoPorEmpleado(idEmpleado);

        if (empleadoRol == null) {
            return "Sin rol";
        }

        Rol rol = rolService.buscarPorId(
                empleadoRol.getIdRol()
        );

        if (rol == null) {
            return "Sin rol";
        }

        return rol.getNombre();
    }

    public void eliminar(Empleado empleado) {

        empleadoService.eliminar(
                empleado.getIdEmpleado()
        );

        empleados = empleadoService.listar();
    }
}