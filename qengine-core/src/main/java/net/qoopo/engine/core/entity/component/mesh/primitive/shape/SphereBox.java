/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.util.mesh.NormalUtil;

/**
 * Crea una esfera a partir de un cubo
 *
 * @author alberto
 */
@Getter
@Setter
public class SphereBox extends Box {

    private float radio = 1.0f;
    private int divisiones = 3;

    public SphereBox() {
        super(0.5f);
        material = new QMaterialBas("CuboEsfera");
        build();
    }

    public SphereBox(float radio) {
        super(radio);
        this.radio = radio;
        material = new QMaterialBas("CuboEsfera");
        build();
    }

    public SphereBox(float radio, int divisiones) {
        super(radio);
        this.radio = radio;
        this.divisiones = divisiones;
        material = new QMaterialBas("CuboEsfera");
        build();
    }

    @Override
    public void build() {
        try {
            setAlto(radio);
            setAncho(radio);
            setLargo(radio);
            super.build();
            NormalUtil.calcularNormales(this);
            dividir(divisiones);
            inflate(radio);
            // el objeto es suavizado
            smooth();
            applyMaterial(material);
        } catch (Exception ex) {
            Logger.getLogger(SphereBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
