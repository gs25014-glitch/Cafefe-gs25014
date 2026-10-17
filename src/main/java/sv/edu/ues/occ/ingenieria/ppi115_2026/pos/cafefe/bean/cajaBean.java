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
    private Caja cajaSeleccionada;

    @PostConstruct
    public void inicializar() {
        cajas = cajaService.listar();
        caja = new Caja();
    }

    public List<Caja> getCajas() {
        return cajas;
    }

    public Caja getCaja() {
        return caja;
    }

    public Caja getCajaSeleccionada() {
        return cajaSeleccionada;
    }

    public void guardar() {
        caja.setIdCaja(UUID.randomUUID());

        cajaService.guardar(caja);

        cajas = cajaService.listar();

        caja = new Caja();
    }

    public void editar(Caja caja) {
        cajaSeleccionada = caja;
    }

    public void actualizar() {
        cajaService.actualizar(cajaSeleccionada);

        cajas = cajaService.listar();

        cajaSeleccionada = null;
    }

    public void eliminar(Caja caja) {

        // Si tiene facturas no se elimina y se avisa por qué
        String motivo = cajaService.motivoNoEliminar(caja.getIdCaja());

        if (motivo != null) {
            mensajeLista(FacesMessage.SEVERITY_ERROR, "No se puede eliminar \""
                    + caja.getNombre() + "\" porque " + motivo + ".");
            return;
        }

        try {
            cajaService.eliminar(caja.getIdCaja());
            mensajeLista(FacesMessage.SEVERITY_INFO, "Caja eliminada correctamente.");
        } catch (Exception e) {
            mensajeLista(FacesMessage.SEVERITY_ERROR, "No se pudo eliminar la caja.");
        }

        cajas = cajaService.listar();
    }

    private void mensajeLista(FacesMessage.Severity tipo, String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                "formLista:tablaCajas",
                new FacesMessage(tipo, mensaje, null));
    }
}