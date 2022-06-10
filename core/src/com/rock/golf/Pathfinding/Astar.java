package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Astar {
    /**
     * We maintain two lists: OPEN and CLOSE
     * OPEN: consists on nodes that have been visited but not expanded (sucessors
     * have not been explored yet)
     * CLOSE: consissts of nodes that have been visited and expanded (sucessors have
     * been expolored already and included in the open list if this was the case)
     * 
     * g -  movement cost to move from the starting point to a given square on the grid, following the path generated to get there
     * h -  estimated movememt cost to move from given square on grid to final destination 
     */

    Queue<Node> OPEN = new LinkedList<Node>();
    private static LinkedList<Node> CLOSE;
    private final Node node_start;
    private final Node node_goal;
    Graph graph = new Graph();
    Node[][] nodes;

    public Astar(Node node_start, Node node_goal, Node from) {
        nodes = graph.generateMatrix();
        this.node_start = node_start;
        this.node_goal = node_goal;
        OPEN.add(node_start);
        CLOSE = new LinkedList<Node>();
    }

    public ArrayList<Object> getPath(Object node_goal, Object previous_node) {
        while (!OPEN.isEmpty()) { // while the open list is not empty,
            // take from the open list the node node_current with the lowest value
            Object node_current = OPEN.poll();
            if (node_current.equals(node_goal)) {
                ArrayList<Object> path = new ArrayList<>();
                do {
                    path.add(0, node_current);
                    // node_current = allNodes.get(node_current.getPrevious());
                } while(node_current != null);
                 return path;
            }
            //TODO: get successors
        } 
        throw new IllegalStateException("No path found");
    }
}
