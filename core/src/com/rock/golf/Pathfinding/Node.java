package com.rock.golf.Pathfinding;

import java.util.ArrayList;

public class Node implements Comparable {
    public boolean isPath;
    // position in array
    // parent
    public double currentNodeValue;
    public Node parent;
    public int row;
    public int column;
    public double euclidean;
    public int ID;
    public ArrayList<Node> children;

    public Node(int value, Node parent) {
        this.currentNodeValue = value;
        this.parent = parent;
        children = new ArrayList<>();
    }

    public Node(int value, Node parent, int row, int column) {
        this.currentNodeValue = value;
        this.parent = parent;
        this.row = row;
        this.column = column;
        children = new ArrayList<>();
    }

    public double calculateEuclidean(Node goal) {
        euclidean = Math.sqrt(Math.pow(goal.row - this.row, 2) + Math.pow(goal.column - this.column, 2));
        return euclidean;
    }

    @Override
    public int compareTo(Object o) {
        Node node = (Node) o;
        if(this.euclidean>node.euclidean) return 1;
        else if (this.euclidean==node.euclidean) return 0;
        else return -1;
    }

    public void birth(Node child) {
        children.add(child);
    }
}
