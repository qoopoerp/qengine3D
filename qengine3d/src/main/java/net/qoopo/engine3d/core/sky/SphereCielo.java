/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.sky;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorMix;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;

/**
 *
 * @author alberto
 */
public class SphereCielo extends QCielo {

    private QTextura texturaDia;
    private QTextura texturaNoche;
    private float radio;
    private QProcesadorMix procesadorTextura;

    public SphereCielo(QTextura texturaDia, QTextura texturaNoche, float radio) {
        this.texturaDia = texturaDia;
        this.texturaNoche = texturaNoche;
        this.radio = radio;
        construir();
    }

    private void construir() {
        entity = new Entity("Cielo");
        Mesh cieloG = new Sphere(radio, 32);
//        QGeometria cieloG = new Sphere(radio, 8);
//        QGeometria cieloG = new Sphere(radio, 4);
        QMaterialBas material = new QMaterialBas();
        material.setFactorEmision(1);//finge emision de luz para no ser afectado por las luces
        procesadorTextura = new QProcesadorMix(texturaDia, texturaNoche);
        procesadorTextura.setRazon(0);
        material.setMapaColor(procesadorTextura);
        MaterialUtil.applyMaterial(cieloG, material);
        NormalUtil.invertirNormales(cieloG);
        entity.addComponent(cieloG);
        System.out.println("entity del cielo construida");
    }

    @Override
    public void setRazon(float razon) {
        procesadorTextura.setRazon(razon);
        procesadorTextura.procesar();
    }

    /**
     * actualiza el color del cielo en funcion de la hora
     */
    private void atualizarCielo() {

    }

    public QTextura getTexturaDia() {
        return texturaDia;
    }

    public void setTexturaDia(QTextura texturaDia) {
        this.texturaDia = texturaDia;
    }

    public QTextura getTexturaNoche() {
        return texturaNoche;
    }

    public void setTexturaNoche(QTextura texturaNoche) {
        this.texturaNoche = texturaNoche;
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public QProcesadorMix getProcesadorTextura() {
        return procesadorTextura;
    }

    public void setProcesadorTextura(QProcesadorMix procesadorTextura) {
        this.procesadorTextura = procesadorTextura;
    }

}
