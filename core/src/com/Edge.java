package com;

import com.rock.golf.Pathfinding.Node;

public class Edge {
    // Represent each edge as one of its end-points, and a direction
    Node from;
    Node to;
    String direction;

    public Edge(Node from, Node to, String direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
    }
}
