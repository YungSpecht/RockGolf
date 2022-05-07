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

    public double[] get_shot() {
        double xDistance = targetX - currentState.getXPos();
        double yDistance = targetY - currentState.getYPos();

        double[] currentBestShot = new double[2];
        double[] currentBestShotCoords = new double[2];

        double angleAtTarget = Math.atan2(xDistance, yDistance);
        double distanceToTarget = Double.MAX_VALUE;

        do{
            System.out.println("ITERATION");
            double[][] shots = generate_shots(angleAtTarget);
            double[] shotOne = engine.get_shot(shots[0][0], shots[0][1]);
            double[] shotTwo = engine.get_shot(shots[1][0], shots[1][1]);
            double distOne = euclidian_distance(engine.get_shot(shots[0][0], shots[0][1]));
            double distTwo = euclidian_distance(engine.get_shot(shots[1][0], shots[1][1]));

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
            xDistance = targetX - currentBestShotCoords[0];
            yDistance = targetY - currentBestShotCoords[1];
            angleAtTarget = Math.atan2(xDistance, yDistance);
        }while(distanceToTarget >= targetRad);
        return currentBestShot;
    }

    public static double[] scale_velocity(double[] velocities) {
        double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
        double scalar = 5 / currentVel;
        return new double[]{velocities[0] * scalar, velocities[1] * scalar};

    }

    private double[][] generate_shots(double angle) {
        double[][] result = new double[2][2];

        result[0] = new double[]{Math.cos(angle - 0.3), Math.sin(angle - 0.3)};
        result[1] = new double[]{Math.cos(angle + 0.3), Math.sin(angle + 0.3)};

        result[0] = scale_velocity(result[0]);
        result[1] = scale_velocity(result[1]);

        return result;
    }

    private double euclidian_distance(double[] position) {
        return Math.sqrt(Math.pow((targetX - position[0]), 2) + Math.pow((targetY - position[1]), 2));
    }

    private boolean ball_in_target(double[] position){
        if(Math.pow(position[0] - targetX, 2) + Math.pow(position[1] - targetY, 2) <= Math.pow(targetRad, 2)){
            return true;
        }
        return false;
    }
}
