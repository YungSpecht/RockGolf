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

    /**
     * This method is the heuristic function for our hill climbing algorithm
     * Our heuristics function is the distance between ball position and target
     * position
     */
    private double get_Distance(StateVector vector) {
        return Math.sqrt(Math.pow((targetX - vector.getXPos()), 2) + Math.pow((targetY - vector.getYPos()), 2));
    }

    /**
     * Uses heuristic function to update state of ball position after one shot
     */

    private StateVector current_State_Shot(StateVector vector) {
        ballPositionX += get_Distance(vector) * 0.1;
        ballPositionY += get_Distance(vector) * 0.1;
        return new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity);
    }

    /**
     * This method determines wether the ball is currently inside the target based
     * on the position and
     * radius of the ball as well as the target.
     * 
     * @return Boolean value: true if ball is inside target, false if not.
     */
    
    private boolean ball_in_target(StateVector vector) {
        if (Math.pow(vector.getXPos() - targetX, 2) + Math.pow(vector.getYPos() - targetY, 2) <= Math.pow(targetRadius,
                2)) {
            return true;
        }
        return false;
    }

    private StateVector update_state(StateVector vector) {
        StateVector newState = current_State_Shot(vector);
        double xPos = newState.getXPos();
        double yPos = newState.getYPos();
        if (get_Distance(newState) < get_Distance(
                new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity))) {
            ballPositionX = xPos;
            ballPositionY = yPos;
        }
        return new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity);
    }

    public StateVector get_Trajectory_With_HillClimb(StateVector vector) {
        // TODO, If ball gets stuck in local minima, aka water
        StateVector newState = current_State_Shot(vector);

        while (!ball_in_target(newState)) {
            newState = update_state(newState);
        }
        xVelocity = 0;
        yVelocity = 0;
        return newState;
    }
}
