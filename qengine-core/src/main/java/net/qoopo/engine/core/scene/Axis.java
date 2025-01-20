/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;

/**
 * Esta entidad representa un el origen para poder dibujarlo en el escenario
 * componentes
 *
 * @author alberto
 */
public class Axis extends Entity {

    public Axis() {
        super("Origen");
        build();
    }

    private void build() {
        try {

            Material matX = new Material("x");
            matX.setColor(QColor.RED);
            matX.setEmissionIntensity(1.0f);

            Material matY = new Material("y");
            matY.setColor(QColor.GREEN);
            matY.setEmissionIntensity(1.0f);

            Material matZ = new Material("z");
            matZ.setColor(QColor.BLUE);
            matZ.setEmissionIntensity(1.0f);

            Box boxX = new Box(1f, 0.05f, 0.1f);
            boxX.applyMaterial(matX);
            addComponent(boxX);

            Box boxY = new Box(0.05f, 1f, 0.1f);
            boxY.applyMaterial(matY);
            addComponent(boxY);

            Box boxZ = new Box(0.05f, 0.1f, 1f);
            boxZ.applyMaterial(matZ);
            addComponent(boxZ);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
