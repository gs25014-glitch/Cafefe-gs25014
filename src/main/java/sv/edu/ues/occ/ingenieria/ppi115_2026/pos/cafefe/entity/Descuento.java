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
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "descuento")
public class Descuento {

    @Id
    @Column(name = "id_descuento")
    private UUID idDescuento;

    @Column(name = "id_tipo_descuento")
    private UUID idTipoDescuento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha_desde")
    private OffsetDateTime fechaDesde;

    @Column(name = "fecha_hasta")
    private OffsetDateTime fechaHasta;

    @Column(name = "observaciones")
    private String observaciones;

    public Descuento() {
    }

    public UUID getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(UUID idDescuento) {
        this.idDescuento = idDescuento;
    }

    public UUID getIdTipoDescuento() {
        return idTipoDescuento;
    }

    public void setIdTipoDescuento(UUID idTipoDescuento) {
        this.idTipoDescuento = idTipoDescuento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}