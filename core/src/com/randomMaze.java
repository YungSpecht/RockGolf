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

    public randomMaze() {

    }

    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    LinkedList<Edge> edges;
    Node[][] grid;

    public Node[][] random_maze() {
        Graph graph = new Graph();
        grid = graph.generateMatrix();
        edges = new LinkedList<>(); // Throw all of the edges in the graph into a big set

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
        Edge removed = edges.getLast();
        edges.removeLast(); // remove the next edge from the list
        recursive(removed);
        return grid;
    }

    public void recursive(Edge removed) {
        if (connected(removed.from, removed.to)) {
            return;
        }
        if (removed.from.ID != removed.to.ID) {
            removed.to.ID = removed.from.ID; // set their ID's as the same
            connect(removed.from, removed.to); // connect the sets
        }
        removed = edges.getLast();
        edges.removeLast(); // remove the next edge from the list
        recursive(removed);
    }

    public void connect(Node from, Node to) {
        for (int index = 0; index < from.children.size(); index++) {
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
}
