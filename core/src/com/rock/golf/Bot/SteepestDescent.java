package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;

public class SteepestDescent extends Bot {
    private double[] currentShot;
    private double[] currentShotCoords;
    private double currentShotDistance;

    public SteepestDescent(PhysicsEngine engine) {
        super();
        this.engine = engine;
    }

    @Override
    public double[] getMove() {
        long time = System.currentTimeMillis();
        currentShot = new double[2];
        currentShotCoords = new double[]{ballPos[0], ballPos[1]};
        currentShotDistance = EuclideanDistance(currentShotCoords);
        double[][][] shots = generate_shot_range(convert(Math.atan2(targetPos[1] - currentShotCoords[1], targetPos[0] - ballPos[0])), 5, 45, 3, 4.5, 10);
        currentShot = process_shots(shots, currentShotDistance, 0.2);
        currentShotCoords = engine.get_shot(currentShot[0], currentShot[1]);
        currentShotDistance = EuclideanDistance(currentShotCoords);
        System.out.println("++HILL CLIMB STARTS NOW+++");

        int counter = 0;
        while (currentShotDistance >= targetRadius && counter < 5) {
            mountain_climber(0.2 - (0.025 * counter));
            System.out.println("LOOP 1 || Iteration: " + ++counter);
        }

        counter = 0;
        while (currentShotDistance >= targetRadius && counter < 4) {
            mountain_climber(0.08 - (0.02 * counter));
            System.out.println("LOOP 2 || Iteration: " + ++counter);
        }

        counter = 0;
        while (currentShotDistance >= targetRadius && counter < 5) {
            mountain_climber(0.01 - (0.002 * counter));
            System.out.println("LOOP 3 || Iteration: " + ++counter);
        }
        System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
        return currentShot;
    }

    private void mountain_climber(double precision) {
        boolean successorAvailable;
        do{
            double[][] successors = new double[6][2];
            successors[0] = new double[]{currentShot[0] - precision, currentShot[1]};
            successors[1] = new double[]{currentShot[0] + precision, currentShot[1]};
            successors[2] = new double[]{currentShot[0], currentShot[1] - precision};
            successors[3] = new double[]{currentShot[0], currentShot[1] + precision};
            successors[4] = new double[]{currentShot[0] - precision, currentShot[1] + precision};
            successors[5] = new double[]{currentShot[0] + precision, currentShot[1] - precision};

            double[][] successorCoords = new double[successors.length][2];
            for (int i = 0; i < successors.length; i++) {
                if (vel_is_legal(successors[i])) {
                    successorCoords[i] = engine.get_shot(successors[i][0], successors[i][1]);
                } else {
                    successorCoords[i] = null;
                }
            }
            int bestSuccessor = compare_successors(successorCoords);

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

        } while (successorAvailable);
    }

    private int compare_successors(double[][] successorCoords) {
        double reference = currentShotDistance;
        int result = -1;
        for (int i = 0; i < successorCoords.length; i++) {
            if (successorCoords[i] != null && !engine.is_in_water(successorCoords[i]) && engine.ball_in_screen(successorCoords[i]) &&EuclideanDistance(successorCoords[i]) < reference) {
                result = i;
                reference = EuclideanDistance(successorCoords[i]);
                System.out.println("New Shortest Distance: " + reference);
                if (reference < targetRadius) {
                    return result;
                }
            }
        }
        return result;
    }

    private boolean vel_is_legal(double[] velPair) {
        return Math.sqrt(Math.pow(velPair[0], 2) + Math.pow(velPair[1], 2)) <= 5.0;
    }
}
