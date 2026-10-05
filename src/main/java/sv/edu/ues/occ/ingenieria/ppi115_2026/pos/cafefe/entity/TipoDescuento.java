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
import java.util.UUID;

@Entity
@Table(name = "tipo_descuento")
public class TipoDescuento {

    @Id
    @Column(name = "id_tipo_descuento")
    private UUID idTipoDescuento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "descuento_maximo")
    private Integer descuentoMaximo;

    @Column(name = "observaciones")
    private String observaciones;

    public TipoDescuento() {
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getDescuentoMaximo() {
        return descuentoMaximo;
    }

    public void setDescuentoMaximo(Integer descuentoMaximo) {
        this.descuentoMaximo = descuentoMaximo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}