package com.rock.golf.Bot;

import java.util.ArrayList;

import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Pathfinding.NodeFinder;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.StateVector;

public class PathBot extends Bot {
    private ArrayList<Node> path;
    private NodeFinder finder;
    private double[] currentShot;
    private Node furthestReach;

    public PathBot(PhysicsEngine engine, ArrayList<Node> path) {
        this.engine = engine;
        this.path = path;
        finder = new NodeFinder(path, engine);
    }

    @Override
    public double[] getMove() {
        long checkpoint = System.currentTimeMillis();
        StateVector current = engine.getVector();
        if(finder.unobstructed(current.getXPos(), current.getYPos(), path.get(path.size()-1))){
            HillClimb helperBot = new HillClimb(engine, 1000, 200);
            return helperBot.getMove();
        }
        furthestReach = finder.findNextTargetNode(current.getXPos(), current.getYPos());
        double nodeAngle = convert(Math.atan2(furthestReach.row*10-current.getYPos(), furthestReach.column*10-current.getXPos()));
        double[][][] shots = GenerateShotRange(nodeAngle, 3, 20, 2, 5, 10);
        currentShot = processShotsNode(shots, path, finder);
        if (path.indexOf(furthestReach) == (path.size()-1) || EuclideanDistance(engine.getSimulatedShot(currentShot[0], currentShot[1])) < targetRadius) {
            time = System.currentTimeMillis() - checkpoint;
            return currentShot;
        }
        int counter = 0;
        while (path.indexOf(furthestReach) != (path.size()-1) && counter < 5) {
            mountainClimber(0.2 - (0.025 * counter));
            ++counter;
        }

        counter = 0;
        while (path.indexOf(furthestReach) != (path.size()-1) && counter < 4) {
            mountainClimber(0.08 - (0.02 * counter));
            ++counter;
        }

        counter = 0;
        while (path.indexOf(furthestReach) != (path.size()-1) && counter < 5) {
            mountainClimber(0.01 - (0.002 * counter));
            ++counter;
        }
        time = System.currentTimeMillis() - checkpoint;
        return currentShot;
    }

    /**
     *
     * This method performs the hill climbing algorithm by generating the successor
     * nodes
     * by in/decreasing the velocity.
     *
     * @param precision The amount by which the x and/or y velocity will be altered
     *                  to
     *                  generate the successor states.
     */

    private void mountainClimber(double precision) {
        boolean successorAvailable;
        do {
            double[][] successors = new double[8][2];
            successors[0] = new double[] { currentShot[0] - precision, currentShot[1] };
            successors[1] = new double[] { currentShot[0] + precision, currentShot[1] };
            successors[2] = new double[] { currentShot[0], currentShot[1] - precision };
            successors[3] = new double[] { currentShot[0], currentShot[1] + precision };
            successors[4] = new double[] { currentShot[0] - precision, currentShot[1] + precision };
            successors[5] = new double[] { currentShot[0] + precision, currentShot[1] - precision };
            successors[6] = new double[] { currentShot[0] - precision, currentShot[1] - precision };
            successors[7] = new double[] { currentShot[0] + precision, currentShot[1] + precision };

            double[][] successorCoords = new double[successors.length][2];
            for (int i = 0; i < successors.length; i++) {
                if (velIsLegal(successors[i])) {
                    successorCoords[i] = engine.getSimulatedShot(successors[i][0], successors[i][1]);
                    iterationsCounter++;
                } else {
                    successorCoords[i] = null;
                }
            }
            int bestSuccessor = compareSuccessors(successorCoords);

            if (bestSuccessor != -1) {
                currentShot = successors[bestSuccessor];
                successorAvailable = true;
            } else {
                successorAvailable = false;
            }
            if (path.indexOf(furthestReach) == (path.size()-1) || EuclideanDistance(engine.getSimulatedShot(currentShot[0], currentShot[1])) < targetRadius) {
                return;
            }

        } while (successorAvailable);
    }

    /**
     *
     * This method compares the successor states and return the best one according
     * to the means of steepest descent hill climbing and simulated annealing
     *
     * @param successorCoords The coordinates resulting from the shots of the
     *                        successor states
     * @return The index of the successor state that was selected
     */

    private int compareSuccessors(double[][] successorCoords) {
        int result = -1;
        for (int i = 0; i < successorCoords.length; i++) {
            if (successorCoords[i] != null && !engine.isInWater(successorCoords[i][0], successorCoords[i][1])
                    && path.indexOf(finder.findNextTargetNode(successorCoords[i][0], successorCoords[i][1])) > path.indexOf(furthestReach)) {
                result = i;
                furthestReach = finder.findNextTargetNode(successorCoords[i][0], successorCoords[i][1]);
                if (path.indexOf(furthestReach) == (path.size()-1) || EuclideanDistance(successorCoords[i]) < targetRadius) {
                    return result;
                }
            }
        }
        return result;
    }

 
}
