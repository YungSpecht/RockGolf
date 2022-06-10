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
     * been expolored already and included in the open list if this was the case)
     * 
     * g - movement cost to move from the starting point to a given square on the
     * grid, following the path generated to get there
     * h - estimated movememt cost to move from given square on grid to final
     * destination
     */

    public void setPath(Node current, Node to) {
        Graph graph = new Graph();
        Queue<RouteNode<Node>> OPEN = new PriorityQueue<>();
        Map<Node, RouteNode<Node>> CLOSE = new HashMap<>();
        RouteNode<Node> node_start = new RouteNode<Node>(current, null, 0);
        OPEN.add(node_start);
        CLOSE.put(current, node_start);

        while (!OPEN.isEmpty()) {
            // find the node with the least f on the open list, call it the current node
            RouteNode<Node> node_current = OPEN.poll();
            // generate the node's successors and set their parents to current
            current = node_current.current_node;

            if (current.equals(to))
                break;

            //ArrayList<Node> path = new ArrayList<>();

            for (int i = 0; i < graph.neighbors(current).size(); i++) { // check for obstacles is in Graph.java
                RouteNode<Node> node_successor = new RouteNode<Node>(graph.neighbors(current).get(i), current,
                        node_current.routeScore + graph.neighbors(current).get(i).currentNodeValue);
                if (OPEN.contains(node_successor)) {
                    if (node_successor.current_node.currentNodeValue <= node_successor.routeScore)
                        continue;
                } else if (CLOSE.containsValue(node_successor)) {
                    if (node_successor.current_node.currentNodeValue <= node_successor.routeScore)
                        continue;
                    OPEN.add(node_successor);
                    CLOSE.remove(current, node_successor);
                } else {
                    OPEN.add(node_successor);
                    node_successor.setRouteScore(heuristicDistance(current, node_successor.current_node));

                    // You don't use the G score to calculate the heuristic, the G score is added to
                    // the heuristic (H score) to get an estimate from the node to the goal (F
                    // score).
                }
                node_successor.current_node.currentNodeValue = node_successor.routeScore; // Not sure if this works
                node_successor.current_node.parent = current;
            }
            CLOSE.put(current, node_current);
        }
        if (current != to)
            throw new IllegalStateException("No path found");
    }

    public int heuristicDistance(Node from, Node to) {
        // TODO
        return 1;
    }
}
