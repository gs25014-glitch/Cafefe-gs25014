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
@Table(name = "producto_caracteristica")
public class ProductoCaracteristica {

    @Id
    @Column(name = "id_producto_caracteristica")
    private UUID idProductoCaracteristica;

    @Column(name = "id_producto")
    private UUID idProducto;

    @Column(name = "id_caracteristica")
    private UUID idCaracteristica;

    @Column(name = "valor")
    private String valor;

    public ProductoCaracteristica() {
    }

    public UUID getIdProductoCaracteristica() {
        return idProductoCaracteristica;
    }

    public void setIdProductoCaracteristica(UUID idProductoCaracteristica) {
        this.idProductoCaracteristica = idProductoCaracteristica;
    }

    public UUID getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(UUID idProducto) {
        this.idProducto = idProducto;
    }

    public UUID getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(UUID idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}