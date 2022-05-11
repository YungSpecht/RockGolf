package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class AIBot {
    private PhysicsEngine engine;
    private StateVector currentState;
    private double targetX, targetY, targetRad;

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

        double[] bestShot = new double[2];
        double[] bestShotCoords = new double[]{currentState.getXPos(), currentState.getYPos()};
        double bestShotDistance = euclidian_distance(bestShotCoords);
        double shotAngle = convert(Math.atan2(targetY - bestShotCoords[0], targetX - bestShotCoords[1]));

        double[][][] shotsArray = generate_shots(shotAngle, 5, 45, 10, 2, 5);
        double[][][] shotCoordSArray = new double[shotsArray.length][shotsArray[0].length][2];
        for(int i = 0; i < shotsArray.length; i++){
            for(int j = 0; j < shotsArray[0].length; j++){
                shotCoordSArray[i][j] = engine.get_shot(shotsArray[i][j][0], shotsArray[i][j][1]);
                double distance = euclidian_distance(shotCoordSArray[i][j]);
                if(distance < bestShotDistance){
                    bestShot = shotsArray[i][j];
                    bestShotCoords = shotCoordSArray[i][j];
                    bestShotDistance = distance;
                    System.out.println("Current shortest Distance to Target: " + bestShotDistance);
                }
            }
        }
        System.out.println("Intermediate checkpoint: " + bestShotDistance);
        
        int counter = 0;
        while(bestShotDistance >= targetRad && counter < 10){
            shotAngle = convert(Math.atan2(bestShot[1], bestShot[0]));
            double[] velRange = get_velocity_range(bestShot);
            shotsArray = generate_shots(shotAngle, 5, 45 - ((1 + counter) * 5), 10 + ((counter + 1) * 5), velRange[0], velRange[1]);
            shotCoordSArray = new double[shotsArray.length][shotsArray[0].length][2];
            for(int i = 0; i < shotsArray.length; i++){
                for(int j = 0; j < shotsArray[0].length; j++){
                    shotCoordSArray[i][j] = engine.get_shot(shotsArray[i][j][0], shotsArray[i][j][1]);
                    double distance = euclidian_distance(shotCoordSArray[i][j]);
                    if(distance < bestShotDistance){
                        bestShot = shotsArray[i][j];
                        bestShotCoords = shotCoordSArray[i][j];
                        bestShotDistance = distance;
                        System.out.println("Current shortest Distance to Target: " + bestShotDistance);
                    }
                }
            }
            System.out.println("Iteration: " + ++counter);
        }
        return bestShot;
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
        double[] result = new double[]{currentVel-0.5, currentVel + 0.5};
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
