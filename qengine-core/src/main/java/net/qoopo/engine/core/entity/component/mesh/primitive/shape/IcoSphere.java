/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.primitive.QShape;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 * Geoesfera o Icoesfera
 *
 * @author alberto
 */
public class IcoSphere extends QShape {

    private float radio;
    private int divisiones = 3;

    public IcoSphere() {
        nombre = "NicoEsfera";
        material = new QMaterialBas("NicoEsfera");
        radio = 1;
        build();
    }

    public IcoSphere(float radio) {
        nombre = "NicoEsfera";
        material = new QMaterialBas("NicoEsfera");
        this.radio = radio;
        build();
    }

    public IcoSphere(float radio, int divisiones) {
        nombre = "NicoEsfera";
        material = new QMaterialBas("NicoEsfera");
        this.radio = radio;
        this.divisiones = divisiones;
        build();
    }

    /**
     * Construye una esfera http://www.songho.ca/opengl/gl_sphere.html
     */
    @Override
    public void build() {
        deleteData();
        // paso 1.- generar el icosaedro origen
        crearOriginal();
        // ahora armamos las caras
        QUtilNormales.calcularNormales(this);

        // paso 2
        // for (int i = 0; i < divisiones; i++) {
        // dividir();
        // inflar(radio);
        // }
        dividir(divisiones);
        inflar(radio);

        // el objeto es suavizado
        QMaterialUtil.suavizar(this, true);
        // QMaterialUtil.aplicarMaterial(this, material);
    }

    /**
     * Crea la forma original
     */
    private void crearOriginal() {
        try {
            QMaterialBas blanco = new QMaterialBas("blanco");
            blanco.setColorBase(QColor.WHITE);
            QMaterialBas rojo = new QMaterialBas("rojo");
            rojo.setColorBase(QColor.RED);
            QMaterialBas azul = new QMaterialBas("azul");
            azul.setColorBase(QColor.BLUE);
            QMaterialBas amarillo = new QMaterialBas("amarillo");
            amarillo.setColorBase(QColor.YELLOW);

            addVertex(radio / 2, radio / 2, radio / 2);// 0
            addVertex(-radio / 2, -radio / 2, radio / 2);// 1
            addVertex(-radio / 2, radio / 2, -radio / 2);// 2
            addVertex(radio / 2, -radio / 2, -radio / 2);// 3
            addVertex(-radio / 2, radio / 2, -radio / 2);// 4

            addPoly(blanco, 1, 0, 2);
            addPoly(rojo, 0, 1, 3);
            addPoly(amarillo, 3, 4, 0);
            addPoly(azul, 2, 3, 1);
        } catch (Exception ex) {
            Logger.getLogger(IcoSphere.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public int getDivisiones() {
        return divisiones;
    }

    public void setDivisiones(int divisiones) {
        this.divisiones = divisiones;
    }

}
