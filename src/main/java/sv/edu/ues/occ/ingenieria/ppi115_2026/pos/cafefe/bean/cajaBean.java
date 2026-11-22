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
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caja;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CajaService;

@Named("cajaBean")
@ViewScoped
public class cajaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CajaService cajaService;

    private List<Caja> cajas;
    private Caja caja;

    // true cuando el formulario esta editando una caja de la lista
    private boolean editando;

    @PostConstruct
    public void inicializar() {
        cajas = cajaService.listar();
        nueva();
    }

    public void nueva() {
        caja = new Caja();
        caja.setIdCaja(UUID.randomUUID());
        caja.setActivo(true);
        editando = false;
    }

    public List<Caja> getCajas() {
        return cajas;
    }

    public Caja getCaja() {
        return caja;
    }

    public boolean isEditando() {
        return editando;
    }

    public void guardar() {

        // Con el mismo formulario se crea o se edita
        try {
            if (editando) {
                cajaService.actualizar(caja);
            } else {
                cajaService.guardar(caja);
            }
        } catch (Exception e) {
            mensajeError("No se pudo guardar la caja. Revise los datos e intente de nuevo.");
            return;
        }

        cajas = cajaService.listar();

        nueva();
    }

    public void editar(Caja cajaSeleccionada) {
        // El formulario de arriba pasa a mostrar los datos de la caja
        caja = cajaSeleccionada;
        editando = true;
    }

    public void cancelar() {
        // Se vuelve a leer la lista por si se cambio algo sin guardar
        cajas = cajaService.listar();
        nueva();
    }

    public void eliminar(Caja cajaEliminar) {
        try {
            cajaService.eliminar(cajaEliminar.getIdCaja());
        } catch (Exception e) {
            // Pasa si la caja ya tiene facturas
            mensajeError("No se puede eliminar la caja porque tiene facturas.");
        }

        cajas = cajaService.listar();

        nueva();
    }

    private void mensajeError(String texto) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
    }
}