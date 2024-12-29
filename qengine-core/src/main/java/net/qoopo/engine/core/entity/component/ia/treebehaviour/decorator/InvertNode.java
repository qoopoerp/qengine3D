package net.qoopo.engine.core.entity.component.ia.treebehaviour.decorator;

import lombok.AllArgsConstructor;
import net.qoopo.engine.core.entity.component.ia.treebehaviour.NodeBehaviour;
import net.qoopo.engine.core.entity.component.ia.treebehaviour.NodeState;

@AllArgsConstructor
public class InvertNode implements NodeBehaviour {

    private NodeBehaviour node;

    @Override
    public NodeState execute() {
        NodeState state = node.execute();
        if (state == NodeState.FAILURE)
            return NodeState.SUCCESS;
        if (state == NodeState.SUCCESS)
            return NodeState.FAILURE;
        return state;
    }

}
