/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.bean;

/**
 *
 * @author kelvi
 */

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Rol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.RolService;

@Named("rolBean")
@ViewScoped
public class rolBean implements Serializable {

    @Inject
    private RolService rolService;

    private Rol rol;
    private List<Rol> roles;
    private boolean editando;

    @PostConstruct
    public void inicializar() {
        cargarRoles();
        nuevo();
    }

    public void cargarRoles() {
        roles = rolService.listar();
    }

    public void nuevo() {
        rol = new Rol();
        rol.setIdRol(UUID.randomUUID());
        rol.setActivo(true);
        editando = false;
    }

    public void guardar() {

        try {
            if (editando) {
                rolService.actualizar(rol);
            } else {
                rolService.guardar(rol);
            }
        } catch (IllegalArgumentException e) {
            // Por ejemplo: el rol que se edita ya no existe
            mensajeError(e.getMessage());
        } catch (Exception e) {
            mensajeError("No se pudo guardar el rol. Revise los datos e intente de nuevo.");
        }

        cargarRoles();
        nuevo();
    }

    public void editar(Rol rolSeleccionado) {
        rol = rolSeleccionado;
        editando = true;
    }

    public void eliminar(UUID id) {

        try {
            rolService.eliminar(id);
        } catch (Exception e) {
            // Pasa si el rol ya está asignado a un empleado
            mensajeError("No se puede eliminar el rol porque está asignado a un empleado.");
        }

        cargarRoles();
        nuevo();
    }

    private void mensajeError(String texto) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
    }

    public void cancelar() {
        nuevo();
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public boolean isEditando() {
        return editando;
    }
}