package com.rock.golf;

import java.util.*;

import com.rock.golf.Pathfinding.AStar1;
import com.rock.golf.Pathfinding.Astar;
import com.rock.golf.Pathfinding.BFS;
import com.rock.golf.Pathfinding.Graph;
import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.StateVector;
import com.rock.golf.Physics.Engine.rectangleObstacle;

public class randomMaze {

    int pixels = 20;
    Random rn = new Random();
    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    int rows = (int) sizeX / pixels;
    int columns = (int) sizeY / pixels;
    LinkedList<Cell> walls;
    Cell[][] wallGrid;
    Node[][] graphArray;
    Graph graph;
    PhysicsEngine physics;

    int startX;
    int startY;
    int goalX;
    int goalY;

    public randomMaze(Graph graph, float startX, float startY, float goalX, float goalY, PhysicsEngine physics) {
        this.graph = graph;
        graphArray = graph.generateMatrix();
        walls = new LinkedList<>();
        int rows = (int) sizeX / pixels;
        int columns = (int) sizeY / pixels;
        wallGrid = new Cell[rows + 1][columns + 1];
        this.physics = physics;

        this.startX = (int) startX;
        this.startY = (int) startY;
        this.goalX = (int) goalX;
        this.goalY = (int) goalY;
    }

    public Cell[][] generateMaze() {

        int cellX = (int) RockGolf.xPosition / 20;
        int cellY = (int) RockGolf.yPosition / 20;
        int tX = (int) RockGolf.targetxPosition / 20;
        int tY = (int) RockGolf.targetyPosition / 20;

        BFS bfs = new BFS();
        bfs.BFSWalkSearch(graph, graphArray[startX / 10][startY/10], graphArray[goalX/10][goalY/10]);

        int counterI = 0;
        int counterJ = 0;
        for (int i = 0; i <= sizeX; i += pixels) {
            for (int j = 0; j <= sizeY; j += pixels) {
                // if(graphArray[counterI * 2][counterJ *2].isPath) {
                //     wallGrid[counterI][counterJ] = new Cell(counterI, counterJ);
                //     wallGrid[counterI][counterJ].isMaze = false;
                //     counterJ++;
                //     continue;
                // }

                // if(counterI != 0 && counterJ != 0 && graphArray[(counterI * 2) - 1][counterJ*2 - 1].isPath) {
                //     wallGrid[counterI][counterJ] = new Cell(counterI, counterJ);
                //     wallGrid[counterI][counterJ].isMaze = false;
                //     counterJ++;
                //     continue;
                // }
                // if(counterI != 0  && graphArray[(counterI * 2) - 1][counterJ*2 ].isPath) {
                //     wallGrid[counterI][counterJ] = new Cell(counterI, counterJ);
                //     wallGrid[counterI][counterJ].isMaze = false;
                //     counterJ++;
                //     continue;
                // }
                // if( counterJ != 0 && graphArray[(counterI * 2) ][counterJ*2 - 1].isPath) {
                //     wallGrid[counterI][counterJ] = new Cell(counterI, counterJ);
                //     wallGrid[counterI][counterJ].isMaze = false;
                //     counterJ++;
                //     continue;
                // }
                wallGrid[counterI][counterJ] = new Cell(counterI, counterJ);

                // Comment this back in to prevent the maze from being generated:
                // wallGrid[counterI][counterJ].isMaze=false;
                counterJ++;
            }
            counterJ = 0;
            counterI++;
        }

        HashSet<Cell> visited = new HashSet<>();
        wallGrid[startX / pixels][startY / pixels].isMaze = false;
        wallGrid[goalX / pixels][goalY / pixels].isMaze = false;
        setNeighbours(startX / pixels, startY / pixels);
        setNeighbours(goalX / pixels, goalY / pixels);

        while (!walls.isEmpty()) {
            int element = rn.nextInt(walls.size());
            Cell wall = walls.get(element);
            walls.remove(element);

            if (!visited.contains(wall) && !hasUnvisitedCell(wall)) {

                double tempX = (wall.wall.getPosition()[0] - RockGolf.originX - 0.5) / RockGolf.metertoPixelRatio;
                double tempY = (wall.wall.getPosition()[1] - RockGolf.originY - 0.5) / RockGolf.metertoPixelRatio;
                setNeighbours(wall.row, wall.column);

                if (physics.isInWater(tempX, tempY)) {
                    visited.add(wall);
                    continue;
                }

                wall.isMaze = false;

            } else if(graphArray[wall.row * 2][wall.column *2].isPath) {
                    wall.isMaze = false;
            } else if(wall.row != 0 && wall.column != 0 && graphArray[wall.row- 1][wall.column*2 - 1].isPath) {
                    wall.isMaze = false;
            } else if(wall.row != 0  && graphArray[(wall.row * 2) - 1][wall.column*2 ].isPath) {
                    wall.isMaze = false;
            } else if( wall.column != 0 && graphArray[(wall.row * 2) ][wall.column*2 - 1].isPath) {
                    wall.isMaze = false;
                }
        }
        
        wallGrid[(startX / pixels)-1][startY / pixels].isMaze = false;
        wallGrid[startX / pixels][(startY / pixels)-1].isMaze = false;
        wallGrid[startX / pixels][(startY / pixels)+1].isMaze = false;
        wallGrid[(startX / pixels)+1][startY / pixels].isMaze = false;
        wallGrid[(goalX / pixels)+1][goalY / pixels].isMaze = false;
        wallGrid[goalX / pixels][(goalY / pixels)+1].isMaze = false;
        wallGrid[goalX / pixels][(goalY / pixels)-1].isMaze = false;
        wallGrid[(goalX / pixels)-1][goalY / pixels].isMaze = false;
        
        return wallGrid;
    }

    public void setNeighbours(int cellX, int cellY) {
        if (cellX + 1 < wallGrid.length && wallGrid[cellX + 1][cellY].isMaze) {
            walls.add(wallGrid[cellX + 1][cellY]);
        }
        if (cellX - 1 >= 0 && wallGrid[cellX - 1][cellY].isMaze) {
            walls.add(wallGrid[cellX - 1][cellY]);
        }
        if (cellY + 1 < wallGrid[0].length && wallGrid[cellX][cellY + 1].isMaze) {
            walls.add(wallGrid[cellX][cellY + 1]);
        }
        if (cellY - 1 >= 0 && wallGrid[cellX][cellY - 1].isMaze) {
            walls.add(wallGrid[cellX][cellY - 1]);
        }
    }

     public boolean hasUnvisitedCell(Cell wall) {
        int counter = 0;

        if (wall.row + 1 < wallGrid.length) {
            if (!wallGrid[wall.row + 1][wall.column].isMaze) {
                counter++;
            }
        }
        if (wall.row - 1 >= 0) {
            if (!wallGrid[wall.row - 1][wall.column].isMaze) {
                counter++;
            }
        }
        if (wall.column + 1 < wallGrid[0].length) {
            if (!wallGrid[wall.row][wall.column + 1].isMaze) {
                counter++;
            }
        }
        if (wall.column - 1 >= 0) {
            if (!wallGrid[wall.row][wall.column - 1].isMaze) {
                counter++;
            }
        }
        if (counter >= 2)
            return true;
        else
            return false;
    }
}
