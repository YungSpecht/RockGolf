package com;

import com.rock.golf.Pathfinding.Node;

public class Edge {
    Node from;
    Node to;
    String direction;

    public Edge(Node from, Node to, String direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
    }
}
