package com.rock.golf;

import com.rock.golf.Physics.Engine.RectangleObstacle;

public class Cell {

    int row;
    int column;
    RectangleObstacle wall;
    boolean isMaze = true;
    boolean immutable = false;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        float x = row * 20;
        float y = column * 20;
        wall = new RectangleObstacle(new float[]{x,y}, 20, 20);
    }
}
