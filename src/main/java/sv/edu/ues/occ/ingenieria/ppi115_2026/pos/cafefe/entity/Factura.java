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
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @Column(name = "id_factura")
    private UUID idFactura;

    @Column(name = "id_caja")
    private UUID idCaja;

    @Column(name = "id_empleado_rol")
    private UUID idEmpleadoRol;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "fecha_facturacion")
    private OffsetDateTime fechaFacturacion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "observaciones")
    private String observaciones;

    public Factura() {
    }

    public UUID getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(UUID idFactura) {
        this.idFactura = idFactura;
    }

    public UUID getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(UUID idCaja) {
        this.idCaja = idCaja;
    }

    public UUID getIdEmpleadoRol() {
        return idEmpleadoRol;
    }

    public void setIdEmpleadoRol(UUID idEmpleadoRol) {
        this.idEmpleadoRol = idEmpleadoRol;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public OffsetDateTime getFechaFacturacion() {
        return fechaFacturacion;
    }

    public void setFechaFacturacion(OffsetDateTime fechaFacturacion) {
        this.fechaFacturacion = fechaFacturacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}