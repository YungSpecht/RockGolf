package com.rock.golf.Bot;

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
	 * This method ist the one that gets called from outside to find x and y velocities that will result
     * in the ball going into the target.
     * 
     * @return A double array of length 2 where the first index is the x velocity and the second
     * index is the y velocity
	 */
    public double[] get_shot() {// here we find out the x and y distance from the ball's current position to the target
        double xDistance = targetX - currentState.getXPos();
        double yDistance = targetY - currentState.getYPos();

        // based on the previously calculated distances we calculate the angle to the target
        double angleAtTarget = Math.atan2(xDistance, yDistance);
        double[] currentBestShot = scale_velocity(new double[]{xDistance, yDistance});
        double[] currentBestShotCoords = engine.get_shot(currentBestShot[0], currentBestShot[1]);
        double distanceToTarget = euclidian_distance(currentBestShotCoords);

        if(euclidian_distance(currentBestShotCoords) < targetRad){
            return currentBestShot;
        }

        do{
            // here we gerate two shots that diverge from the angle to the target slightly and we see where they end up
            double[][] shots = generate_shots(angleAtTarget, currentBestShotCoords, distanceToTarget);
            double[] shotOne = engine.get_shot(shots[0][0], shots[0][1]);
            double[] shotTwo = engine.get_shot(shots[1][0], shots[1][1]);
            double distOne = euclidian_distance(shotOne);
            double distTwo = euclidian_distance(shotTwo);

            // here we see which of the shots ended up closest to the target and set the variables accordingly
            if(distOne < distTwo){
                currentBestShot = shots[0];
                currentBestShotCoords = shotOne;
                distanceToTarget = distOne;
            }
            else{
                currentBestShot = shots[1];
                currentBestShotCoords = shotTwo;
                distanceToTarget = distTwo;
            }

            // since we now have a new best shot we calculate the angle of that shot as the new reference angle for
            // our next two shots
            xDistance = targetX - currentBestShotCoords[0];
            yDistance = targetY - currentBestShotCoords[1];
            angleAtTarget = Math.atan2(xDistance, yDistance);
        }while(distanceToTarget >= targetRad);
        return currentBestShot;
    }

    /**
	 * This method will scale a velocity consisting of x and y component such that the resulting velocity will be
     * 5 meters per second.
     * 
     * @param velocities A double array containing the unscaled velocity array
     * @return A double array containing the scaled velocities such that the resulting velocity is 5 m/s
	 */
    private double[] scale_velocity(double[] velocities) {
        double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
        double scalar = 5 / currentVel;
        return new double[]{velocities[0] * scalar, velocities[1] * scalar};

    }

    /**
	 * Based on an angle that is given as a reference this method will generate two shots, where one shot
     * is performed at a slightly bigger and the other shot at a slightly smaller angle.
     * 
     * @param angle An angle that is given as a reference, usually the angle of the previously best shot.
     * @return A double array containing two shots that slightly diverge in both directions from the reference
     * angle
	 */
    private double[][] generate_shots(double angle, double[] currentBestShotCoords, double distanceToTarget) {
        double[][] result = new double[2][2];

        result[0] = new double[]{Math.cos(angle - 0.3), Math.sin(angle - 0.3)};
        result[1] = new double[]{Math.cos(angle + 0.3), Math.sin(angle + 0.3)};

        result[0] = scale_velocity(result[0]);
        result[1] = scale_velocity(result[1]);

        return result;
    }

    private double calculate_divergence(double[] currentBestShotCoords, double distanceToTarget){
        double distanceFromStart = Math.sqrt(Math.pow(currentBestShotCoords[0] - currentState.getXPos(), 2) + Math.pow(currentBestShotCoords[1] - currentState.getYPos(), 2));
        double finalAngleChange = 1 - (distanceToTarget);
        return finalAngleChange;
    }

    /**
	 * This method can be viewed as the heuristic function as it calculates the euclidian distance between the
     * position of the ball and the target position.
     * 
     * @param position A double array of length 2 containing the x and y position of the ball.
     * @return A double that describes the euclidian distance of the ball to the target.
	 */
    private double euclidian_distance(double[] position) {
        return Math.sqrt(Math.pow((targetX - position[0]), 2) + Math.pow((targetY - position[1]), 2));
    }
}
