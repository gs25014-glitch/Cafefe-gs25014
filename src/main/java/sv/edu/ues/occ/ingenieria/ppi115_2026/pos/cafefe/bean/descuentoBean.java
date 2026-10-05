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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    // El calendario (input datetime-local) entrega "2026-10-01T14:30" sin zona;
    // se interpreta en la zona horaria de El Salvador (UTC-6), sin importar
    // la zona del servidor o del contenedor Docker.
    private static final ZoneId ZONA = ZoneId.of("America/El_Salvador");

    private static final DateTimeFormatter FORMATO_CALENDARIO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

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

            OffsetDateTime fechaDesde = textoAFecha(fechaDesdeTexto);

            OffsetDateTime fechaHasta = null;

            if (fechaHastaTexto != null
                    && !fechaHastaTexto.trim().isEmpty()) {

                fechaHasta = textoAFecha(fechaHastaTexto);
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

    // "2026-10-01T14:30" (calendario) -> OffsetDateTime en zona El Salvador
    private OffsetDateTime textoAFecha(String texto) {

        return LocalDateTime.parse(texto.trim())
                .atZone(ZONA)
                .toOffsetDateTime();
    }

    // OffsetDateTime -> "2026-10-01T14:30" para rellenar el calendario
    private String fechaATexto(OffsetDateTime fecha) {

        if (fecha == null) {
            return "";
        }

        return fecha.atZoneSameInstant(ZONA)
                .toLocalDateTime()
                .format(FORMATO_CALENDARIO);
    }

    public void editar(Descuento descuentoSeleccionado) {

        descuento = descuentoSeleccionado;

        idTipoSeleccionado =
                descuentoSeleccionado.getIdTipoDescuento();

        fechaDesdeTexto =
                fechaATexto(descuentoSeleccionado.getFechaDesde());

        fechaHastaTexto =
                fechaATexto(descuentoSeleccionado.getFechaHasta());

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