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

    public void random_maze() {
        Graph graph = new Graph();
        Node[][] grid = graph.generateMatrix();
        HashMap bucket = new LinkedHashMap()<>();

        // Represent each edge as one of its end-points, and a direction
        LinkedList<Edge> edges = new LinkedList<>();
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (int j2 = 0; j2 < grid.length * 2; j2++) {
                    bucket.put(j2, grid[i][j]);
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
        // Throw all of the edges in the graph into a big set
        while (!edges.isEmpty()) {
            Edge removed = edges.getLast();
            edges.removeLast(); // remove the next edge from the list
            // If cells are not already in the same bucket: Connect them and Merge Buckets
            
            // compute the other endpoint
            // test their two sets
        }

        // Remove an edge from the bag at random. If the edge connects two disjoint
        // trees, join the trees. Otherwise, throw that edge away.
        Random rand = new Random();
        rand.nextInt(10);
        // Repeat until there are no more edges left.
    }
}
