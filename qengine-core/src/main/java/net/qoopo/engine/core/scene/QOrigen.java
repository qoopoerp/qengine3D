/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;

/**
 * Esta entidad representa un el origen para poder dibujarlo en el escenario
 * componentes
 *
 * @author alberto
 */
public class QOrigen extends Entity {

    public QOrigen() {
        super("Origen");
        build();
    }

    /**
     * Esta geometria
     */
    private void build() {
        try {

            Material matX = new Material("x");
            matX.setColor(QColor.RED);
            matX.setFactorEmision(1.0f);

            Material matY = new Material("y");
            matY.setColor(QColor.GREEN);
            matY.setFactorEmision(1.0f);

            Material matZ = new Material("z");
            matZ.setColor(QColor.BLUE);
            matZ.setFactorEmision(1.0f);

            // Mesh mesh = new Mesh();
            // mesh.addVertex(0, 0, 0); // 0
            // mesh.addVertex(1.0f, 0, 0); // 1
            // mesh.addVertex(0, 1.0f, 0); // 2
            // mesh.addVertex(0, 0, 1.0f); // 3

            // mesh.addNormal(0, 0, 0);
            // mesh.addNormal(0, 0, 0);
            // mesh.addNormal(0, 0, 0);
            // mesh.addNormal(0, 0, 0);

            // mesh.addUV(0, 0);
            // mesh.addUV(0, 0);
            // mesh.addUV(0, 0);
            // mesh.addUV(0, 0);

            // mesh.addLine(matX, 0, 1);
            // mesh.addLine(matY, 0, 2);
            // mesh.addLine(matZ, 0, 3);
            // addComponent(mesh);

            Box boxX = new Box(1f, 0.1f, 0.1f);
            boxX.applyMaterial(matX);
            addComponent(boxX);

            Box boxY = new Box(0.1f, 1f, 0.1f);
            boxY.applyMaterial(matY);
            addComponent(boxY);

            Box boxZ = new Box(0.1f, 0.1f, 1f);
            boxZ.applyMaterial(matZ);
            addComponent(boxZ);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
