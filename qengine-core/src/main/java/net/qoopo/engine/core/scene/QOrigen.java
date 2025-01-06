/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.material.basico.QMaterialBas;
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
        agregarGeometria();
    }

    /**
     * Esta geometria
     */
    private void agregarGeometria() {
        try {

            Mesh geometria = new Mesh();
            geometria.addVertex(0, 0, 0); // 0
            geometria.addVertex(1.0f, 0, 0); // 1
            geometria.addVertex(0, 1.0f, 0); // 2
            geometria.addVertex(0, 0, 1.0f); // 3

            QMaterialBas matX = new QMaterialBas("x");
            matX.setColorBase(QColor.RED);
            matX.setFactorEmision(1.0f);

            QMaterialBas matY = new QMaterialBas("y");
            matY.setColorBase(QColor.GREEN);
            matY.setFactorEmision(1.0f);

            QMaterialBas matZ = new QMaterialBas("z");
            matZ.setColorBase(QColor.BLUE);
            matZ.setFactorEmision(1.0f);

            geometria.addLine(matX, 0, 1);
            geometria.addLine(matY, 0, 2);
            geometria.addLine(matZ, 0, 3);
            addComponent(geometria);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
