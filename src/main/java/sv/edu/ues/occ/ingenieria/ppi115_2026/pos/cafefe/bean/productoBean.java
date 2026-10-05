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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.ProductoTipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoCaracteristica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoProducto;

import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.CaracteristicaService;
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

    private List<Producto> productos;

    // Texto ya resuelto para la tabla: id de producto -> tipo / características
    private Map<UUID, String> tiposPorProducto = new HashMap<>();

    private Map<UUID, String> caracteristicasPorProducto = new HashMap<>();

    private Producto producto;

    private Producto productoSeleccionado;

    private List<TipoProducto> tiposProductoActivos;

    private List<Caracteristica> caracteristicasActivas;

    private UUID idTipoProductoSeleccionado;

    private UUID idCaracteristicaSeleccionada;

    private String valorCaracteristica;

    @PostConstruct
    public void inicializar() {

        recargarProductos();

        cargarTiposProducto();

        cargarCaracteristicas();

        nuevoProducto();
    }

    /**
     * Vuelve a leer los productos y resuelve, una sola vez por carga,
     * el tipo y las características de cada uno para mostrarlos en la tabla.
     */
    private void recargarProductos() {

        productos = productoService.listar();

        tiposPorProducto = new HashMap<>();
        caracteristicasPorProducto = new HashMap<>();

        for (Producto p : productos) {

            tiposPorProducto.put(
                    p.getIdProducto(),
                    resolverTipo(p.getIdProducto())
            );

            caracteristicasPorProducto.put(
                    p.getIdProducto(),
                    resolverCaracteristicas(p.getIdProducto())
            );
        }
    }

    private String resolverTipo(UUID idProducto) {

        ProductoTipoProducto relacion =
                productoTipoProductoService.buscarPorProducto(idProducto);

        if (relacion == null) {
            return "Sin tipo";
        }

        TipoProducto tipo =
                tipoProductoService.buscarPorId(relacion.getIdTipoProducto());

        return tipo != null ? tipo.getNombre() : "Sin tipo";
    }

    private String resolverCaracteristicas(UUID idProducto) {

        List<ProductoCaracteristica> relaciones =
                productoCaracteristicaService.listarPorProducto(idProducto);

        if (relaciones == null || relaciones.isEmpty()) {
            return "Sin características";
        }

        return relaciones.stream()
                .map(r -> {
                    Caracteristica c =
                            caracteristicaService.buscarPorId(
                                    r.getIdCaracteristica());

                    String nombre = c != null ? c.getNombre() : "(eliminada)";

                    return nombre + ": " + r.getValor();
                })
                .collect(Collectors.joining(", "));
    }

    public String tipoDeProducto(Producto producto) {

        return tiposPorProducto.getOrDefault(
                producto.getIdProducto(), "Sin tipo");
    }

    public String caracteristicasDeProducto(Producto producto) {

        return caracteristicasPorProducto.getOrDefault(
                producto.getIdProducto(), "Sin características");
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

            mensajeError(
                    "El tipo de producto seleccionado no está activo."
            );

            return;
        }

        // Validar característica
        if (!validarCaracteristica(
                idCaracteristicaSeleccionada,
                valorCaracteristica)) {

            return;
        }

        // Crear la relación producto-característica
        List<ProductoCaracteristica> caracteristicas =
                new java.util.ArrayList<>();

        if (idCaracteristicaSeleccionada != null) {

            ProductoCaracteristica relacionCaracteristica =
                    new ProductoCaracteristica();

            relacionCaracteristica.setIdCaracteristica(
                    idCaracteristicaSeleccionada
            );

            relacionCaracteristica.setValor(
                    valorCaracteristica
            );

            caracteristicas.add(relacionCaracteristica);
        }

        // Guardar producto, tipo de producto y característica
        productoService.guardarProductoCompleto(
                producto,
                idTipoProductoSeleccionado,
                caracteristicas
        );

        // Actualizar lista
        recargarProductos();

        // Preparar nuevo producto
        nuevoProducto();

        mensajeExito(
                "Producto guardado correctamente."
        );
    }

    private boolean validarCaracteristica(
            UUID idCaracteristica,
            String valor) {

        // La característica es opcional
        if (idCaracteristica == null) {

            return true;
        }

        // El valor es obligatorio si se seleccionó característica
        if (valor == null || valor.trim().isEmpty()) {

            mensajeError(
                    "Debe ingresar el valor de la característica."
            );

            return false;
        }

        // Buscar característica
        Caracteristica caracteristica =
                caracteristicaService.buscarPorId(
                        idCaracteristica
                );

        if (caracteristica == null
                || !Boolean.TRUE.equals(caracteristica.getActivo())) {

            mensajeError(
                    "La característica seleccionada no está activa."
            );

            return false;
        }

        // Buscar tipo de característica
        TipoCaracteristica tipoCaracteristica =
                tipoCaracteristicaService.buscarPorId(
                        caracteristica.getIdTipoCaracteristica()
                );

        if (tipoCaracteristica == null
                || !Boolean.TRUE.equals(tipoCaracteristica.getActivo())) {

            mensajeError(
                    "La característica pertenece a un tipo inactivo."
            );

            return false;
        }

        // Obtener expresión regular
        String expresionRegular =
                tipoCaracteristica.getExpresionRegular();

        if (expresionRegular != null
                && !expresionRegular.isBlank()) {

            try {

                boolean valido =
                        Pattern.matches(
                                expresionRegular,
                                valor
                        );

                if (!valido) {

                    mensajeError(
                            "El valor \""
                            + valor
                            + "\" no cumple con la expresión regular."
                    );

                    return false;
                }

            } catch (Exception e) {

                mensajeError(
                        "La expresión regular de la característica "
                        + "no es válida."
                );

                return false;
            }
        }

        return true;
    }

    public void editar(Producto producto) {

        productoSeleccionado = producto;
    }

    public void actualizar() {

        productoService.actualizar(
                productoSeleccionado
        );

        recargarProductos();

        productoSeleccionado = null;
    }

    public void eliminar(Producto producto) {

        productoService.eliminar(
                producto.getIdProducto()
        );

        recargarProductos();
    }

    private void mensajeError(String mensaje) {

        FacesContext.getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                mensaje,
                                null
                        )
                );
    }

    private void mensajeExito(String mensaje) {

        FacesContext.getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                mensaje,
                                null
                        )
                );
    }
}