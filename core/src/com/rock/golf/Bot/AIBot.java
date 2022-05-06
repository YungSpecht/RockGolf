package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class AIBot {
    private PhysicsEngine engine;
    private StateVector currentState;
    private double targetX, targetY, targetRad, xVel, yVel;

    public AIBot(PhysicsEngine engine, StateVector currentState) {
        double[] input = InputModule.get_input();
        this.engine = engine;
        this.currentState = currentState;
        targetX = input[3];
        targetY = input[4];
        targetRad = input[5];
        xVel = input[7]; // if we need it
        yVel = input[8]; // if we need it
    }

    public StateVector get_shot() {
        double xDistance = targetX - currentState.getXPos();
        double yDistance = targetY - currentState.getYPos();
        double[] shotAtTarget = scale_velocity(new double[] { xDistance, yDistance });
        while(true){
            double[][] shots = generate_shots(shotAtTarget);
            double comparison = Double.MAX_VALUE;
            int bestShot = 99999;
            for(int i = 0; i < shots.length; i++){
                double[] position = engine.get_shot(shots[i][0], shots[i][1]);
                double distance = euclidian_distance(position);
                if(distance < comparison){
                    comparison = distance;
                    bestShot = i;
                }
            }
            shotAtTarget = shots[bestShot];
        }

    }

    private double[] scale_velocity(double[] velocities) {
        if (Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2)) < 5) {
            while (Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2)) < 5) {
                velocities[0] *= 1.1;
                velocities[1] *= 1.1;
            }
            return velocities;
        } else if (Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2)) > 5) {
            while (Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2)) > 5) {
                velocities[0] *= 0.9;
                velocities[1] *= 0.9;
            }
            return velocities;
        }
        return velocities;

    }

    private double[][] generate_shots(double[] shot) {
        double[][] result = new double[4][2];
        result[0] = new double[]{shot[0] * 1.1, shot[1]};
        result[1] = new double[]{shot[0] * 0.9, shot[1]};
        result[2] = new double[]{shot[0], shot[1] * 1.1};
        result[3] = new double[]{shot[0], shot[1] * 0.9};
        return result;
    }

    private double euclidian_distance(double[] position) {
        return Math.sqrt(Math.pow((targetX - position[0]), 2) + Math.pow((targetY - position[1]), 2));
    }
}
