package com.rock.golf.Pathfinding;

public class Node implements Comparable {
    public boolean isPath;
    // position in array
    // parent
    public int currentNodeValue;
    public Node parent;
    public int row;
    public int column;
    public double euclidean;

    public Node(int value, Node parent) {
        this.currentNodeValue = value;
        this.parent = parent;
    }

    public Node(int value, Node parent, int row, int column) {
        this.currentNodeValue = value;
        this.parent = parent;
        this.row = row;
        this.column = column;
    }

    public double calculateEuclidean(Node goal){
        euclidean = Math.sqrt(Math.pow(goal.row - this.row, 2) + Math.pow(goal.column - this.column, 2));
        return euclidean;
    }

    @Override
    public int compareTo(Object o) {
        Node node = (Node)o;
        return 0;
    }
}
