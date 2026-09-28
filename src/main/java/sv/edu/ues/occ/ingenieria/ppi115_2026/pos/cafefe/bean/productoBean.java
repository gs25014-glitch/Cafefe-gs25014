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
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoService;

@Named("productoBean")
@ViewScoped
public class productoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProductoService productoService;

    private List<Producto> productos;
    private Producto producto;
    private Producto productoSeleccionado;

    @PostConstruct
    public void inicializar() {
        productos = productoService.listar();
        producto = new Producto();
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

    public void guardar() {
        producto.setIdProducto(UUID.randomUUID());

        productoService.guardar(producto);

        productos = productoService.listar();

        producto = new Producto();
    }

    public void editar(Producto producto) {
        productoSeleccionado = producto;
    }

    public void actualizar() {
        productoService.actualizar(productoSeleccionado);

        productos = productoService.listar();

        productoSeleccionado = null;
    }
    
    public void eliminar(Producto producto) {
        productoService.eliminar(producto.getIdProducto()); 

        productos = productoService.listar();
    }
}