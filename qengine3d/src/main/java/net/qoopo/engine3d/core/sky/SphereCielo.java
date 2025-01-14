/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.sky;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.MixTexture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;

/**
 *
 * @author alberto
 */
public class SphereCielo extends QCielo {

    private Texture texturaDia;
    private Texture texturaNoche;
    private float radio;
    private MixTexture procesadorTextura;

    public SphereCielo(Texture texturaDia, Texture texturaNoche, float radio) {
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
        Material material = new Material();
        material.setEmision(1);//finge emision de luz para no ser afectado por las luces
        procesadorTextura = new MixTexture(texturaDia, texturaNoche);
        procesadorTextura.setRazon(0);
        material.setColorMap(procesadorTextura);
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

    public Texture getTexturaDia() {
        return texturaDia;
    }

    public void setTexturaDia(Texture texturaDia) {
        this.texturaDia = texturaDia;
    }

    public Texture getTexturaNoche() {
        return texturaNoche;
    }

    public void setTexturaNoche(Texture texturaNoche) {
        this.texturaNoche = texturaNoche;
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public MixTexture getProcesadorTextura() {
        return procesadorTextura;
    }

    public void setProcesadorTextura(MixTexture procesadorTextura) {
        this.procesadorTextura = procesadorTextura;
    }

}
