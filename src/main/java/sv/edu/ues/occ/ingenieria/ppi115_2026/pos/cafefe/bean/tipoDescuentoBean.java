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

        if (tipoDescuento.getNombre() == null
                || tipoDescuento.getNombre().trim().isEmpty()) {

            mensajeError("Debe ingresar el nombre del tipo de descuento.");
            return;
        }

        if (tipoDescuento.getDescuentoMaximo() == null) {

            mensajeError("Debe ingresar el límite máximo de descuento.");
            return;
        }

        if (tipoDescuento.getDescuentoMaximo() < 0
                || tipoDescuento.getDescuentoMaximo() > 100) {

            mensajeError("El límite máximo debe estar entre 0 y 100.");
            return;
        }

        if (editando) {

            tipoDescuentoService.actualizar(tipoDescuento);

            mensajeExito("Tipo de descuento actualizado correctamente.");

        } else {

            tipoDescuentoService.guardar(tipoDescuento);

            mensajeExito("Tipo de descuento guardado correctamente.");
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

            mensajeExito("Tipo de descuento eliminado correctamente.");

        } catch (Exception e) {

            mensajeError(
                    "No se puede eliminar el tipo de descuento porque está siendo utilizado."
            );
        }

        cargarTipos();
        nuevo();
    }

    public void cancelar() {
        nuevo();
    }

    private void mensajeError(String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        mensaje,
                        null
                )
        );
    }

    private void mensajeExito(String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        mensaje,
                        null
                )
        );
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