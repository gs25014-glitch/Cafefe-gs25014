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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Descuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoDescuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.DescuentoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoDescuentoService;

@Named("descuentoBean")
@ViewScoped
public class descuentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private DescuentoService descuentoService;

    @Inject
    private TipoDescuentoService tipoDescuentoService;

    private List<Descuento> descuentos;

    private List<SelectItem> opcionesTipos;

    private Descuento descuento;

    private UUID idTipoSeleccionado;

    private String fechaDesdeTexto;

    private String fechaHastaTexto;

    private boolean editando;

    @PostConstruct
    public void inicializar() {
        cargarDescuentos();
        cargarTiposActivos();
        nuevo();
    }

    public void cargarDescuentos() {
        descuentos = descuentoService.listar();
    }

    public void cargarTiposActivos() {

        opcionesTipos = new ArrayList<>();

        List<TipoDescuento> tipos =
                tipoDescuentoService.listarActivos();

        for (TipoDescuento tipo : tipos) {

            opcionesTipos.add(
                    new SelectItem(
                            tipo.getIdTipoDescuento(),
                            tipo.getNombre()
                    )
            );
        }
    }

    public void nuevo() {

        descuento = new Descuento();

        descuento.setIdDescuento(UUID.randomUUID());

        idTipoSeleccionado = null;

        fechaDesdeTexto = "";

        fechaHastaTexto = "";

        editando = false;
    }

    public void guardar() {

        if (idTipoSeleccionado == null) {

            mostrarError(
                    "Debe seleccionar un tipo de descuento."
            );

            return;
        }

        try {

            OffsetDateTime fechaDesde = inicioDelDia(fechaDesdeTexto);

            OffsetDateTime fechaHasta = null;

            if (fechaHastaTexto != null
                    && !fechaHastaTexto.trim().isEmpty()) {

                fechaHasta = finDelDia(fechaHastaTexto);
            }

            descuento.setIdTipoDescuento(idTipoSeleccionado);
            descuento.setFechaDesde(fechaDesde);
            descuento.setFechaHasta(fechaHasta);

            boolean resultado;

            if (editando) {

                resultado =
                        descuentoService.actualizar(descuento) != null;

            } else {

                resultado =
                        descuentoService.guardar(descuento);
            }

            if (!resultado) {

                mostrarError(
                        "El tipo de descuento no está activo "
                        + "o las fechas no son válidas."
                );

                return;
            }

            cargarDescuentos();
            cargarTiposActivos();
            nuevo();

        } catch (Exception e) {

            mostrarError(
                    "Seleccione fechas válidas usando el calendario."
            );
        }
    }

    // Fecha sin hora para mostrar en la tabla
    public String fecha(OffsetDateTime fecha) {

        if (fecha == null) {
            return "";
        }

        return fecha.atZoneSameInstant(ZONA).toLocalDate().toString();
    }

    private static final ZoneId ZONA = ZoneId.of("America/El_Salvador");

    // "2026-10-01" (calendario) -> inicio de ese día
    private OffsetDateTime inicioDelDia(String texto) {
        return LocalDate.parse(texto.trim()).atStartOfDay(ZONA).toOffsetDateTime();
    }

    // "2026-10-01" (calendario) -> final de ese día, para que cuente completo
    private OffsetDateTime finDelDia(String texto) {
        return LocalDate.parse(texto.trim()).atTime(23, 59, 59).atZone(ZONA).toOffsetDateTime();
    }

    public void editar(Descuento descuentoSeleccionado) {

        descuento = descuentoSeleccionado;

        idTipoSeleccionado =
                descuentoSeleccionado.getIdTipoDescuento();

        fechaDesdeTexto =
                fecha(descuentoSeleccionado.getFechaDesde());

        fechaHastaTexto =
                fecha(descuentoSeleccionado.getFechaHasta());

        editando = true;
    }

    public void eliminar(UUID id) {

        descuentoService.eliminar(id);

        cargarDescuentos();

        nuevo();
    }

    public void cancelar() {
        nuevo();
    }

    private void mostrarError(String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        mensaje
                )
        );
    }

    public List<Descuento> getDescuentos() {
        return descuentos;
    }

    public List<SelectItem> getOpcionesTipos() {
        return opcionesTipos;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public UUID getIdTipoSeleccionado() {
        return idTipoSeleccionado;
    }

    public void setIdTipoSeleccionado(UUID idTipoSeleccionado) {
        this.idTipoSeleccionado = idTipoSeleccionado;
    }

    public String getFechaDesdeTexto() {
        return fechaDesdeTexto;
    }

    public void setFechaDesdeTexto(String fechaDesdeTexto) {
        this.fechaDesdeTexto = fechaDesdeTexto;
    }

    public String getFechaHastaTexto() {
        return fechaHastaTexto;
    }

    public void setFechaHastaTexto(String fechaHastaTexto) {
        this.fechaHastaTexto = fechaHastaTexto;
    }

    public boolean isEditando() {
        return editando;
    }
}