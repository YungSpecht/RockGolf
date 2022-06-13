package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.mariuszgromada.math.mxparser.Function;
import com.rock.golf.RockGolf;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Physics.Engine.Derivation;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;
import com.rock.golf.Physics.Engine.rectangleObstacle;

public class Graph implements Comparator {
    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    List<rectangleObstacle> rectangles;
    List<Tree> trees;
    float originX = sizeX / 2;
    float originY = sizeY / 2;
    float metertoPixelRatio = RockGolf.metertoPixelRatio;
    int counter = 0;
    Node[][] adjacencyMatrix;
    public int[] lowerCoordinates = new int[2];
    {
        Arrays.fill(lowerCoordinates, Integer.MAX_VALUE);
    }
    public int[] higherCoordinates = new int[2];
    {
        Arrays.fill(higherCoordinates, 0);
    }

    public Node[][] generateMatrix() {
        int counterI = 0;
        int counterJ = 0;
        int pixels = 10;
        int rows = (int) sizeX / pixels;
        int columns = (int) sizeY / pixels;
        Function profile = InputModule.getProfile();
        trees = PhysicsEngine.trees;
        rectangles = PhysicsEngine.rectangles;
        adjacencyMatrix = new Node[rows + 1][columns + 1];

        for (float i = 0; i <= sizeX; i += pixels) {
            for (float j = 0; j <= sizeY; j += pixels) {
                float x = (i - originX) / metertoPixelRatio;
                float y = (j - originY) / metertoPixelRatio;

                float n = (float) Derivation.compute(x, y, profile);

                if (n < 0) {
                    if (counterI + 1 < lowerCoordinates[0])
                        lowerCoordinates[0] = counterI + 1;
                    if (counterI + 1 > higherCoordinates[0])
                        higherCoordinates[0] = counterI + 1;
                    if (counterJ + 1 < lowerCoordinates[1])
                        lowerCoordinates[1] = counterJ + 1;
                    if (counterJ + 1 > higherCoordinates[1])
                        higherCoordinates[1] = counterJ + 1;
                    adjacencyMatrix[counterI][counterJ] = new Node(1, null, counterI, counterJ);
                } else if (thereisObastacle(x, y)) {
                    adjacencyMatrix[counterI][counterJ] = new Node(0, null, counterI, counterJ);
                } else {
                    adjacencyMatrix[counterI][counterJ] = new Node(1, null, counterI, counterJ);
                }
                counterJ++;
            }
            counterJ = 0;
            counterI++;
        }

        for (int i = lowerCoordinates[0]; i < higherCoordinates[0]; i++) {
            for (int j = lowerCoordinates[1]; j < higherCoordinates[1]; j++) {
                adjacencyMatrix[i][j] = new Node(0, null, i, j);
            }
        }

        return adjacencyMatrix;
    }

    private boolean thereisObastacle(float xPos, float yPos) {

        for (int i = 0; i < trees.size(); i++) {
            if (trees.get(i).collidedWithTree(xPos, yPos, 0.05)) {
                return true;
            }
        }

        for (int i = 0; i < rectangles.size(); i++) {
            if (rectangles.get(i).obstacleCollision(xPos, yPos, 0.05)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Node> neighbors(Node currentNode) {
        int row = currentNode.row;
        int column = currentNode.column;
        ArrayList<Node> neighbors = new ArrayList<>();

        if (column + 1 < adjacencyMatrix[row].length && adjacencyMatrix[row][column + 1].currentNodeValue == 1) {
            if (adjacencyMatrix[row][column + 1].parent == null)
                adjacencyMatrix[row][column + 1].parent = currentNode;
            neighbors.add(adjacencyMatrix[row][column + 1]);
        }

        if (column != 0 && adjacencyMatrix[row][column - 1].currentNodeValue == 1) {
            if (adjacencyMatrix[row][column - 1].parent == null)
                adjacencyMatrix[row][column - 1].parent = currentNode;
            neighbors.add(adjacencyMatrix[row][column - 1]);
        }

        if (row + 1 < adjacencyMatrix.length && adjacencyMatrix[row + 1][column].currentNodeValue == 1) {
            if (adjacencyMatrix[row + 1][column].parent == null)
                adjacencyMatrix[row + 1][column].parent = currentNode;
            neighbors.add(adjacencyMatrix[row + 1][column]);
        }

        if (row != 0 && adjacencyMatrix[row - 1][column].currentNodeValue == 1) {
            if (adjacencyMatrix[row - 1][column].parent == null)
                adjacencyMatrix[row - 1][column].parent = currentNode;
            neighbors.add(adjacencyMatrix[row - 1][column]);
        }

        return neighbors;
    }

    @Override
    public int compare(Object o1, Object o2) {

        Node node1 = (Node) o1;
        return node1.compareTo(o2);

    }
}
