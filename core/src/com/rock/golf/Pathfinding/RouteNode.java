package com.rock.golf.Pathfinding;

public class RouteNode<Node> extends Graph {
    private final Node current_node;
    private Node previous_node;
    private int routeScore;
    private int estimatedScore;

    RouteNode(Node current, Node previous, int routeScore, int estimatedScore) {
        current_node = current;
        previous_node = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    public Node getCurrentNode() {
        return current_node;
    }
}
