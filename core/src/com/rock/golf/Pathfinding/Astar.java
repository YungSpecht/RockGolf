package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Astar {
    /**
     * We maintain two lists: OPEN and CLOSE
     * OPEN: consists on nodes that have been visited but not expanded (sucessors
     * have not been explored yet)
     * CLOSE: consissts of nodes that have been visited and expanded (sucessors have
     * been explored already and included in the open list if this was the case)
     * 
     * g - movement cost to move from the starting point to a given square on the
     * grid, following the path generated to get there
     * Is added to h to get f
     * h - heuristic, estimated movememt cost to move from given square on grid to
     * final
     * destination
     * f - estimate from the node to the goal
     */

    Node from;
    Node to;

    public Astar(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public ArrayList<Node> getPath() {
        Graph graph = new Graph();
        Queue<RouteNode<Node>> OPEN = new PriorityQueue<>();
        Map<Node, RouteNode<Node>> CLOSE = new HashMap<>();
        RouteNode<Node> node_start = new RouteNode<Node>(from, null, 0);
        OPEN.add(node_start);
        CLOSE.put(from, node_start);
        ArrayList<Node> path = new ArrayList<>();

        while (!OPEN.isEmpty()) {
            // find the node with the least f on the open list, call it the current node
            RouteNode<Node> node_current = OPEN.poll();
            // generate the node's successors and set their parents to current
            from = node_current.current_node;

            if (from.equals(to)) {
                RouteNode<Node> current = node_current;
                do {
                    current.current_node.isPath = true;
                    path.add(current.current_node);
                    current = CLOSE.get(current);
                } while (current != null);
                return path;
            }

            for (int i = 0; i < graph.neighbors(from).size(); i++) { // check for obstacles is in Graph.java
                RouteNode<Node> node_successor = new RouteNode<Node>(graph.neighbors(from).get(i), from,
                        node_current.getRouteSxore() + graph.neighbors(from).get(i).currentNodeValue);
                if (OPEN.contains(node_successor)) {
                    if (node_successor.current_node.currentNodeValue <= node_successor.getRouteSxore())
                        continue;
                } else if (CLOSE.containsValue(node_successor)) {
                    if (node_successor.current_node.currentNodeValue <= node_successor.getRouteSxore())
                        continue;
                    OPEN.add(node_successor);
                    CLOSE.remove(from, node_successor);
                } else {
                    OPEN.add(node_successor);
                    node_successor.setRouteScore(heuristicDistance(from.column, node_successor.current_node.column,
                            from.row, node_successor.current_node.row));
                }
                node_successor.current_node.currentNodeValue = node_successor.getRouteSxore(); // Not sure if this works
                node_successor.current_node.parent = from;
            }
            CLOSE.put(from, node_current);
        }
        if (from != to) {
            from.isPath = false;
            throw new IllegalStateException("No path found");
        }
        return path;
    }

    public double heuristicDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}
