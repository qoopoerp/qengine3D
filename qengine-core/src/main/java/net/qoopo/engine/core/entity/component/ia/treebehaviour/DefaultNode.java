package net.qoopo.engine.core.entity.component.ia.treebehaviour;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DefaultNode implements NodeBehaviour {

    protected List<NodeBehaviour> childs = new ArrayList<>();
    protected NodeBehaviour parent;

    @Override
    public abstract NodeState execute();

    public void addChild(NodeBehaviour child) {
        childs.add(child);
    }

    public void removeChild(NodeBehaviour child) {
        childs.remove(child);
    }
}
