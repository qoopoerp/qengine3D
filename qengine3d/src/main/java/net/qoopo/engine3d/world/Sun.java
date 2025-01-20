package net.qoopo.engine3d.world;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector3;

public class Sun extends Entity {

    public Sun() {
        this(null);
    }

    public Sun(String name) {
        this(name, 1.5f);
    }

    public Sun(String name, float power) {
        super(name);
        this.addComponent(new QDirectionalLigth(power, QColor.WHITE, 50, Vector3.of(0, -1f, 0), true, true));
    }
}
