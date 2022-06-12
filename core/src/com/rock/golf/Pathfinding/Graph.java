package com.rock.golf.Pathfinding;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.mariuszgromada.math.mxparser.Function;
import com.rock.golf.RockGolf;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Physics.Engine.Derivation;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;

public class Graph implements Comparator {
    float sizeX = RockGolf.width;
    float sizeY = RockGolf.height;
    float originX = sizeX / 2;
    float originY = sizeY / 2;
    float metertoPixelRatio = RockGolf.metertoPixelRatio;
    int counter = 0;
    List<Tree> obstacles = RockGolf.trees;
    Node[][] adjacencyMatrix;

    public Node[][] generateMatrix() {
        int counterI = 0;
        int counterJ = 0;
        int pixels = 10;
        int rows = (int) sizeX / pixels;
        int columns = (int) sizeY / pixels;
        Function profile = InputModule.getProfile();
        
        adjacencyMatrix = new Node[rows + 1][columns + 1];

        for (int i = 0; i <= sizeX; i += pixels) {
            for (int j = 0; j <= sizeY; j += pixels) {
                float x = (i - originX) / metertoPixelRatio;
                float y = (j - originY) / metertoPixelRatio;

                float n = (float) Derivation.compute(x, y, profile);

                if (n < 0) {
                    adjacencyMatrix[counterI][counterJ] = new Node(0, null, counterI, counterJ);
                } else if(thereisObastacle(x,y)) {
                    adjacencyMatrix[counterI][counterJ] = new Node(0, null, counterI, counterJ);
                } else {
                    adjacencyMatrix[counterI][counterJ] = new Node(1, null, counterI, counterJ);
                }
                counterJ++;
            }
            counterJ = 0;
            counterI++;
        }

        return adjacencyMatrix;
    }

    private boolean thereisObastacle(float xPos, float yPos) {
        
        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).collidedWithTree(xPos, yPos, 0.05)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Node> neighbors(Node currentNode) {
        int row = currentNode.row;
        int column = currentNode.column;
        ArrayList<Node> neighbors = new ArrayList<>();

        if(column + 1 < adjacencyMatrix[row].length && adjacencyMatrix[row][column + 1].currentNodeValue == 1) {
            if (adjacencyMatrix[row][column + 1].parent == null)
                adjacencyMatrix[row][column + 1].parent = currentNode;
            neighbors.add(adjacencyMatrix[row][column + 1]);
        }

        if(column != 0 && adjacencyMatrix[row][column - 1].currentNodeValue == 1) {
            if (adjacencyMatrix[row][column - 1].parent == null)
                adjacencyMatrix[row][column - 1].parent = currentNode;
            neighbors.add(adjacencyMatrix[row][column - 1]);
        }

        if(row + 1 < adjacencyMatrix.length && adjacencyMatrix[row + 1][column].currentNodeValue == 1) {
            if (adjacencyMatrix[row + 1][column].parent == null)
                adjacencyMatrix[row + 1][column].parent = currentNode;
            neighbors.add(adjacencyMatrix[row + 1][column]);
        }

        if(row != 0 &&  adjacencyMatrix[row - 1][column].currentNodeValue == 1) {
            if (adjacencyMatrix[row - 1][column].parent == null)
                adjacencyMatrix[row - 1][column].parent = currentNode;
            neighbors.add(adjacencyMatrix[row - 1][column]);
        }

        return neighbors;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if(!o1.getClass().equals("com.rock.golf.Pathfinding.Node")){
            throw new IllegalArgumentException("It is not a Node");
        } else{
            Node node1 = (Node)o1;
            return node1.compareTo(o2);
        }
        
    }
}
