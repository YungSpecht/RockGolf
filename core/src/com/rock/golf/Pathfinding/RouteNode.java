package com.rock.golf.Pathfinding;

public class RouteNode<Node> extends Graph {
    final Node current_node;
    private Node previous_node;
    private static double routeScore;

    RouteNode(Node current, Node previous, double routeScore) {
        current_node = current;
        previous_node = previous;
        this.routeScore = routeScore;
    }

    public void setRouteScore(double score) {
        routeScore = score;
    }

    public double getRouteSxore() {
        return routeScore;
    }
}
