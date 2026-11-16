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
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoProductoService;

@Named("tipoProductoBean")
@ViewScoped
public class tipoProductoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TipoProductoService tipoProductoService;

    private List<TipoProducto> tiposProducto;
    private TipoProducto tipoProducto;
    private TipoProducto tipoProductoSeleccionado;

    @PostConstruct
    public void inicializar() {
        tiposProducto = tipoProductoService.listar();
        tipoProducto = new TipoProducto();
    }

    public List<TipoProducto> getTiposProducto() {
        return tiposProducto;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public TipoProducto getTipoProductoSeleccionado() {
        return tipoProductoSeleccionado;
    }

    public void guardar() {
        tipoProducto.setIdTipoProducto(UUID.randomUUID());

        try {
            tipoProductoService.guardar(tipoProducto);
        } catch (Exception e) {
            mensajeError("No se pudo guardar el tipo de producto. Revise los datos e intente de nuevo.");
            return;
        }

        tiposProducto = tipoProductoService.listar();

        tipoProducto = new TipoProducto();
    }

    public void editar(TipoProducto tipoProducto) {
        tipoProductoSeleccionado = tipoProducto;
    }

    public void actualizar() {
        try {
            tipoProductoService.actualizar(tipoProductoSeleccionado);
        } catch (IllegalArgumentException e) {
            mensajeError(e.getMessage());
        } catch (Exception e) {
            mensajeError("No se pudo actualizar el tipo de producto. Revise los datos e intente de nuevo.");
        }

        tiposProducto = tipoProductoService.listar();

        tipoProductoSeleccionado = null;
    }

    public void eliminar(TipoProducto tipoProducto) {
        try {
            tipoProductoService.eliminar(tipoProducto.getIdTipoProducto());
        } catch (Exception e) {
            // Pasa si algún producto usa este tipo
            mensajeError("No se puede eliminar el tipo porque hay productos que lo usan.");
        }

        tiposProducto = tipoProductoService.listar();
    }

    private void mensajeError(String texto) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
    }

}