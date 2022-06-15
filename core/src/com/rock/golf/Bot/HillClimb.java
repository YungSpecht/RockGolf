package com.rock.golf.Bot;

import com.rock.golf.Physics.Engine.PhysicsEngine;

/**
 * HillClimb bot
 */

public class HillClimb extends Bot {
    private double[] currentShot;
    private double[] currentShotCoords;
    private double currentShotDistance;
    private final double MAX_TEMP;
    private double currentTemp;
    private double coolingRate;

    public HillClimb(PhysicsEngine engine, double MAX_TEMP, double coolingRate) {
        super();
        this.engine = engine;
        this.MAX_TEMP = MAX_TEMP;
        currentTemp = MAX_TEMP;
        this.coolingRate = coolingRate;
    }

    @Override
    /**
     * Inherited abstract class from super
     */

    public double[] getMove() {
        long checkpoint = System.currentTimeMillis();
        double angle = convert(Math.atan2(targetPos[1] - ballPos[1], targetPos[0] - ballPos[0]));

        double[][][] shots = GenerateShotRange(angle, 4, 45, 5, 5, 1);
        currentShot = processShots(shots, EuclideanDistance(ballPos), 0);
        currentShotCoords = engine.getSimulatedShot(currentShot[0], currentShot[1]);
        currentShotDistance = EuclideanDistance(currentShotCoords);
        if (currentShotDistance < targetRadius) {
            time = System.currentTimeMillis() - checkpoint;
            return currentShot;
        }

        angle = convert(Math.atan2(currentShot[1], currentShot[0]));
        shots = GenerateShotRange(angle, 5, 20, 3.5, 5, 10);
        currentShot = processShots(shots, currentShotDistance, 0);
        currentShotCoords = engine.getSimulatedShot(currentShot[0], currentShot[1]);
        currentShotDistance = EuclideanDistance(currentShotCoords);

        if (currentShotDistance < targetRadius) {
            time = System.currentTimeMillis() - checkpoint;
            return currentShot;
        }
        int counter = 0;
        while (currentShotDistance >= targetRadius && counter < 5) {
            mountainClimber(0.2 - (0.025 * counter));
            System.out.println("LOOP 1 || Iteration: " + ++counter);
        }

        counter = 0;
        while (currentShotDistance >= targetRadius && counter < 4) {
            mountainClimber(0.08 - (0.02 * counter));
            System.out.println("LOOP 2 || Iteration: " + ++counter);
        }

        counter = 0;
        while (currentShotDistance >= targetRadius && counter < 5) {
            mountainClimber(0.01 - (0.002 * counter));
            System.out.println("LOOP 3 || Iteration: " + ++counter);
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
                currentShotCoords = successorCoords[bestSuccessor];
                currentShotDistance = EuclideanDistance(currentShotCoords);
                successorAvailable = true;
            } else {
                successorAvailable = false;
            }
            if (currentShotDistance < targetRadius) {
                return;
            }
            if (currentTemp > 0) {
                currentTemp -= coolingRate;
                System.out.println("Current Temperature: " + currentTemp);
                if (currentTemp < 0) {
                    currentTemp = 0;
                }
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
        double reference = currentShotDistance;
        int result = -1;
        for (int i = 0; i < successorCoords.length; i++) {
            if (successorCoords[i] != null && !engine.isInWater(successorCoords[i][0], successorCoords[i][1])
                    && annealing(successorCoords[i], reference)) {
                result = i;
                reference = EuclideanDistance(successorCoords[i]);
                System.out.println("New Shortest Distance: " + (reference - targetRadius));
                if (reference < targetRadius) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     *
     * This function determines wether a state that worsenes the current state
     * will be accepted as the new current state based on the current temperature
     * and the amount by which it worsenes the current state
     *
     * @param coords      The ball coordinate of the successor state
     * @param refDistance The target distance of the current best state
     * @return true if the state was accepted by the function false otherwise
     */

    private boolean annealing(double[] coords, double refDistance) {
        if (EuclideanDistance(coords) < refDistance) {
            return true;
        }
        double control = currentTemp / (MAX_TEMP * (EuclideanDistance(coords) * 12 - refDistance));
        double rando = rand.nextDouble();
        return rando < control;
    }
}
