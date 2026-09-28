/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.Producto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.ProductoService;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos/prueba"})
public class ProductoServlet extends HttpServlet {

    @Inject
    private ProductoService productoService;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String idTexto = request.getParameter("id");

            // Si no se proporciona ID, listar todos los productos
            if (idTexto == null || idTexto.isBlank()) {

                List<Producto> productos = productoService.listar();

                out.println("<h1>Lista de productos</h1>");

                if (productos.isEmpty()) {
                    out.println("<p>No hay productos registrados.</p>");
                    return;
                }

                for (Producto producto : productos) {

                    out.println("<hr>");

                    out.println("<p><strong>ID:</strong> "
                            + producto.getIdProducto() + "</p>");

                    out.println("<p><strong>Nombre:</strong> "
                            + producto.getNombre() + "</p>");

                    out.println("<p><strong>Activo:</strong> "
                            + producto.getActivo() + "</p>");

                    out.println("<p><strong>Comentarios:</strong> "
                            + producto.getComentarios() + "</p>");

                    out.println("<p><strong>Precio sugerido:</strong> $"
                            + producto.getPrecioSugerido() + "</p>");
                }

                return;
            }

            // Si se proporciona ID, buscar un producto
            UUID id = UUID.fromString(idTexto);

            Producto producto = productoService.buscarPorId(id);

            if (producto == null) {
                out.println("<h1>Producto no encontrado</h1>");
                return;
            }

            out.println("<h1>Producto encontrado</h1>");

            out.println("<p><strong>ID:</strong> "
                    + producto.getIdProducto() + "</p>");

            out.println("<p><strong>Nombre:</strong> "
                    + producto.getNombre() + "</p>");

            out.println("<p><strong>Activo:</strong> "
                    + producto.getActivo() + "</p>");

            out.println("<p><strong>Comentarios:</strong> "
                    + producto.getComentarios() + "</p>");

            out.println("<p><strong>Precio sugerido:</strong> $"
                    + producto.getPrecioSugerido() + "</p>");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String nombre = request.getParameter("nombre");
            String activoTexto = request.getParameter("activo");
            String comentarios = request.getParameter("comentarios");
            String precioTexto = request.getParameter("precio_sugerido");

            if (nombre == null || nombre.isBlank()) {
                out.println("<h1>Error</h1>");
                out.println("<p>El nombre es obligatorio.</p>");
                return;
            }

            if (precioTexto == null || precioTexto.isBlank()) {
                out.println("<h1>Error</h1>");
                out.println("<p>El precio sugerido es obligatorio.</p>");
                return;
            }

            Boolean activo = Boolean.parseBoolean(activoTexto);
            BigDecimal precioSugerido = new BigDecimal(precioTexto);

            Producto producto = new Producto();

            producto.setIdProducto(UUID.randomUUID());
            producto.setNombre(nombre);
            producto.setActivo(activo);
            producto.setComentarios(comentarios);
            producto.setPrecioSugerido(precioSugerido);

            productoService.guardar(producto);

            out.println("<h1>Producto creado correctamente</h1>");

            out.println("<p><strong>ID:</strong> "
                    + producto.getIdProducto() + "</p>");

            out.println("<p><strong>Nombre:</strong> "
                    + producto.getNombre() + "</p>");

            out.println("<p><strong>Activo:</strong> "
                    + producto.getActivo() + "</p>");

            out.println("<p><strong>Comentarios:</strong> "
                    + producto.getComentarios() + "</p>");

            out.println("<p><strong>Precio sugerido:</strong> $"
                    + producto.getPrecioSugerido() + "</p>");
        }
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String idTexto = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String activoTexto = request.getParameter("activo");
            String comentarios = request.getParameter("comentarios");
            String precioTexto = request.getParameter("precio_sugerido");
            

            if (idTexto == null || idTexto.isBlank()) {
                out.println("<h1>Error</h1>");
                out.println("<p>Debes proporcionar el ID del producto.</p>");
                return;
            }

            UUID id = UUID.fromString(idTexto);

            Producto producto = productoService.buscarPorId(id);

            if (producto == null) {
                out.println("<h1>Producto no encontrado</h1>");
                return;
            }

            if (nombre != null && !nombre.isBlank()) {
                producto.setNombre(nombre);
            }

            if (activoTexto != null && !activoTexto.isBlank()) {
                producto.setActivo(Boolean.parseBoolean(activoTexto));
            }

            if (comentarios != null) {
                producto.setComentarios(comentarios);
            }

            if (precioTexto != null && !precioTexto.isBlank()) {
                producto.setPrecioSugerido(
                        new BigDecimal(precioTexto)
                );
            }

            productoService.actualizar(producto);

            out.println("<h1>Producto actualizado correctamente</h1>");

            out.println("<p><strong>ID:</strong> "
                    + producto.getIdProducto() + "</p>");

            out.println("<p><strong>Nombre:</strong> "
                    + producto.getNombre() + "</p>");

            out.println("<p><strong>Activo:</strong> "
                    + producto.getActivo() + "</p>");

            out.println("<p><strong>Comentarios:</strong> "
                    + producto.getComentarios() + "</p>");

            out.println("<p><strong>Precio sugerido:</strong> $"
                    + producto.getPrecioSugerido() + "</p>");
        }
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String idTexto = request.getParameter("id");

            if (idTexto == null || idTexto.isBlank()) {
                out.println("<h1>Error</h1>");
                out.println("<p>Debes proporcionar el ID del producto.</p>");
                return;
            }

            UUID id = UUID.fromString(idTexto);

            Producto producto = productoService.buscarPorId(id);

            if (producto == null) {
                out.println("<h1>Producto no encontrado</h1>");
                return;
            }

            productoService.eliminar(id);

            out.println("<h1>Producto eliminado correctamente</h1>");

            out.println("<p><strong>ID eliminado:</strong> "
                    + id + "</p>");
        }
    }
}