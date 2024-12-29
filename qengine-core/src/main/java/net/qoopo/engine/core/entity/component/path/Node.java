package net.qoopo.engine.core.entity.component.path;

public class Node implements Comparable<Node> {
    int x, y;
    double gCost, hCost, fCost;
    Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getFCost() {
        return gCost + hCost;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.getFCost(), other.getFCost());
    }
}
