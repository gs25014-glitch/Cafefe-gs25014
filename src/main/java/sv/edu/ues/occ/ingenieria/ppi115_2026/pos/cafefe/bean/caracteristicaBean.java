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
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CaracteristicaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoCaracteristicaService;

@Named("caracteristicaBean")
@ViewScoped
public class caracteristicaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CaracteristicaService caracteristicaService;

    @Inject
    private TipoCaracteristicaService tipoCaracteristicaService;

    private List<Caracteristica> caracteristicas;

    private List<SelectItem> opcionesTipos;

    private Caracteristica caracteristica;

    private Caracteristica caracteristicaSeleccionada;

    private UUID idTipoSeleccionado;

    private boolean editando;

    @PostConstruct
    public void inicializar() {

        cargarCaracteristicas();
        cargarTiposActivos();
        nuevo();
    }

    public void cargarCaracteristicas() {
        caracteristicas = caracteristicaService.listar();
    }

    public void cargarTiposActivos() {

        opcionesTipos = new ArrayList<>();

        List<TipoCaracteristica> tipos =
                tipoCaracteristicaService.listarActivos();

        for (TipoCaracteristica tipo : tipos) {

            if (Boolean.TRUE.equals(tipo.getActivo())) {

                opcionesTipos.add(
                        new SelectItem(
                                tipo.getIdTipoCaracteristica(),
                                tipo.getNombre()
                        )
                );
            }
        }
    }
    
    public String obtenerNombreTipo(UUID idTipoCaracteristica) {

        TipoCaracteristica tipo =
                tipoCaracteristicaService.buscarPorId(idTipoCaracteristica);

        if (tipo == null) {
            return "Sin tipo";
        }

        return tipo.getNombre();
    }

    public void nuevo() {

        caracteristica = new Caracteristica();

        caracteristica.setIdCaracteristica(UUID.randomUUID());

        caracteristica.setActivo(true);

        idTipoSeleccionado = null;

        editando = false;
    }

    public void guardar() {

        if (idTipoSeleccionado == null) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Error",
                            "Debe seleccionar un tipo de característica."
                    )
            );

            return;
        }

        if (!caracteristicaService.tipoActivo(idTipoSeleccionado)) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Error",
                            "No se puede utilizar un tipo de característica inactivo."
                    )
            );

            return;
        }

        caracteristica.setIdTipoCaracteristica(idTipoSeleccionado);

        if (editando) {

            caracteristicaService.actualizar(caracteristica);

        } else {

            boolean guardada =
                    caracteristicaService.guardar(caracteristica);

            if (!guardada) {

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                "Error",
                                "No se pudo guardar la característica."
                        )
                );

                return;
            }
        }

        cargarCaracteristicas();
        cargarTiposActivos();
        nuevo();
    }

    public void editar(Caracteristica caracteristicaSeleccionada) {

        caracteristica = caracteristicaSeleccionada;

        idTipoSeleccionado =
                caracteristicaSeleccionada.getIdTipoCaracteristica();

        editando = true;
    }

    public void eliminar(UUID id) {

        caracteristicaService.eliminar(id);

        cargarCaracteristicas();

        nuevo();
    }

    public void cancelar() {
        nuevo();
    }

    public List<Caracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public List<SelectItem> getOpcionesTipos() {
        return opcionesTipos;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public Caracteristica getCaracteristicaSeleccionada() {
        return caracteristicaSeleccionada;
    }

    public UUID getIdTipoSeleccionado() {
        return idTipoSeleccionado;
    }

    public void setIdTipoSeleccionado(UUID idTipoSeleccionado) {
        this.idTipoSeleccionado = idTipoSeleccionado;
    }

    public boolean isEditando() {
        return editando;
    }
}