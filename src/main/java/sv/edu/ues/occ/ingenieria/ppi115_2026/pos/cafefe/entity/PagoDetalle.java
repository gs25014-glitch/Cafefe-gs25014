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
@Table(name = "pago_detalle")
public class PagoDetalle {

    @Id
    @Column(name = "id_pago_detalle")
    private UUID idPagoDetalle;

    @Column(name = "id_pago")
    private UUID idPago;

    @Column(name = "monto", precision = 8, scale = 2)
    private BigDecimal monto;

    @Column(name = "tipo_pago")
    private String tipoPago;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "referencia_externa")
    private String referenciaExterna;

    public PagoDetalle() {
    }

    public UUID getIdPagoDetalle() {
        return idPagoDetalle;
    }

    public void setIdPagoDetalle(UUID idPagoDetalle) {
        this.idPagoDetalle = idPagoDetalle;
    }

    public UUID getIdPago() {
        return idPago;
    }

    public void setIdPago(UUID idPago) {
        this.idPago = idPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }
}