package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class AIBot {
    private PhysicsEngine engine;
    private StateVector currentState;
    private double targetX, targetY, targetRad;
    private double[] bestShot;
    private double[] bestShotCoords;
    private double bestShotDistance;
    private double bestShotAngle;

    // constructor
    public AIBot(PhysicsEngine engine) {
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
    public double[] get_shot() {
        bestShot = new double[2];
        bestShotCoords = new double[]{currentState.getXPos(), currentState.getYPos()};
        bestShotDistance = euclidian_distance(bestShotCoords);
        bestShotAngle = convert(Math.atan2(targetY - bestShotCoords[1], targetX - bestShotCoords[0]));

        double[][][] shotsArray = generate_shots(bestShotAngle, 10, 40, 10, 2, 4.5);
        double[][] centerShots = shotsArray[shotsArray.length/2];
        double[][][] leftShots = arrange_shots(shotsArray, 'l');
        double[][][] rightShots = arrange_shots(shotsArray, 'r');
        get_best_shot(centerShots, leftShots, rightShots, 0.15);
        if(bestShotDistance < targetRad){
            return bestShot;
        }

        System.out.println("Intermediate checkpoint: " + (bestShotDistance - targetRad));

        int counter = 0;
        while(bestShotDistance >= targetRad && counter < 5){
            bestShotAngle = convert(Math.atan2(bestShot[1], bestShot[0]));
            double[] velRange = get_velocity_range(bestShot);
            shotsArray = generate_shots(bestShotAngle, 10, 30 - ((1 + counter) * 7.5), 10 + ((counter + 1) * 4), velRange[0], velRange[1]);
            centerShots = shotsArray[shotsArray.length/2];
            leftShots = arrange_shots(shotsArray, 'l');
            rightShots = arrange_shots(shotsArray, 'r');
            get_best_shot(centerShots, leftShots, rightShots, 0);
            if(bestShotDistance < targetRad){
                return bestShot;
            }
            System.out.println("Iteration: " + ++counter);
        }
        return bestShot;
    }

    /**
     * This method is responsible for simulating the shots using the physics engine and 
     * implements a pruning tactic.
     * 
     * @param centerShots Array of shots in the direction of the centered angle.
     * @param leftShots   Array of shots diverging to the left of the centered angle.
     * @param rightShots  Array of shots diverging to the right of the centered angle.
     * @return            A double array of length 2 where the first index is the x velocity
     *                    and the second index is the y velocity.
     */
    private void get_best_shot(double[][] centerShots, double[][][] leftShots, double[][][]rightShots, double instantReturnRange){
        boolean cancelRight = false;
        boolean cancelLeft = false;
        int lCount = 0;
        int rCount = 0;
        for(int i = 0; i < centerShots.length; i++){
            double[] shotCoords = engine.get_shot(centerShots[i][0], centerShots[i][1]);
            double distance = euclidian_distance(shotCoords);
            compare_shot(centerShots[i], shotCoords, distance);
        }
        for(int i = 0; i < leftShots.length; i++){
            boolean leftPruned = false;
            boolean rightPruned = false;
            int leftCounter = 0;
            int rightCounter = 0;
            for(int j = 0; j < leftShots[0].length; j++){
                if(!leftPruned && !cancelLeft){
                    double[] leftShotCoords = engine.get_shot(leftShots[i][j][0], leftShots[i][j][1]);
                    double leftShotDistance = euclidian_distance(leftShotCoords);
                    if(leftShotDistance < bestShotDistance){
                        leftCounter = 0;
                        lCount = 0;
                    }
                    else{
                        leftCounter++;
                        lCount++;
                    }
                    if(leftCounter > 10){
                        leftPruned = true;
                    }
                    if(lCount > 15){
                        cancelLeft = true;
                    }
                    compare_shot(leftShots[i][j], leftShotCoords, leftShotDistance);
                    if(bestShotDistance < instantReturnRange){
                        return;
                    }
                }
                if(!rightPruned && !cancelRight){
                    double[] rightShotCoords = engine.get_shot(rightShots[i][j][0], rightShots[i][j][1]);
                    double rightShotDistance = euclidian_distance(rightShotCoords);
                    if(rightShotDistance-targetRad < bestShotDistance){
                        rightCounter = 0;
                        rCount = 0;
                    }
                    else{
                        rightCounter++;
                        rCount++;
                    }
                    if(rightCounter > 10){
                        rightPruned = true;
                    }
                    if(rCount > 15){
                        cancelRight = true;
                    }
                    compare_shot(rightShots[i][j], rightShotCoords, rightShotDistance);
                    if(bestShotDistance-targetRad < instantReturnRange){
                        return;
                    }
                }
                if(bestShotDistance < targetRad ||  cancelLeft&&cancelRight){
                    return;
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
    private void compare_shot(double[] shot, double[] shotCoords, double distance){
        if(distance < bestShotDistance){
            bestShot = shot;
            bestShotCoords = shotCoords;
            bestShotDistance = distance;
            bestShotAngle = convert(Math.atan2(targetY - bestShotCoords[1], targetX - bestShotCoords[0]));
            System.out.println("Current shortest Distance to Target: " + (bestShotDistance - targetRad));
        }
    }

    /**
     * This method will generate a multitude of shots based on the input parameters its given.
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
    private double[][][] generate_shots(double angle, int divergetShots, double outermostAngle, int velocitySims, double velocityStart, double velocityEnd) {
        double[][][] result = new double[1+divergetShots*2][velocitySims][2];
        for(int i = 0; i < result[0].length; i++){
            result[result.length/2][i] = get_velocity(angle, velocityStart + (i+1) * ((velocityEnd-velocityStart)/velocitySims));
        }
        for(int i = 0; i < divergetShots; i++){
            for(int j = 0; j < result[0].length; j++){
                result[(result.length/2) + i + 1][j] = get_velocity((angle + (outermostAngle/divergetShots) * (i+1)) % 360, velocityStart + (j+1) * ((velocityEnd-velocityStart)/velocitySims));
                result[(result.length/2) - i - 1][j] = get_velocity((angle - (outermostAngle/divergetShots) * (i+1)) % 360, velocityStart + (j+1) * ((velocityEnd-velocityStart)/velocitySims));
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
    private double[][][] arrange_shots(double[][][] shots, char side){
        double[][][] result = new double[shots.length/2][shots[0].length][2];
        if(side == 'l'){
            for(int i = 0; i < (shots.length/2)-1; i++){
                result[i] = shots[(shots.length/2)-1-i];
            }
        }
        else{
            for(int i = (shots.length/2)+1; i < shots.length; i++){
                result[i - ((shots.length/2)+1)] = shots[i];
            }
        }
        return result;
    }


    /**
     * This method will generate a shot in the direction of the specified angle with
     * the specified velocity.
     * 
     * @param angle    The angle the shot is supposed to follow.
     * @param velocity The velocity with which the ball is suppoded to be shot.
     * @return         A double array containing the x and y velocity for the desired shot.
     */
    private double[] get_velocity(double angle, double velocity){
        double[] result = new double[]{Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))};
        return scale_velocity(result, velocity);
    }

    /**
     * This method will create a velocity range around the input velocity.
     * 
     * @param velocity A double array containing the current x and y velocity.
     * @return         A double array containing a start- and endpoint of the 
     *                 velocity range in the range [0.0-5.0].
     */
    private double[] get_velocity_range(double[] velocity){
        double currentVel = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
        double[] result = new double[]{currentVel-0.4, currentVel + 0.4};
        if(result[0] < 0){
            result[0] = 0;
        }
        if(result[1] > 5){
            result[1] = 5;
        }
        return result;
    }

    /**
     * This method will scale a velocity consisting of x and y component such that
     * the resulting velocity will be 5 meters per second.
     * 
     * @param velocities A double array containing the unscaled velocity array
     * @return           A double array containing the scaled velocities such that the
     *                   resulting velocity is 5 m/s
     */
    private double[] scale_velocity(double[] velocities, double velocity) {
        double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
        double scalar = velocity / currentVel;
        return new double[] { velocities[0] * scalar, velocities[1] * scalar };

    }

    /**
     * This method can be viewed as the heuristic function as it calculates the
     * euclidian distance between the position of the ball and the target position.
     * 
     * @param position A double array of length 2 containing the x and y position of
     *                 the ball.
     * @return         A double that describes the euclidian distance of the ball to the
     *                 target.
     */
    private double euclidian_distance(double[] position) {
        return Math.sqrt(Math.pow((targetX - position[0]), 2) + Math.pow((targetY - position[1]), 2));
    }

    /**
     * This method converts a radian angle that is output by the Math.atan2() function
     * and maps it to a degree angle and maps it to the 0-360 degree range.
     * 
     * @param radian Any radian angle
     * @return       The radian angle converted to degrees and mapped to the range 0-360.
     */
    private static double convert(double radian){
        if(radian > 0){
            return Math.toDegrees(radian);
        }
        else{
            return Math.toDegrees(2 * Math.PI + radian);
        }
    }
}
