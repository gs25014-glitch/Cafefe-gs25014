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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Descuento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.DescuentoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoTipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoProducto;

import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CaracteristicaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.DescuentoProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.DescuentoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoCaracteristicaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoTipoProductoService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoCaracteristicaService;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoProductoService;

@Named("productoBean")
@ViewScoped
public class productoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProductoService productoService;

    @Inject
    private TipoProductoService tipoProductoService;

    @Inject
    private CaracteristicaService caracteristicaService;

    @Inject
    private TipoCaracteristicaService tipoCaracteristicaService;

    @Inject
    private ProductoTipoProductoService productoTipoProductoService;

    @Inject
    private ProductoCaracteristicaService productoCaracteristicaService;

    @Inject
    private DescuentoService descuentoService;

    @Inject
    private DescuentoProductoService descuentoProductoService;

    private List<Producto> productos;

    private Producto producto;

    private Producto productoSeleccionado;

    private List<TipoProducto> tiposProductoActivos;

    private List<Caracteristica> caracteristicasActivas;

    private UUID idTipoProductoSeleccionado;

    private UUID idCaracteristicaSeleccionada;

    private String valorCaracteristica;

    // Al editar solo se muestran (deshabilitados)
    private UUID idTipoProductoEditar;

    private UUID idCaracteristicaEditar;

    private String valorCaracteristicaEditar;

    // Para asignar un descuento a un producto
    private UUID idProductoDescuento;

    private UUID idDescuentoSeleccionado;


    private String fechaDesdeDescuento;

    private String fechaHastaDescuento;

    @PostConstruct
    public void inicializar() {

        productos = productoService.listar();

        cargarTiposProducto();

        cargarCaracteristicas();

        nuevoProducto();
    }

    public void cargarTiposProducto() {

        tiposProductoActivos = tipoProductoService.listar()
                .stream()
                .filter(t -> Boolean.TRUE.equals(t.getActivo()))
                .toList();
    }

    public void cargarCaracteristicas() {

        caracteristicasActivas = caracteristicaService.listar()
                .stream()
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .toList();
    }

    public void nuevoProducto() {

        producto = new Producto();

        producto.setIdProducto(UUID.randomUUID());

        producto.setActivo(true);

        idTipoProductoSeleccionado = null;

        idCaracteristicaSeleccionada = null;

        valorCaracteristica = "";
    }

    // ---------- Datos para la tabla de productos ----------

    public String tipoDeProducto(Producto p) {

        ProductoTipoProducto relacion =
                productoTipoProductoService.buscarPorProducto(p.getIdProducto());

        if (relacion == null) {
            return "Sin tipo";
        }

        return tipoProductoService.buscarPorId(relacion.getIdTipoProducto())
                .getNombre();
    }

    public String caracteristicasDeProducto(Producto p) {

        String texto = "";

        for (ProductoCaracteristica pc
                : productoCaracteristicaService.listarPorProducto(p.getIdProducto())) {

            Caracteristica c =
                    caracteristicaService.buscarPorId(pc.getIdCaracteristica());

            texto = texto + c.getNombre() + ": " + pc.getValor() + "  ";
        }

        return texto.isEmpty() ? "Sin características" : texto;
    }

    public String descuentosDeProducto(Producto p) {

        String texto = "";

        for (DescuentoProducto dp
                : descuentoProductoService.listarPorProducto(p.getIdProducto())) {

            texto = texto + nombreDescuento(dp.getIdDescuento())
                    + " (" + dp.getValor() + "%)  ";
        }

        return texto.isEmpty() ? "Sin descuentos" : texto;
    }

    // ---------- Guardar producto ----------

    public void guardar() {

        // Validar tipo de producto
        if (idTipoProductoSeleccionado == null) {
            mensajeError("Debe seleccionar un tipo de producto.");
            return;
        }

        TipoProducto tipoProducto =
                tipoProductoService.buscarPorId(idTipoProductoSeleccionado);

        if (tipoProducto == null
                || !Boolean.TRUE.equals(tipoProducto.getActivo())) {
            mensajeError("El tipo de producto seleccionado no está activo.");
            return;
        }

        // Validar característica
        if (!validarCaracteristica(
                idCaracteristicaSeleccionada,
                valorCaracteristica)) {
            return;
        }

        // Crear la relación producto-característica
        List<ProductoCaracteristica> caracteristicas = new ArrayList<>();

        if (idCaracteristicaSeleccionada != null) {

            ProductoCaracteristica relacion = new ProductoCaracteristica();
            relacion.setIdCaracteristica(idCaracteristicaSeleccionada);
            relacion.setValor(valorCaracteristica);

            caracteristicas.add(relacion);
        }

        try {
            productoService.guardarProductoCompleto(
                    producto,
                    idTipoProductoSeleccionado,
                    caracteristicas
            );
        } catch (Exception e) {
            mensajeError("No se pudo guardar el producto. Revise los datos e intente de nuevo.");
            return;
        }

        productos = productoService.listar();

        nuevoProducto();

        mensajeExito("Producto guardado correctamente.");
    }

    private boolean validarCaracteristica(UUID idCaracteristica, String valor) {

        // La característica es opcional
        if (idCaracteristica == null) {
            return true;
        }

        // El valor es obligatorio si se seleccionó característica
        if (valor == null || valor.trim().isEmpty()) {
            mensajeError("Debe ingresar el valor de la característica.");
            return false;
        }

        Caracteristica caracteristica =
                caracteristicaService.buscarPorId(idCaracteristica);

        if (caracteristica == null
                || !Boolean.TRUE.equals(caracteristica.getActivo())) {
            mensajeError("La característica seleccionada no está activa.");
            return false;
        }

        TipoCaracteristica tipoCaracteristica =
                tipoCaracteristicaService.buscarPorId(
                        caracteristica.getIdTipoCaracteristica());

        if (tipoCaracteristica == null
                || !Boolean.TRUE.equals(tipoCaracteristica.getActivo())) {
            mensajeError("La característica pertenece a un tipo inactivo.");
            return false;
        }

        // Validar con la expresión regular del tipo
        String expresionRegular = tipoCaracteristica.getExpresionRegular();

        if (expresionRegular != null && !expresionRegular.isBlank()) {

            try {

                if (!Pattern.matches(expresionRegular, valor)) {
                    mensajeError("El valor \"" + valor
                            + "\" no cumple con la expresión regular.");
                    return false;
                }

            } catch (Exception e) {
                mensajeError("La expresión regular de la característica no es válida.");
                return false;
            }
        }

        return true;
    }

    // ---------- Editar producto ----------

    public void editar(Producto producto) {

        productoSeleccionado = producto;

        ProductoTipoProducto relacion =
                productoTipoProductoService.buscarPorProducto(producto.getIdProducto());

        idTipoProductoEditar = relacion != null
                ? relacion.getIdTipoProducto()
                : null;

        idCaracteristicaEditar = null;
        valorCaracteristicaEditar = "";

        List<ProductoCaracteristica> lista =
                productoCaracteristicaService.listarPorProducto(producto.getIdProducto());

        if (!lista.isEmpty()) {
            idCaracteristicaEditar = lista.get(0).getIdCaracteristica();
            valorCaracteristicaEditar = lista.get(0).getValor();
        }
    }

    public void actualizar() {

        try {
            productoService.actualizar(productoSeleccionado);
        } catch (IllegalArgumentException e) {
            mensajeError(e.getMessage());
        } catch (Exception e) {
            mensajeError("No se pudo actualizar el producto. Revise los datos e intente de nuevo.");
        }

        productos = productoService.listar();

        productoSeleccionado = null;
    }

    public void cancelarEdicion() {

        productoSeleccionado = null;

        productos = productoService.listar();
    }

    public void eliminar(Producto producto) {

        // Si tiene filas relacionadas no se elimina y se avisa por qué
        String motivo = productoService.motivoNoEliminar(producto.getIdProducto());

        if (motivo != null) {
            mensajeLista(FacesMessage.SEVERITY_ERROR, "No se puede eliminar \""
                    + producto.getNombre() + "\" porque" + motivo);
            return;
        }

        try {
            productoService.eliminar(producto.getIdProducto());
            mensajeLista(FacesMessage.SEVERITY_INFO, "Producto eliminado correctamente.");
        } catch (Exception e) {
            mensajeLista(FacesMessage.SEVERITY_ERROR, "No se pudo eliminar el producto.");
        }

        productos = productoService.listar();
    }

    // ---------- Asignar descuento a producto ----------

    public void asignarDescuento() {

        if (idProductoDescuento == null || idDescuentoSeleccionado == null) {
            mensajeDescuento(FacesMessage.SEVERITY_ERROR,
                    "Debe seleccionar un producto y un descuento.");
            return;
        }

        DescuentoProducto dp = new DescuentoProducto();
        dp.setIdDescuentoProducto(UUID.randomUUID());
        dp.setIdProducto(idProductoDescuento);
        dp.setIdDescuento(idDescuentoSeleccionado);

        try {
            if (fechaDesdeDescuento != null && !fechaDesdeDescuento.isBlank()) {
                dp.setFechaDesde(inicioDelDia(fechaDesdeDescuento));
            }

            if (fechaHastaDescuento != null && !fechaHastaDescuento.isBlank()) {
                dp.setFechaHasta(finDelDia(fechaHastaDescuento));
            }
        } catch (DateTimeParseException e) {
            mensajeDescuento(FacesMessage.SEVERITY_ERROR,
                    "Seleccione las fechas con el calendario.");
            return;
        }

        // El servicio valida el valor máximo y las fechas
        String error = descuentoProductoService.asignar(dp);

        if (error != null) {
            mensajeDescuento(FacesMessage.SEVERITY_ERROR, error);
            return;
        }

        limpiarDescuento();

        mensajeDescuento(FacesMessage.SEVERITY_INFO,
                "Descuento asignado correctamente.");
    }

    // Texto del select: nombre del descuento y su porcentaje
    public String etiquetaDescuento(Descuento d) {
        return d.getNombre() + " (" + descuentoProductoService.porcentaje(d) + "%)";
    }

    public void quitarDescuento(DescuentoProducto dp) {

        try {
            descuentoProductoService.eliminar(dp.getIdDescuentoProducto());
        } catch (Exception e) {
            mensajeDescuento(FacesMessage.SEVERITY_ERROR, "No se pudo quitar el descuento.");
        }
    }

    public void limpiarDescuento() {

        idProductoDescuento = null;
        idDescuentoSeleccionado = null;
        fechaDesdeDescuento = "";
        fechaHastaDescuento = "";
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

    public List<Producto> getProductosActivos() {

        List<Producto> lista = new ArrayList<>();

        for (Producto p : productos) {
            if (Boolean.TRUE.equals(p.getActivo())) {
                lista.add(p);
            }
        }

        return lista;
    }

    // Solo los descuentos cuyo tipo está activo
    public List<Descuento> getDescuentosDisponibles() {

        List<Descuento> lista = new ArrayList<>();

        for (Descuento d : descuentoService.listar()) {
            if (descuentoService.tipoActivo(d.getIdTipoDescuento())) {
                lista.add(d);
            }
        }

        return lista;
    }

    public List<DescuentoProducto> getAsignaciones() {
        return descuentoProductoService.listar();
    }

    public String nombreProducto(UUID idProducto) {
        return productoService.buscarPorId(idProducto).getNombre();
    }

    public String nombreDescuento(UUID idDescuento) {
        return descuentoService.buscarPorId(idDescuento).getNombre();
    }

    // ---------- Mensajes ----------

    private void mensajeError(String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }

    private void mensajeExito(String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null));
    }

    // Se muestra solo en el formulario de descuentos
    private void mensajeLista(FacesMessage.Severity tipo, String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                "formLista:tablaProductos",
                new FacesMessage(tipo, mensaje, null));
    }

    private void mensajeDescuento(FacesMessage.Severity tipo, String mensaje) {

        FacesContext.getCurrentInstance().addMessage(
                "formDescuento:botonAsignar",
                new FacesMessage(tipo, mensaje, null));
    }

    // ---------- Getters y setters ----------

    public List<Producto> getProductos() {
        return productos;
    }

    public Producto getProducto() {
        return producto;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public List<TipoProducto> getTiposProductoActivos() {
        return tiposProductoActivos;
    }

    public List<Caracteristica> getCaracteristicasActivas() {
        return caracteristicasActivas;
    }

    public List<TipoProducto> getTiposProductoTodos() {
        return tipoProductoService.listar();
    }

    public List<Caracteristica> getCaracteristicasTodas() {
        return caracteristicaService.listar();
    }

    public UUID getIdTipoProductoSeleccionado() {
        return idTipoProductoSeleccionado;
    }

    public void setIdTipoProductoSeleccionado(UUID idTipoProductoSeleccionado) {
        this.idTipoProductoSeleccionado = idTipoProductoSeleccionado;
    }

    public UUID getIdCaracteristicaSeleccionada() {
        return idCaracteristicaSeleccionada;
    }

    public void setIdCaracteristicaSeleccionada(UUID idCaracteristicaSeleccionada) {
        this.idCaracteristicaSeleccionada = idCaracteristicaSeleccionada;
    }

    public String getValorCaracteristica() {
        return valorCaracteristica;
    }

    public void setValorCaracteristica(String valorCaracteristica) {
        this.valorCaracteristica = valorCaracteristica;
    }

    public UUID getIdTipoProductoEditar() {
        return idTipoProductoEditar;
    }

    public void setIdTipoProductoEditar(UUID idTipoProductoEditar) {
        this.idTipoProductoEditar = idTipoProductoEditar;
    }

    public UUID getIdCaracteristicaEditar() {
        return idCaracteristicaEditar;
    }

    public void setIdCaracteristicaEditar(UUID idCaracteristicaEditar) {
        this.idCaracteristicaEditar = idCaracteristicaEditar;
    }

    public String getValorCaracteristicaEditar() {
        return valorCaracteristicaEditar;
    }

    public void setValorCaracteristicaEditar(String valorCaracteristicaEditar) {
        this.valorCaracteristicaEditar = valorCaracteristicaEditar;
    }

    public UUID getIdProductoDescuento() {
        return idProductoDescuento;
    }

    public void setIdProductoDescuento(UUID idProductoDescuento) {
        this.idProductoDescuento = idProductoDescuento;
    }

    public UUID getIdDescuentoSeleccionado() {
        return idDescuentoSeleccionado;
    }

    public void setIdDescuentoSeleccionado(UUID idDescuentoSeleccionado) {
        this.idDescuentoSeleccionado = idDescuentoSeleccionado;
    }


    public String getFechaDesdeDescuento() {
        return fechaDesdeDescuento;
    }

    public void setFechaDesdeDescuento(String fechaDesdeDescuento) {
        this.fechaDesdeDescuento = fechaDesdeDescuento;
    }

    public String getFechaHastaDescuento() {
        return fechaHastaDescuento;
    }

    public void setFechaHastaDescuento(String fechaHastaDescuento) {
        this.fechaHastaDescuento = fechaHastaDescuento;
    }
}