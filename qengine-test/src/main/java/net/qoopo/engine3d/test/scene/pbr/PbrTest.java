/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.pbr;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 * esferas con diferentes valores de rugosidad
 * y metalico
 * 
 * @author alberto
 */
public class PbrTest extends MakeTestScene {

    public PbrTest() {

    }

    public void make(Scene scene) {
        this.scene = scene;

        int nrRows = 7;
        int nrColumns = 7;
        float spacing = 2.5f;

        Mesh mesh = new Sphere();

        for (int row = 0; row < nrRows; ++row) {
            for (int col = 0; col < nrColumns; ++col) {
                float roughness = (float) QMath.round((float) col / (float) nrColumns, 2);
                float metalness = (float) QMath.round((float) row / (float) nrRows, 2);
                Material material = new Material("PBR-" + roughness + " " + metalness);
                material.setColor(QColor.DEFAULT);
                material.setRoughness(roughness);
                material.setMetalness(metalness);
                Entity objeto = new Entity("PBR-" + roughness + " " + metalness);
                objeto.move((col - (nrColumns / 2)) * spacing, (row - (nrRows / 2)) * spacing, 0);
                objeto.addComponent(mesh.clone().applyMaterial(material));
                scene.addEntity(objeto);
            }
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
