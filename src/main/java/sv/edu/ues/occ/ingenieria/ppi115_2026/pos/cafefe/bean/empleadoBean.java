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
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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

    private UUID idRolSeleccionado;

    // true cuando el formulario esta editando un empleado de la lista (el rol solo se muestra)
    private boolean editando;

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

        editando = false;
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

    public UUID getIdRolSeleccionado() {
        return idRolSeleccionado;
    }

    public void setIdRolSeleccionado(UUID idRolSeleccionado) {
        this.idRolSeleccionado = idRolSeleccionado;
    }

    public boolean isEditando() {
        return editando;
    }

    public void guardar() {

        // Con el mismo formulario se crea o se edita
        if (editando) {
            actualizar();
            return;
        }

        if (idRolSeleccionado == null) {
            mensajeError("Debe seleccionar un rol.");
            return;
        }

        Rol rol = rolService.buscarPorId(idRolSeleccionado);

        if (rol == null || !Boolean.TRUE.equals(rol.getActivo())) {
            mensajeError("No se puede asignar un rol inactivo.");
            return;
        }

        try {
            empleadoService.guardarConRol(
                    empleado,
                    idRolSeleccionado
            );
        } catch (Exception e) {
            mensajeError("No se pudo guardar el empleado. Revise los datos e intente de nuevo.");
            return;
        }

        empleados = empleadoService.listar();

        nuevoEmpleado();
    }

    public void editar(Empleado empleadoSeleccionado) {

        // El formulario de arriba pasa a mostrar los datos del empleado
        empleado = empleadoSeleccionado;

        // Se busca su rol actual para mostrarlo deshabilitado
        EmpleadoRol empleadoRol =
                empleadoRolService.buscarActivoPorEmpleado(empleado.getIdEmpleado());

        idRolSeleccionado = empleadoRol == null ? null : empleadoRol.getIdRol();

        editando = true;
    }

    public void cancelar() {

        // Se vuelve a leer la lista por si se cambio algo sin guardar
        empleados = empleadoService.listar();

        nuevoEmpleado();
    }

    private void mensajeError(String texto) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
    }

    private void actualizar() {

        try {
            empleadoService.actualizar(empleado);
        } catch (IllegalArgumentException e) {
            mensajeError(e.getMessage());
            return;
        } catch (Exception e) {
            mensajeError("No se pudo actualizar el empleado. Revise los datos e intente de nuevo.");
            return;
        }

        empleados = empleadoService.listar();

        nuevoEmpleado();
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

        try {
            empleadoService.eliminar(
                    empleado.getIdEmpleado()
            );
        } catch (Exception e) {
            // Pasa si el empleado tiene rol asignado u órdenes
            mensajeError("No se puede eliminar a " + empleado.getNombre()
                    + " porque tiene registros relacionados (rol u órdenes).");
        }

        empleados = empleadoService.listar();

        nuevoEmpleado();
    }
}