package com.rock.golf;

import java.util.*;

import com.rock.golf.Pathfinding.Graph;
import com.rock.golf.Pathfinding.Node;

public class randomMaze {

    int pixels = 20;
    Random rn = new Random();
    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    int rows = (int) sizeX / pixels;
    int columns = (int) sizeY / pixels;
    LinkedList<Cell> walls;
    Cell[][] wallGrid;
    Graph graph;

    public randomMaze(Graph graph) {
        this.graph = graph;
        walls = new LinkedList<>();
        int rows = (int) sizeX / pixels;
        int columns = (int) sizeY / pixels;
        wallGrid = new Cell[rows + 1][columns + 1];
    }

    public Cell[][] generateMaze() {
        int counterI = 0;
        int counterJ = 0;
        for (int i = 0; i <= sizeX; i +=pixels) {
            for (int j = 0; j <= sizeY; j +=pixels) {
                wallGrid[counterI][counterJ] = new Cell(counterI,counterJ);
                counterJ++;
            }
            counterJ = 0;
            counterI++;
        }

        int cellX = rn.nextInt(wallGrid.length);
        int cellY = rn.nextInt(wallGrid[0].length);
        wallGrid[cellX][cellY].isMaze = true;
        setNeighbours(cellX, cellY);

        while(!walls.isEmpty()) {
            int element = rn.nextInt(walls.size());
            Cell wall = walls.get(element);
            if(!hasUnvisitedCell(wall)) {
                wall.isMaze = false;
                setNeighbours(wall.row, wall.column);
            }
            walls.remove(element);
        }

        return wallGrid;
    }

    public void setNeighbours(int cellX, int cellY) {
        if(cellX + 1 < wallGrid.length && wallGrid[cellX + 1][cellY].isMaze ) {
            walls.add(wallGrid[cellX + 1][cellY]);
        }

        if(cellX - 1 >= 0 && wallGrid[cellX - 1][cellY].isMaze ) {
            walls.add(wallGrid[cellX - 1][cellY]);
        }

        if(cellY + 1 < wallGrid[0].length && wallGrid[cellX][cellY + 1].isMaze) {
            walls.add(wallGrid[cellX][cellY + 1]);
        }

        if(cellY - 1 >= 0 && wallGrid[cellX][cellY - 1].isMaze) {
            walls.add(wallGrid[cellX][cellY - 1]);
        }
    }

    public boolean hasUnvisitedCell(Cell wall) {
        int counter = 0;

        if(wall.row + 1 < wallGrid.length) {
            if(!wallGrid[wall.row + 1][wall.column].isMaze) {
                counter++;
            }
        }

        if(wall.row - 1 >= 0) {
            if(!wallGrid[wall.row - 1][wall.column].isMaze) {
                counter++; 
            }
        }

        if(wall.column + 1 < wallGrid[0].length) {
            if(!wallGrid[wall.row][wall.column + 1].isMaze) {
                counter++;
            }
        }

        if(wall.column - 1 >= 0) {
            if(!wallGrid[wall.row][wall.column - 1].isMaze) {
                counter++;
            };
        }

        if(counter >= 2) return true;
        else return false;
    }
}
