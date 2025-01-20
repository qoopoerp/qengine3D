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
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.util.ComponentUtil;

/**
 *
 * @author alberto
 */
public class Teapot extends Shape {

    public Teapot() {
        material = new Material("Teapot");
        name = "Teapot";
        build();
    }

    @Override
    public void build() {
        deleteData();
        try {
            ModelLoader loadModel = new LoadModelObj();
            Entity ent = loadModel.loadModel(Teapot.class.getResourceAsStream("/models/teapot.obj"));
            Mesh mesh = ComponentUtil.getMesh(ent);
            for (Vertex vertice : mesh.vertexList) {
                this.addVertex(vertice);
            }
            for (Vector3 normal : mesh.normalList) {
                this.addNormal(normal);
            }
            for (Vector2 uv : mesh.uvList) {
                this.addUV(uv);
            }
            for (Primitive primitiva : mesh.primitiveList) {
                this.addPoly(primitiva.vertexIndexList, primitiva.normalIndexList, primitiva.uvIndexList);
            }
        } catch (Exception ex) {
            Logger.getLogger(Teapot.class.getName()).log(Level.SEVERE, null, ex);
        }
        computeNormals();
        smooth();
        applyMaterial(material);
    }

}
