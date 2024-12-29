package net.qoopo.engine.core.entity.component.ia.treebehaviour.decorator;

import lombok.AllArgsConstructor;
import net.qoopo.engine.core.entity.component.ia.treebehaviour.NodeBehaviour;
import net.qoopo.engine.core.entity.component.ia.treebehaviour.NodeState;

@AllArgsConstructor
// otros wait, debu, invert
public class LoopNode implements NodeBehaviour {

    private NodeBehaviour node;

    @Override
    public NodeState execute() {
        while (node.execute() != NodeState.SUCCESS) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return NodeState.SUCCESS;

    }

}
