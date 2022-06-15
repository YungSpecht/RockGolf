package com;

import java.util.*;
import com.rock.golf.RockGolf;
import com.rock.golf.Pathfinding.Graph;
import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.rectangleObstacle;

public class randomMaze {

    /**
     * To randomly create a maze, we're using Kruskal's algorithm,
     * which produces a minimal spanning tree from a weighted graph
     * Pseudocode from:
     * https://weblog.jamisbuck.org/2011/1/3/maze-generation-kruskal-s-algorithm
     */

    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    LinkedList<Edge> edges;
    Node[][] grid;
    Graph graph;

    public randomMaze(Graph graph) {
        this.graph = graph;
        grid = graph.generateMatrix();
        edges = new LinkedList<>(); // Throw all of the edges in the graph into a big set
    }

    public Node[][] random_maze() {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                // grid[i][j].ID = count;
                count++;
                if (i > 0) {
                    Edge h = new Edge(grid[i - 1][j], grid[i][j], "horizontal");
                    edges.add(h);
                }
                if (j > 0) {
                    Edge v = new Edge(grid[i][j - 1], grid[i][j], "vertical");
                    edges.add(v);
                }
            }
        }

        Collections.shuffle(edges);

        while (!edges.isEmpty() && edges.getLast().from.children.size() != count) {
            Edge removed = edges.getLast();
            edges.removeLast(); // remove the next edge from the list
            if (!connected(removed.from, removed.to)) { // && removed.from.ID != removed.to.ID
                // removed.to.ID = removed.from.ID; // set their ID's as the same
                connect(removed.from, removed.to); // connect the sets
            }
        }

        graph.rectangles = addWalls();
        return grid;
    }

    public void connect(Node from, Node to) {
        for (int index = 0; index < from.children.size(); index++) {
            if (!to.children.contains(from.children.get(index)))
                to.children.add(from.children.get(index));
        }
        for (int i = 0; i < to.children.size(); i++) {
            if (!from.children.contains(to.children.get(i)))
                from.children.add(to.children.get(i));
        }
    }

    public boolean connected(Node from, Node to) {
        if (from.children.contains(to) && to.children.contains(from))
            return true;
        return false;
    }

    public List<rectangleObstacle> addWalls() {
        for (int i = 0; i < edges.size(); i++) {
            PhysicsEngine.rectangles.add(edges.get(i).wall);
        }
        return PhysicsEngine.rectangles;
    }
}
