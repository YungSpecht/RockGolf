package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class AngleBot extends Bot{
    private PhysicsEngine engine;
    private StateVector currentState;
    private double targetX, targetY, targetRad;
    private double[] bestShot;
    private double[] bestShotCoords;
    private double bestShotDistance;
    private double bestShotAngle;

    // constructor
    public AngleBot(PhysicsEngine engine) {
        double[] input = InputModule.get_input();
        this.engine = engine;
        this.currentState = new StateVector(input[5], input[6], input[7], input[8]);
        targetX = input[2];
        targetY = input[3];
        targetRad = input[4];
    }

    /**
     * This method ist the one that gets called from outside to find x and y
     * velocities that will result in the ball going into the target.
     * 
     * @return A double array of length 2 where the first index is the x velocity
     *         and the second index is the y velocity.
     */

     
    @Override
    public double[] getMove() {
        long time = System.currentTimeMillis();
        bestShot = new double[2];
        bestShotCoords = new double[] { currentState.getXPos(), currentState.getYPos() };
        bestShotDistance = EuclideanDistance(bestShotCoords);
        bestShotAngle = convert(Math.atan2(targetY - bestShotCoords[1], targetX - bestShotCoords[0]));

        double[][][] shotsArray = generate_shots(bestShotAngle, 5, 40, 5, 4, 5);
        double[][] centerShots = shotsArray[shotsArray.length / 2];
        double[][][] leftShots = arrange_shots(shotsArray, 'l');
        double[][][] rightShots = arrange_shots(shotsArray, 'r');
        get_best_shot(centerShots, leftShots, rightShots, 6);
        if (bestShotDistance < targetRad) {
            System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
            return bestShot;
        }

        System.out.println("Intermediate checkpoint: " + (bestShotDistance - targetRad));

        int counter = 0;
        while (bestShotDistance >= targetRad && counter < 5) {
            bestShotAngle = convert(Math.atan2(bestShot[1], bestShot[0]));
            double[] velRange = get_velocity_range(bestShot, 0.4);
            shotsArray = generate_shots(bestShotAngle, 10, 30 - ((1 + counter) * 7.5), 10 + ((counter + 1) * 4), velRange[0], velRange[1]);
            centerShots = shotsArray[shotsArray.length / 2];
            leftShots = arrange_shots(shotsArray, 'l');
            rightShots = arrange_shots(shotsArray, 'r');
            get_best_shot(centerShots, leftShots, rightShots, counter);
            if (bestShotDistance < targetRad) {
                System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
                return bestShot;
            }
            System.out.println("Iteration: " + ++counter);
        }
        System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
        return bestShot;
    }

    /**
     * This method is responsible for simulating the shots using the physics engine
     * and implements a pruning tactic.
     * 
     * @param centerShots Array of shots in the direction of the centered angle.
     * @param leftShots   Array of shots diverging to the left of the centered angle.
     * @param rightShots  Array of shots diverging to the right of the centered angle.
     * @return            A double array of length 2 where the first index is the x velocity
     *                    and the second index is the y velocity.
     */
    private void get_best_shot(double[][] centerShots, double[][][] leftShots, double[][][] rightShots,
        int multiplicator) {
        int tracker = 0;
        boolean cancelRight = false;
        boolean cancelLeft = false;
        int lCount = 0;
        int rCount = 0;
        for (int i = 0; i < centerShots.length; i++) {
            double[] shotCoords = engine.get_shot(centerShots[i][0], centerShots[i][1]);
            double distance = EuclideanDistance(shotCoords);
            compare_shot(centerShots[i], shotCoords, distance);
        }
        for (int i = 0; i < leftShots.length; i++) {
            boolean leftPruned = false;
            boolean rightPruned = false;
            int leftCounter = 0;
            int rightCounter = 0;
            for (int j = 0; j < leftShots[0].length; j++) {
                if (!leftPruned && !cancelLeft) {
                    double[] leftShotCoords = engine.get_shot(leftShots[i][j][0], leftShots[i][j][1]);
                    double leftShotDistance = EuclideanDistance(leftShotCoords);
                    if (leftShotDistance < bestShotDistance) {
                        tracker = 0;
                        leftCounter = 0;
                        lCount = 0;
                    } else {
                        leftCounter++;
                        lCount++;
                        tracker++;
                    }
                    if (leftCounter > 8) {
                        leftPruned = true;
                    }
                    if (lCount > 15) {
                        cancelLeft = true;
                    }
                    compare_shot(leftShots[i][j], leftShotCoords, leftShotDistance);
                    if (bestShotDistance < targetRad) {
                        return;
                    }
                    if (tracker > 25 - (multiplicator + 1 * 2)) {
                        return;
                    }
                }
                if (!rightPruned && !cancelRight) {
                    double[] rightShotCoords = engine.get_shot(rightShots[i][j][0], rightShots[i][j][1]);
                    double rightShotDistance = EuclideanDistance(rightShotCoords);
                    if (rightShotDistance - targetRad < bestShotDistance) {
                        rightCounter = 0;
                        rCount = 0;
                        tracker = 0;
                    } else {
                        rightCounter++;
                        rCount++;
                        tracker++;
                    }
                    if (rightCounter > 8) {
                        rightPruned = true;
                    }
                    if (rCount > 15) {
                        cancelRight = true;
                    }
                    compare_shot(rightShots[i][j], rightShotCoords, rightShotDistance);
                    if (bestShotDistance < targetRad) {
                        return;
                    }
                    if (tracker > 25 - (multiplicator + 1 * 2)) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * This method compares a shot against the current best shot and updates the
     * fields accordingly.
     * 
     * @param shot       Array containing the x and y velocity of the shot.
     * @param shotCoords Array containing the x and y position of the shot.
     * @param distance   The euclidian distance from the shot to the target.
     */
    private void compare_shot(double[] shot, double[] shotCoords, double distance) {
        if (distance < bestShotDistance) {
            bestShot = shot;
            bestShotCoords = shotCoords;
            bestShotDistance = distance;
            bestShotAngle = convert(Math.atan2(targetY - bestShotCoords[1], targetX - bestShotCoords[0]));
            System.out.println("Current shortest Distance to Target: " + (bestShotDistance - targetRad));
        }
    }

    /**
     * This method will generate a multitude of shots based on the input parameters
     * its given.
     * 
     * @param angle          The angle of the main reference shot in the middle.
     * @param divergentShots The amount of divergent shots to the left OR to the right.
     * @param outermostAngle The delta in angle of the outermost and the reference shot.
     * @param velocitySims   The amount of shots into which the velocity range is divided.
     * @param velocityStart  The start value of the velocity range [0.0-5.0].
     * @param velocityEnd    The end value of the velocity range [0.0-5.0].
     * @return               A three dimensional double array where each two dimensional double
     *                       array is a collection of shots in an angle with different velocities.
     */
    private double[][][] generate_shots(double angle, int divergetShots, double outermostAngle, int velocitySims,
        double velocityStart, double velocityEnd) {
        double[][][] result = new double[1 + divergetShots * 2][velocitySims][2];
        for (int i = 0; i < result[0].length; i++) {
            result[result.length / 2][i] = get_velocity(angle, velocityStart + (i + 1) * ((velocityEnd - velocityStart) / velocitySims));
        }
        for (int i = 0; i < divergetShots; i++) {
            for (int j = 0; j < result[0].length; j++) { 
                result[(result.length / 2) + i + 1][j] = get_velocity((angle + (outermostAngle / divergetShots) * (i + 1)) % 360, velocityStart + (j + 1) * ((velocityEnd - velocityStart) / velocitySims));
                result[(result.length / 2) - i - 1][j] = get_velocity((angle - (outermostAngle / divergetShots) * (i + 1)) % 360, velocityStart + (j + 1) * ((velocityEnd - velocityStart) / velocitySims));
            }
        }
        return result;
    }

    /**
     * This method arranges the shots to the left or right side of the reference
     * angle into array starting from the reference angle towards the outside.
     * 
     * @param shots Array containing the x and y velocity of the shot.
     * @param side  A char deciding which side to order; 'r' for right, 'l for left'.
     * @return      Array containing the ordered shots
     */
    private double[][][] arrange_shots(double[][][] shots, char side) {
        double[][][] result = new double[shots.length / 2][shots[0].length][2];
        if (side == 'l') {
            for (int i = 0; i < (shots.length / 2) - 1; i++) {
                result[i] = shots[(shots.length / 2) - 1 - i];
            }
        } else {
            for (int i = (shots.length / 2) + 1; i < shots.length; i++) {
                result[i - ((shots.length / 2) + 1)] = shots[i];
            }
        }
        return result;
    }

 
}
