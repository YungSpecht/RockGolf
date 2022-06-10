package com.rock.golf.Pathfinding;

public class Node {
    public boolean isPath;
    // position in array
    // parent
    public int currentNodeValue;
    public Node parent;
    public int row;
    public int column;

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

}
