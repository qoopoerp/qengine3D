package net.qoopo.engine.core.entity.component.ia.treebehaviour.composite;

import net.qoopo.engine.core.entity.component.ia.treebehaviour.DefaultNode;
import net.qoopo.engine.core.entity.component.ia.treebehaviour.NodeState;

public class ParallelNode extends DefaultNode {

    @Override
    public NodeState execute() {

        if (childs.isEmpty())
            return NodeState.FAILURE;
        childs.stream().parallel().forEach(c -> c.execute());
        return NodeState.SUCCESS;
    }

}
