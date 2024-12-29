package net.qoopo.engine.core.entity.component.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Pathfinding {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private int[][] grid; // Mapa (0 = libre, 1 = obstáculo)
    private int rows, cols;

    public Pathfinding(int[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
    }

    public List<Node> findPath(Node start, Node goal) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        HashSet<Node> closedList = new HashSet<>();
        start.gCost = 0;
        start.hCost = heuristic(start, goal);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.x == goal.x && current.y == goal.y) {
                return reconstructPath(current);
            }

            closedList.add(current);

            for (int[] direction : DIRECTIONS) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];

                if (isInBounds(newX, newY) && grid[newX][newY] == 0) {
                    Node neighbor = new Node(newX, newY);

                    if (closedList.contains(neighbor)) continue;

                    double tentativeGCost = current.gCost + 1; // Suponiendo coste uniforme
                    if (tentativeGCost < neighbor.gCost || !openList.contains(neighbor)) {
                        neighbor.gCost = tentativeGCost;
                        neighbor.hCost = heuristic(neighbor, goal);
                        neighbor.parent = current;

                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }

        return Collections.emptyList(); // Sin camino encontrado
    }

    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private double heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Distancia Manhattan
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < rows && y < cols;
    }

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };

        Pathfinding pathfinding = new Pathfinding(grid);
        Node start = new Node(0, 0);
        Node goal = new Node(4, 4);

        List<Node> path = pathfinding.findPath(start, goal);

        for (Node node : path) {
            System.out.println("(" + node.x + ", " + node.y + ")");
        }
    }
}