/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.modifier.generate.InflateModifier;
import net.qoopo.engine.core.entity.component.modifier.generate.SubdivisionModifier;
import net.qoopo.engine.core.material.Material;

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
        material = new Material("CuboEsfera");
        build();
    }

    public SphereBox(float radio) {
        super(radio);
        this.radio = radio;
        material = new Material("CuboEsfera");
        build();
    }

    public SphereBox(float radio, int divisiones) {
        super(radio);
        this.radio = radio;
        this.divisiones = divisiones;
        material = new Material("CuboEsfera");
        build();
    }

    @Override
    public void build() {
        setAlto(radio);
        setAncho(radio);
        setLargo(radio);
        super.build();
        computeNormals();
        new SubdivisionModifier(SubdivisionModifier.TYPE_SIMPLE, divisiones).apply(this);
        new InflateModifier(radio).apply(this);
        computeNormals();
        smooth();
        applyMaterial(material);
    }

}
