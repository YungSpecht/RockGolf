package com.rock.golf.Bot;

import java.util.LinkedList;
import java.util.Queue;

public class Astar {
    /**
     * We maintain two lists: OPEN and CLOSE
     * OPEN: consists on nodes that have been visited but not expanded (sucessors
     * have not been explored yet)
     * CLOSE: consissts of nodes that have been visited and expanded (sucessors have
     * been expolored already and included in the open list if this was the case)
     */

    // private final Graph<T> graph;
    // private final Scorer<T> node_next;
    // private final Scorer<T> node_goal;
    // public List<T> findRoute(T from, T to) {throw new IllegalStateException("No
    // route found");}
    // Map<T, RouteNode>> allNodes = new HashMap<>();

    /**
     * RouteNode<T> start = new RouteNode<>(from, null, 0d,
     * targetScorer.computeCost(from, to));
     * openSet.add(start);
     * allNodes.put(from, start);
     */

    Queue<Object> OPEN;
    private static LinkedList<Object> CLOSE;
    private final Object node_start;
    private final Object node_goal;

    public Astar(Object node_start, Object node_goal) {
        this.node_start = node_start;
        this.node_goal = node_goal;
        OPEN = new LinkedList<Object>();
        CLOSE = new LinkedList<Object>();
    }

    // from pseudocode:
    public void initialise(Object node_goal, Object previous_node) {
        while (!OPEN.isEmpty()) { // while the open lsit is not empty,
            // take from the open list the node node_current with the lowest
            Object node_current = OPEN.poll();
            if (node_current == node_goal)
                break;

            // Generate each state node_successor that come after node_current
            LinkedList<Object> successors = new LinkedList<>();
            for (Object i : OPEN) {
                successors.add(i);
            }

            while (node_current != null) {
                for (Object node_successor : successors) {
                    // set successor_current_cost = g(node_current) + w(node_current,
                    // node_successor)

                    if (OPEN.contains(node_successor)) {

                    }
                }
            }
        }
    }
}
