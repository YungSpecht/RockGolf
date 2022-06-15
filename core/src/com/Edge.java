package com;

import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Physics.Engine.rectangleObstacle;

public class Edge {
    // Represent each edge as one of its end-points, and a direction
    Node from;
    Node to;
    String direction;
    double height;
    double width;

    public Edge(Node from, Node to, String direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        float row = from.row;
        float col = from.column;
        float[] position = {row, col};
    
        if(direction.equals("horizontal")) {
             height = 1;
             width = 0.5;
        } else {
             height = 0.5;
             width = 1;
        }
        rectangleObstacle wall = new rectangleObstacle(position, width, height);
    }
}
