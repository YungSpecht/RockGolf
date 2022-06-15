package com.rock.golf;

import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Physics.Engine.rectangleObstacle;

public class Cell {

    int row;
    int column;
    rectangleObstacle wall;
    boolean isMaze = true;

    public Cell(int row, int column) {

        this.row = row;
        this.column = column;
        float x = row * 20;
        float y = column * 20;
        wall = new rectangleObstacle(new float[]{x,y}, 20, 20);
    }
}
