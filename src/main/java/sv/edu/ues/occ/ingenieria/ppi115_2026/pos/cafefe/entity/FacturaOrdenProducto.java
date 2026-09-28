/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kelvi
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "factura_orden_producto")
public class FacturaOrdenProducto {

    @Id
    @Column(name = "id_factura_orden_producto")
    private UUID idFacturaOrdenProducto;

    @Column(name = "id_factura")
    private UUID idFactura;

    @Column(name = "id_orden_producto")
    private UUID idOrdenProducto;

    @Column(name = "precio", precision = 8, scale = 2)
    private BigDecimal precio;

    @Column(name = "observaciones")
    private String observaciones;

    public FacturaOrdenProducto() {
    }

    public UUID getIdFacturaOrdenProducto() {
        return idFacturaOrdenProducto;
    }

    public void setIdFacturaOrdenProducto(UUID idFacturaOrdenProducto) {
        this.idFacturaOrdenProducto = idFacturaOrdenProducto;
    }

    public UUID getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(UUID idFactura) {
        this.idFactura = idFactura;
    }

    public UUID getIdOrdenProducto() {
        return idOrdenProducto;
    }

    public void setIdOrdenProducto(UUID idOrdenProducto) {
        this.idOrdenProducto = idOrdenProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}