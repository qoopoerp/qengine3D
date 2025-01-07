/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.math.QColor;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public class AbstractMaterial implements Serializable {

    protected String nombre = "Material_";

    // Color base del material
    protected QColor color = QColor.WHITE.clone();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
