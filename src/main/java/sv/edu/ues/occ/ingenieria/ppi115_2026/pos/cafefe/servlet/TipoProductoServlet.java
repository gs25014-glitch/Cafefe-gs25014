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
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity.TipoProducto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.service.TipoProductoService;

@WebServlet(
        name = "TipoProductoServlet",
        urlPatterns = {"/tipos-producto"}
)
public class TipoProductoServlet extends HttpServlet {

    @Inject
    private TipoProductoService tipoProductoService;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String idTexto = request.getParameter("id");

            if (idTexto == null || idTexto.isBlank()) {

                List<TipoProducto> tipos =
                        tipoProductoService.listar();

                out.println("<h1>Lista de tipos de producto</h1>");

                if (tipos.isEmpty()) {
                    out.println("<p>No hay tipos de producto registrados.</p>");
                    return;
                }

                for (TipoProducto tipo : tipos) {

                    out.println("<hr>");

                    out.println("<p><strong>ID:</strong> "
                            + tipo.getIdTipoProducto() + "</p>");

                    out.println("<p><strong>Nombre:</strong> "
                            + tipo.getNombre() + "</p>");

                    out.println("<p><strong>Activo:</strong> "
                            + tipo.getActivo() + "</p>");

                    out.println("<p><strong>Observaciones:</strong> "
                            + tipo.getObservaciones() + "</p>");
                }

                return;
            }

            UUID id = UUID.fromString(idTexto);

            TipoProducto tipo =
                    tipoProductoService.buscarPorId(id);

            if (tipo == null) {
                out.println("<h1>Tipo de producto no encontrado</h1>");
                return;
            }

            out.println("<h1>Tipo de producto encontrado</h1>");

            out.println("<p><strong>ID:</strong> "
                    + tipo.getIdTipoProducto() + "</p>");

            out.println("<p><strong>Nombre:</strong> "
                    + tipo.getNombre() + "</p>");

            out.println("<p><strong>Activo:</strong> "
                    + tipo.getActivo() + "</p>");

            out.println("<p><strong>Observaciones:</strong> "
                    + tipo.getObservaciones() + "</p>");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String nombre = request.getParameter("nombre");
            String activoTexto = request.getParameter("activo");
            String observaciones =
                    request.getParameter("observaciones");

            if (nombre == null || nombre.isBlank()) {
                out.println("<h1>Error</h1>");
                out.println("<p>El nombre es obligatorio.</p>");
                return;
            }

            TipoProducto tipo = new TipoProducto();

            tipo.setIdTipoProducto(UUID.randomUUID());
            tipo.setNombre(nombre);
            tipo.setActivo(Boolean.parseBoolean(activoTexto));
            tipo.setObservaciones(observaciones);

            tipoProductoService.guardar(tipo);

            out.println("<h1>Tipo de producto creado correctamente</h1>");

            out.println("<p><strong>ID:</strong> "
                    + tipo.getIdTipoProducto() + "</p>");

            out.println("<p><strong>Nombre:</strong> "
                    + tipo.getNombre() + "</p>");

            out.println("<p><strong>Activo:</strong> "
                    + tipo.getActivo() + "</p>");

            out.println("<p><strong>Observaciones:</strong> "
                    + tipo.getObservaciones() + "</p>");
        }
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String idTexto = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String activoTexto = request.getParameter("activo");
            String observaciones =
                    request.getParameter("observaciones");

            if (idTexto == null || idTexto.isBlank()) {
                out.println("<h1>Error</h1>");
                out.println("<p>Debes proporcionar el ID.</p>");
                return;
            }

            UUID id = UUID.fromString(idTexto);

            TipoProducto tipo =
                    tipoProductoService.buscarPorId(id);

            if (tipo == null) {
                out.println("<h1>Tipo de producto no encontrado</h1>");
                return;
            }

            if (nombre != null && !nombre.isBlank()) {
                tipo.setNombre(nombre);
            }

            if (activoTexto != null && !activoTexto.isBlank()) {
                tipo.setActivo(Boolean.parseBoolean(activoTexto));
            }

            if (observaciones != null) {
                tipo.setObservaciones(observaciones);
            }

            tipoProductoService.actualizar(tipo);

            out.println("<h1>Tipo de producto actualizado correctamente</h1>");

            out.println("<p><strong>ID:</strong> "
                    + tipo.getIdTipoProducto() + "</p>");

            out.println("<p><strong>Nombre:</strong> "
                    + tipo.getNombre() + "</p>");

            out.println("<p><strong>Activo:</strong> "
                    + tipo.getActivo() + "</p>");

            out.println("<p><strong>Observaciones:</strong> "
                    + tipo.getObservaciones() + "</p>");
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
                out.println("<p>Debes proporcionar el ID.</p>");
                return;
            }

            UUID id = UUID.fromString(idTexto);

            TipoProducto tipo =
                    tipoProductoService.buscarPorId(id);

            if (tipo == null) {
                out.println("<h1>Tipo de producto no encontrado</h1>");
                return;
            }

            tipoProductoService.eliminar(id);

            out.println("<h1>Tipo de producto eliminado correctamente</h1>");

            out.println("<p><strong>ID eliminado:</strong> "
                    + id + "</p>");
        }
    }
}