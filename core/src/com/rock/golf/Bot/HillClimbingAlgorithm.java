package com.rock.golf.Bot;

import javax.swing.undo.StateEdit;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class HillClimbingAlgorithm {

    private double targetX, targetY, ballPositionX, ballPositionY, xVelocity, yVelocity, targetRadius;
    double[] input;

    public HillClimbingAlgorithm() {
        input = InputModule.get_input();
        targetX = input[2];
        targetY = input[3];
        targetRadius = input[4];
        ballPositionX = input[5];
        ballPositionY = input[6];
        xVelocity = input[7];
        yVelocity = input[8];
    }

    private double getDistance(StateVector vector) {
        return Math.sqrt(Math.pow((targetX - vector.getXPos()), 2) + Math.pow((targetY - vector.getYPos()), 2));
    }

    private StateVector currentStateShot(StateVector vector) {
        ballPositionX += getDistance(vector) * 0.1;
        ballPositionY += getDistance(vector) * 0.1;
        return new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity);
    }

    private boolean ball_in_target(StateVector vector) {
        if(Math.pow(vector.getXPos() - targetX, 2) + Math.pow(vector.getYPos() - targetY, 2) <= Math.pow(targetRadius, 2)){
            return true;
        }
        return false;
    }

    private StateVector update_state(StateVector vector) {
        StateVector newState = currentStateShot(vector);
        double xPos = newState.getXPos();
        double yPos = newState.getYPos();
        if (getDistance(newState) < getDistance(new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity))) {
            ballPositionX = xPos;
            ballPositionY = yPos;
        }
        return new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity);
    }

    public StateVector getTrajectoryWithHillClimb(StateVector vector) {
        // TODO, If ball gets stuck in local minima, aka water
        StateVector newState = currentStateShot(vector);

        while (!ball_in_target(newState)) {
            newState = update_state(newState);
        }
        xVelocity = 0;
        yVelocity = 0;
        return newState;

    }

}
