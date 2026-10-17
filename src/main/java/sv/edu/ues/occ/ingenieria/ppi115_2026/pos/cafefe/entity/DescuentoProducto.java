/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.pos.cafefe.entity;

/**
 *
 * @author kelvi
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "descuento_producto")
public class DescuentoProducto {

    @jakarta.persistence.Id
    @Column(name = "id_descuento_producto")
    private UUID idDescuentoProducto;

    @Column(name = "id_descuento")
    private UUID idDescuento;

    @Column(name = "id_producto")
    private UUID idProducto;

    @Column(name = "fecha_desde")
    private OffsetDateTime fechaDesde;

    @Column(name = "fecha_hasta")
    private OffsetDateTime fechaHasta;

    @Column(name = "valor")
    private Integer valor;

    @Column(name = "observaciones")
    private String observaciones;

    public DescuentoProducto() {
    }

    public UUID getIdDescuentoProducto() {
        return idDescuentoProducto;
    }

    public void setIdDescuentoProducto(UUID idDescuentoProducto) {
        this.idDescuentoProducto = idDescuentoProducto;
    }

    public UUID getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(UUID idDescuento) {
        this.idDescuento = idDescuento;
    }

    public UUID getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(UUID idProducto) {
        this.idProducto = idProducto;
    }

    public OffsetDateTime getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(OffsetDateTime fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public OffsetDateTime getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(OffsetDateTime fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}