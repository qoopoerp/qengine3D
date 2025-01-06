package net.qoopo.engine.core.entity.component.modifier;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;

public interface ModifierComponent extends EntityComponent {

    public void apply(Mesh mesh);

}
