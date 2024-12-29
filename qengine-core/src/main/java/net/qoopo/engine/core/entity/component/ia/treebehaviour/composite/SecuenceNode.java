package net.qoopo.engine.core.entity.component.ia.treebehaviour.composite;

import net.qoopo.engine.core.entity.component.ia.treebehaviour.DefaultNode;
import net.qoopo.engine.core.entity.component.ia.treebehaviour.NodeBehaviour;
import net.qoopo.engine.core.entity.component.ia.treebehaviour.NodeState;

public class SecuenceNode extends DefaultNode {

    @Override
    public NodeState execute() {

        if (childs.isEmpty())
            return NodeState.FAILURE;
        for (NodeBehaviour child : childs) {
            if (child.execute() != NodeState.SUCCESS) {
                return NodeState.FAILURE;
            }
        }
        return NodeState.SUCCESS;
    }

}
