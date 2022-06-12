package com.rock.golf.Pathfinding;

public class RouteNode<Node> extends Graph {
    final Node current_node;
    private Node previous_node;
    private static int routeScore;

    RouteNode(Node current, Node previous, int routeScore) {
        current_node = current;
        previous_node = previous;
        this.routeScore = routeScore;
    }

    public void setRouteScore(int score) {
        routeScore = score;
    }

    public int getRouteSxore() {
        return routeScore;
    }
}
