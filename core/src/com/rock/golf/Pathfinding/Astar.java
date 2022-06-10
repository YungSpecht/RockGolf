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

    public ArrayList<Node> getPath(Node from, Node to) {
        Graph graph = new Graph();
        Queue<RouteNode<Node>> OPEN = new PriorityQueue<>();
        Map<Node, RouteNode<Node>> CLOSE = new HashMap<>();
        RouteNode<Node> node_start = new RouteNode<Node>(from, null, 0, 0);
        OPEN.add(node_start);
        CLOSE.put(from, node_start);

        while (!OPEN.isEmpty()) { // while the open list is not empty,
            // find the node with the least f on the open list, call it the current node
            RouteNode<Node> node_current = OPEN.poll();
            // generate the node's successors and set their parents to current

            if (node_current.getCurrentNode().equals(to)) {
                ArrayList<Node> path = new ArrayList<>();
                RouteNode<Node> current = node_current; // might not be necessary
                
                do {
                    path.add(0, node_current.getCurrentNode());
                    // right left down up
                    current = CLOSE.get(current.getCurrentNode());
                } while (node_current != null);
                return path;
            }
        }
        throw new IllegalStateException("No path found");
    }
}
