package net.qoopo.engine.core.entity.component.modifier;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;

public interface ModifierComponent extends EntityComponent {

    /** Aplica el modicador a la malla */
    public void apply(Mesh mesh);

    public void changed();

    public boolean isChanged();

}
