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
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.util.QUtilComponentes;

/**
 *
 * @author alberto
 */
public class Teapot extends Shape {

    public Teapot() {
        material = new QMaterialBas("Teapot");
        nombre = "Teapot";
        build();
    }

    @Override
    public void build() {
        deleteData();
        try {
            ModelLoader loadModel = new LoadModelObj();
            Entity ent = loadModel.loadModel(Teapot.class.getResourceAsStream("/models/teapot.obj"));
            Mesh teapot = QUtilComponentes.getMesh(ent);
            for (Vertex vertice : teapot.vertices) {
                this.addVertex(vertice);
            }
            for (QPrimitiva primitiva : teapot.primitivas) {
                this.addPoly(primitiva.listaVertices);
            }
        } catch (Exception ex) {
            Logger.getLogger(Teapot.class.getName()).log(Level.SEVERE, null, ex);
        }
        calculateNormals();
        smooth();
        applyMaterial(material);
    }

}
