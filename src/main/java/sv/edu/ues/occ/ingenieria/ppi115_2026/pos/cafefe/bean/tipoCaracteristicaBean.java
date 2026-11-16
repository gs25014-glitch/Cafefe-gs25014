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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoCaracteristicaService;

@Named("tipoCaracteristicaBean")
@ViewScoped
public class tipoCaracteristicaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TipoCaracteristicaService tipoCaracteristicaService;

    private List<TipoCaracteristica> tiposCaracteristica;

    private TipoCaracteristica tipoCaracteristica;

    private TipoCaracteristica tipoCaracteristicaSeleccionado;

    private boolean editando;

    @PostConstruct
    public void inicializar() {
        cargarTipos();
        nuevo();
    }

    public void cargarTipos() {
        tiposCaracteristica = tipoCaracteristicaService.listar();
    }

    public void nuevo() {

        tipoCaracteristica = new TipoCaracteristica();

        tipoCaracteristica.setIdTipoCaracteristica(UUID.randomUUID());

        tipoCaracteristica.setActivo(true);

        editando = false;
    }

    public void guardar() {

        try {

            if (editando) {

                tipoCaracteristicaService.actualizar(
                        tipoCaracteristica
                );

            } else {

                tipoCaracteristicaService.guardar(
                        tipoCaracteristica
                );
            }

        } catch (IllegalArgumentException e) {
            mensajeError(e.getMessage());
        } catch (Exception e) {
            mensajeError("No se pudo guardar el tipo de característica. Revise los datos e intente de nuevo.");
        }

        cargarTipos();
        nuevo();
    }

    public void editar(TipoCaracteristica tipoSeleccionado) {

        tipoCaracteristica = tipoSeleccionado;

        editando = true;
    }

    public void eliminar(UUID id) {

        try {
            tipoCaracteristicaService.eliminar(id);
        } catch (Exception e) {
            // Pasa si el tipo ya lo usa alguna característica
            mensajeError("No se puede eliminar el tipo porque lo usa alguna característica.");
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

    public List<TipoCaracteristica> getTiposCaracteristica() {
        return tiposCaracteristica;
    }

    public TipoCaracteristica getTipoCaracteristica() {
        return tipoCaracteristica;
    }

    public TipoCaracteristica getTipoCaracteristicaSeleccionado() {
        return tipoCaracteristicaSeleccionado;
    }

    public void setTipoCaracteristicaSeleccionado(
            TipoCaracteristica tipoCaracteristicaSeleccionado) {

        this.tipoCaracteristicaSeleccionado =
                tipoCaracteristicaSeleccionado;
    }

    public boolean isEditando() {
        return editando;
    }
}