package com.rock.golf.Bot;

import java.util.Arrays;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class AIBot {
    private PhysicsEngine engine;
    private StateVector currentState;
    private double targetX, targetY, targetRad;

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
     * velocities that will result
     * in the ball going into the target.
     * 
     * @return A double array of length 2 where the first index is the x velocity
     *         and the second
     *         index is the y velocity
     */
    public double[] get_shot() {
        double xDistance = targetX - currentState.getXPos();
        double yDistance = targetY - currentState.getYPos();
        double angleAtTarget = convert(Math.atan2(yDistance, xDistance));

        double[] bestShot = new double[2];
        double[] bestShotCoords = new double[]{currentState.getXPos(), currentState.getYPos()};
        double bestShotDistance = euclidian_distance(bestShotCoords);

        while(bestShotDistance >= targetRad){
            double[][][] shotsArray = generate_shots(angleAtTarget, 5, 45, 10, 2, 5);
            double currentBestDistance = Double.MAX_VALUE;
            for(int i = 0; i < shotsArray.length; i++){
                for(int j = 0; j < shotsArray[0].length; j++){
                    double distance = euclidian_distance(engine.get_shot(shotsArray[i][j][0], shotsArray[i][j][1]));
                    System.out.println(distance);
                    if(distance < currentBestDistance){
                        bestShot = shotsArray[i][j];
                        currentBestDistance = distance;
                    }
                }
            }
        }
        return bestShot;
    }

    /**
     * Based on an angle that is given as a reference this method will generate two
     * shots, where one shot
     * is performed at a slightly bigger and the other shot at a slightly smaller
     * angle.
     * 
     * @param angle An angle that is given as a reference, usually the angle of the
     *              previously best shot.
     * @return A double array containing two shots that slightly diverge in both
     *         directions from the reference
     *         angle
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

    private double[] get_velocity(double angle, double velocity){
        double[] result = new double[]{Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))};
        return scale_velocity(result, velocity);
    }

    /**
     * This method will scale a velocity consisting of x and y component such that
     * the resulting velocity will be
     * 5 meters per second.
     * 
     * @param velocities A double array containing the unscaled velocity array
     * @return A double array containing the scaled velocities such that the
     *         resulting velocity is 5 m/s
     */
    private double[] scale_velocity(double[] velocities, double velocity) {
        double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
        double scalar = velocity / currentVel;
        return new double[] { velocities[0] * scalar, velocities[1] * scalar };

    }

    /**
     * This method can be viewed as the heuristic function as it calculates the
     * euclidian distance between the
     * position of the ball and the target position.
     * 
     * @param position A double array of length 2 containing the x and y position of
     *                 the ball.
     * @return A double that describes the euclidian distance of the ball to the
     *         target.
     */
    private double euclidian_distance(double[] position) {
        return Math.sqrt(Math.pow((targetX - position[0]), 2) + Math.pow((targetY - position[1]), 2));
    }

    private static double convert(double radian){
        if(radian > 0){
            return Math.toDegrees(radian);
        }
        else{
            return Math.toDegrees(2 * Math.PI + radian);
        }
    }
}
