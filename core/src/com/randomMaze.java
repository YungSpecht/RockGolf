package com;

import java.util.*;
import com.rock.golf.RockGolf;
import com.rock.golf.Pathfinding.Graph;
import com.rock.golf.Pathfinding.Node;

public class randomMaze {

    /**
     * To randomly create a maze, we're using Kruskal's algorithm,
     * which produces a minimal spanning tree from a weighted graph
     * Pseudocode from:
     * https://weblog.jamisbuck.org/2011/1/3/maze-generation-kruskal-s-algorithm
     */

    float metertoPixelRatio = RockGolf.metertoPixelRatio;
    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    HashMap<Integer, Node> bucket;

    public Node[][] random_maze() {
        Graph graph = new Graph();
        Node[][] grid = graph.generateMatrix();
        bucket = new HashMap<Integer, Node>();
        LinkedList<Edge> edges = new LinkedList<>(); // Throw all of the edges in the graph into a big set

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (int j2 = 0; j2 < grid.length * grid[0].length; j2++) {
                    grid[i][j].ID = j2;
                }
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

        while (!edges.isEmpty()) {
            Edge removed = edges.getLast();
            edges.removeLast(); // remove the next edge from the list
            // If cells don't already have the same ID: give them the same ID
            if (removed.from.ID != removed.to.ID) {
                removed.to.ID = removed.from.ID;
                connect(removed.from, removed.to);
            }
        } // Repeat until there are no more edges left.
        return grid;
    }

    public void connect(Node from, Node to) {
        // HashMap subset = new HashMap<>();
        bucket.put(from.ID, from);
        bucket.put(from.ID, to);
    }
}
