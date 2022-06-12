package com.rock.golf.Pathfinding;

public class Node implements Comparable{
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

    public double calculateEuclidean( Node goal){
        this.euclidean = Math.sqrt(Math.pow(this.row-goal.row,2)+Math.pow(this.column-goal.column,2));
        return euclidean;
    }
   
    /**
     * returns -1,0 or 1 if
     * this object is less, equal or greater than the specified one
     */
    @Override
    public int compareTo(Object o) {
       if(!o.getClass().equals(this.getClass())){
        throw new IllegalArgumentException("Illegal parameter");
       }else {
        Node node = (Node)o;
        if(node.euclidean>this.euclidean){
            return -1;
        }else if(node.euclidean==this.euclidean){
            return 0;
        }else{
            return 1;
        }
       }
    }

}
