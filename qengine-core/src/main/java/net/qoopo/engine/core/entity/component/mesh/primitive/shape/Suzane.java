/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.ComponentUtil;

/**
 *
 * @author alberto
 */
public class Suzane extends Shape {

    public Suzane() {
        material = new QMaterialBas("Suzane");
        nombre = "Suzane";
        build();
    }

    @Override
    public void build() {
        deleteData();
        try {
            ModelLoader loadModel = new LoadModelObj();
            Entity ent = loadModel.loadModel(Suzane.class.getResourceAsStream("/models/suzane.obj"));
            Mesh mesh = ComponentUtil.getMesh(ent);
            // ent.addComponent(mesh);
            for (Vertex vertice : mesh.vertexList) {
                this.addVertex(vertice);
            }
            for (QVector3 normal : mesh.normalList) {
                this.addNormal(normal);
            }
            for (QVector2 uv : mesh.uvList) {
                this.addUV(uv);
            }
            for (Primitive primitiva : mesh.primitiveList) {
                this.addPoly(primitiva.vertexIndexList, primitiva.normalIndexList, primitiva.uvIndexList);
            }
        } catch (Exception ex) {
            Logger.getLogger(Suzane.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        // computeNormals();
        smooth();
        applyMaterial(material);
    }

}
