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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoDescuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoDescuentoService;

@Named("tipoDescuentoBean")
@ViewScoped
public class tipoDescuentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TipoDescuentoService tipoDescuentoService;

    private List<TipoDescuento> tiposDescuento;

    private TipoDescuento tipoDescuento;

    private boolean editando;

    @PostConstruct
    public void inicializar() {
        cargarTipos();
        nuevo();
    }

    public void cargarTipos() {
        tiposDescuento = tipoDescuentoService.listar();
    }

    public void nuevo() {

        tipoDescuento = new TipoDescuento();

        tipoDescuento.setIdTipoDescuento(UUID.randomUUID());

        tipoDescuento.setActivo(true);

        editando = false;
    }

    public void guardar() {

        try {

            if (editando) {

                tipoDescuentoService.actualizar(tipoDescuento);

            } else {

                tipoDescuentoService.guardar(tipoDescuento);
            }

        } catch (IllegalArgumentException e) {
            mensajeError(e.getMessage());
        } catch (Exception e) {
            mensajeError("No se pudo guardar el tipo de descuento. Revise los datos e intente de nuevo.");
        }

        cargarTipos();
        nuevo();
    }

    public void editar(TipoDescuento tipoSeleccionado) {

        tipoDescuento = tipoSeleccionado;

        editando = true;
    }

    public void eliminar(UUID id) {

        try {
            tipoDescuentoService.eliminar(id);
        } catch (Exception e) {
            // Pasa si el tipo ya lo usa algún descuento
            mensajeError("No se puede eliminar el tipo porque lo usa algún descuento.");
        }

        cargarTipos();
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

    public List<TipoDescuento> getTiposDescuento() {
        return tiposDescuento;
    }

    public TipoDescuento getTipoDescuento() {
        return tipoDescuento;
    }

    public boolean isEditando() {
        return editando;
    }
}