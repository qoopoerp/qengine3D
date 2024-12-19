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
import net.qoopo.engine.core.entity.component.mesh.primitive.QShape;
import net.qoopo.engine.core.entity.component.mesh.primitive.QVertex;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 *
 * @author alberto
 */
public class QTeapot extends QShape {

    public QTeapot() {
        material = new QMaterialBas("QTeapot");
        nombre = "QTeapot";
        build();
    }

    @Override
    public void build() {
        deleteData();
        try {
            ModelLoader loadModel = new LoadModelObj();
            Entity ent = loadModel.loadModel(QTeapot.class.getResourceAsStream("/models/teapot.obj"));
            Mesh teapot = QUtilComponentes.getMesh(ent);
            for (QVertex vertice : teapot.vertices) {
                this.addVertex(vertice);
            }
            for (QPrimitiva primitiva : teapot.primitivas) {
                this.addPoly(primitiva.listaVertices);
            }
        } catch (Exception ex) {
            Logger.getLogger(QTeapot.class.getName()).log(Level.SEVERE, null, ex);
        }
        QUtilNormales.calcularNormales(this);
        QMaterialUtil.suavizar(this, true);
        QMaterialUtil.aplicarMaterial(this, material);
    }

}
