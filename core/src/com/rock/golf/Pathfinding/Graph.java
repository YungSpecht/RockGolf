package com.rock.golf.Pathfinding;
import java.util.List;

import org.mariuszgromada.math.mxparser.Function;
import com.rock.golf.RockGolf;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Physics.Engine.Derivation;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;

public class Graph {
    float sizeX = RockGolf.width;
    float sizeY = RockGolf.height;
    float originX = sizeX / 2;
    float originY = sizeY / 2;
    float metertoPixelRatio = RockGolf.metertoPixelRatio;
    int counter = 0;

    public int[][] generateMatrix() {
        int counterI = 0;
        int counterJ = 0;
        int pixels = 10;
        int rows = (int) sizeX / pixels;
        int columns = (int) sizeY / pixels;
        Function profile = InputModule.getProfile();
        
        int[][] adiacencyMatrix = new int[rows + 1][columns + 1];

        for (int i = 0; i <= sizeX; i += pixels) {
            for (int j = 0; j <= sizeY; j += pixels) {
                float x = (i - originX) / metertoPixelRatio;
                float y = (j - originY) / metertoPixelRatio;

                float n = (float) Derivation.compute(x, y, profile);

                if (n < 0) {
                    adiacencyMatrix[counterI][counterJ] = 0;
                } else if(thereisObastacle(i,j)) {
                    adiacencyMatrix[counterI][counterJ] = 0;
                } else {
                    adiacencyMatrix[counterI][counterJ] = 1;
                }
                counterJ++;
            }
            counterJ = 0;
            counterI++;
        }

        return adiacencyMatrix;
    }

    private boolean thereisObastacle(int xPos, int yPos) {
        List<Tree> obstacles = RockGolf.trees;

        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).collidedWithTree(xPos, yPos)) {
                return true;
            }
        }
        return false;
    }
}
