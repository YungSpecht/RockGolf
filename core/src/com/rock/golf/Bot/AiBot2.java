package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class AiBot2 {
    private PhysicsEngine engine;
    private StateVector currentState;
    private double targetX, targetY, targetRad;
    private double[] bestShot;
    private double[] bestShotCoords;
    private double bestShotDistance;
    private double bestShotAngle;

    // constructor
    public AiBot2(PhysicsEngine engine) {
        double[] input = InputModule.get_input();
        this.engine = engine;
        this.currentState = new StateVector(input[5], input[6], input[7], input[8]);
        targetX = input[2];
        targetY = input[3];
        targetRad = input[4];
    }

    public double[] get_shot(){
        long time = System.currentTimeMillis();
        bestShot = new double[2];
        bestShotCoords = new double[] { currentState.getXPos(), currentState.getYPos() };
        bestShotDistance = euclidian_distance(bestShotCoords);
        bestShotAngle = convert(Math.atan2(targetY - bestShotCoords[1], targetX - bestShotCoords[0]));

        double[][][] shots = generate_shot_range(bestShotAngle, 3, 45, 5, 5, 1);
        process_shots(shots, 0);
        System.out.println("First best Shot: " +(bestShotDistance-targetRad));
        if(bestShotDistance < targetRad){
            System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
            return bestShot;
        }
        
        shots = generate_shot_range(bestShotAngle, 5, 20, 3.5, 4.5, 10);
        process_shots(shots, 0.15);

        if(bestShotDistance < targetRad){
            System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
            return bestShot;
        }

        double[] velRange = get_velocity_range(bestShot, 0.1);
        shots = generate_shot_range(bestShotAngle, 5, 5, velRange[0], velRange[1], 10);
        process_shots(shots, 0);

        if(bestShotDistance < targetRad){
            System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
            return bestShot;
        }

        double rangeBackX = 0.3;
        double rangeForwardX = 0.3;
        double rangeBackY = 0.3;
        double rangeForwardY = 0.3;
        double prezi = 0.1;

        int iteration = 0;
        while (bestShotDistance >= targetRad && iteration < 5) {
            int counter = 0;
            prezi *= 0.8;
            boolean abort = false;
            for (double i = bestShot[0] - rangeBackX; i < bestShot[0] + rangeForwardX; i += prezi) {
                for (double j = bestShot[1] - rangeBackY; j < bestShot[1] + rangeForwardY; j += prezi) {
                    double[] shotCoords = engine.get_shot(i, j);
                    double distance = euclidian_distance(shotCoords);
                    if (distance < bestShotDistance) {
                        counter = 0;
                    } else {
                        counter++;
                    }
                    compare_shot(new double[]{i, j}, shotCoords, distance);
                    if(bestShotDistance < targetRad){
                        System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
                        return bestShot;
                    }
                    if (counter > 20) {
                        abort = true;
                        break;
                    }
                }
                if (abort) {
                    break;
                }
            }
            System.out.println("Iteration: " + ++iteration);
        }
        System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");

        return bestShot;
    }

    private double[][][] generate_shot_range(double currentAngle, int divergentShots, double outermostAngle,
            double velocityStart, double velocityEnd, int velAmount) {
        double[][][] result = new double[1 + 2 * divergentShots][velAmount][2];
        for (int i = 0; i < velAmount; i++) {
            if (velAmount == 1)
                result[0][0] = get_velocity(currentAngle, velocityStart);
            else
                result[0][i] = get_velocity(currentAngle,
                        velocityStart + i * ((velocityEnd - velocityStart) / (velAmount - 1)));
        }
        for (int i = 1; i < result.length; i = i + 2) {
            for (int j = 0; j < result[0].length; j++) {
                if (velAmount == 1) {
                    result[i][j] = get_velocity(currentAngle + ((i + 1) / 2) * (outermostAngle / divergentShots),
                            velocityStart);
                    result[i + 1][j] = get_velocity(currentAngle - ((i + 1) / 2) * (outermostAngle / divergentShots),
                            velocityStart);
                } else {
                    result[i][j] = get_velocity(currentAngle + ((i + 1) / 2) * (outermostAngle / divergentShots),
                            velocityStart + j * ((velocityEnd - velocityStart) / (velAmount - 1)));
                    result[i + 1][j] = get_velocity(currentAngle - ((i + 1) / 2) * (outermostAngle / divergentShots),
                            velocityStart + j * ((velocityEnd - velocityStart) / (velAmount - 1)));
                }
            }
        }
        return result;
    }

    private void process_shots(double[][][] shots, double instantReturn) {
        for (int i = 0; i < shots[0].length; i++) {
            double[] shotCoords = engine.get_shot(shots[0][i][0], shots[0][i][1]);
            double distance = euclidian_distance(shotCoords);
            compare_shot(shots[0][i], shotCoords, distance);
        }
        for (int i = 1; i < shots.length; i = i + 2) {
            for (int j = 0; j < shots[0].length; j++) {
                double[] leftShotCoords = engine.get_shot(shots[i][j][0], shots[i][j][1]);
                double leftDist = euclidian_distance(leftShotCoords);
                compare_shot(shots[i][j], leftShotCoords, leftDist);
                if (bestShotDistance < targetRad || bestShotDistance < (instantReturn + targetRad)) {
                    return;
                }

                double[] rightShotCoords = engine.get_shot(shots[i + 1][j][0], shots[i + 1][j][1]);
                double rightDist = euclidian_distance(rightShotCoords);
                compare_shot(shots[i + 1][j], rightShotCoords, rightDist);
                if (bestShotDistance < targetRad || bestShotDistance < (instantReturn + targetRad)) {
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

    private void compare_shot(double[] shot, double[] shotCoords, double distance) {
        if (distance < bestShotDistance && !engine.is_in_water(shotCoords)) {
            bestShot = shot;
            bestShotCoords = shotCoords;
            bestShotDistance = distance;
            bestShotAngle = convert(Math.atan2(bestShot[1], bestShot[0]));
            System.out.println("Current shortest Distance to Target: " + (bestShotDistance - targetRad));
        }
    }

    /**
     * This method will generate a shot in the direction of the specified angle with
     * the specified velocity.
     * 
     * @param angle    The angle the shot is supposed to follow.
     * @param velocity The velocity with which the ball is suppoded to be shot.
     * @return A double array containing the x and y velocity for the desired shot.
     */

    private double[] get_velocity(double angle, double velocity) {
        double[] result = new double[] { Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)) };
        return scale_velocity(result, velocity);
    }

    /**
     * This method will create a velocity range around the input velocity.
     * 
     * @param velocity A double array containing the current x and y velocity.
     * @return A double array containing a start- and endpoint of the
     *         velocity range in the range [0.0-5.0].
     */

    private double[] get_velocity_range(double[] velocity, double range) {
        double currentVel = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
        double[] result = new double[] { currentVel - range, currentVel + range };
        if (result[0] < 0) {
            result[0] = 0;
        }
        if (result[1] > 5) {
            result[1] = 5;
        }
        return result;
    }

    /**
     * This method will scale a velocity consisting of x and y component such that
     * the resulting velocity will be 5 meters per second.
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
     * euclidian distance between the position of the ball and the target position.
     * 
     * @param position A double array of length 2 containing the x and y position of
     *                 the ball.
     * @return A double that describes the euclidian distance of the ball to the
     *         target.
     */

    private double euclidian_distance(double[] position) {
        return Math.sqrt(Math.pow((targetX - position[0]), 2) + Math.pow((targetY - position[1]), 2));
    }

    /**
     * This method converts a radian angle that is output by the Math.atan2()
     * function
     * and maps it to a degree angle and maps it to the 0-360 degree range.
     * 
     * @param radian Any radian angle
     * @return The radian angle converted to degrees and mapped to the range 0-360.
     */
    
    private static double convert(double radian) {
        if (radian > 0) {
            return Math.toDegrees(radian);
        } else {
            return Math.toDegrees(2 * Math.PI + radian);
        }
    }
}
