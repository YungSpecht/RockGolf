package com.rock.golf.Bot;

import javax.swing.undo.StateEdit;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class HillClimbingAlgorithm {


    private double targetX, targetY, ballPositionX, ballPositionY, xVelocity, yVelocity;
    double[] input;


    public HillClimbingAlgorithm(){
        input = InputModule.get_input();

    public double getTrajectoryWithHillClimb(){
        //TODO, If ball gets stuck in local minima, aka water
        double currentState = getHeuristics();
        double newState = currentStateShot();

        while(currentState != 0){
            if(newState < currentState){
                currentState = newState;
            }
            newState *= 0.1;
        }
        return currentState;

    }

    public double getHeuristics(){
        
        targetX = input[2];

        targetX = input[3];
        ballPositionX = input[5];
        ballPositionY = input[6];
        xVelocity = input[7];
        yVelocity = input[8];
    }

    private double getHeuristics(){
        return Math.sqrt(Math.pow((targetX - ballPositionX), 2) + Math.pow((targetY - ballPositionY),2));
    }


    private StateVector currentStateShot(){
        ballPositionX *= getHeuristics();
        ballPositionY *= getHeuristics();
        return new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity);
    }

<<<<<<< HEAD
    private StateVector ball_in_target(){
        return null;
    }


    private StateVector update_state(){
        StateVector newState = currentStateShot();
        double xPos = newState.getXPos();
        double yPos = newState.getYPos();
        if((targetX - xPos) < (targetX - ballPositionX) && (targetY - yPos) < (targetY - ballPositionY)){
            ballPositionX = xPos;
            ballPositionY = yPos;
        }
        return new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity);
    }
    
    public StateVector getTrajectoryWithHillClimb(){
        //TODO, If ball gets stuck in local minima, aka water
        StateVector currentState = new StateVector(ballPositionX, ballPositionY, xVelocity, yVelocity);
        StateVector newState = currentStateShot();

        while(newState != ball_in_target()){
            currentState = update_state();
        }

        xVelocity = 0;
        yVelocity = 0;
        return currentState;
        

    }

=======
>>>>>>> 2145fa193bac8a54099897857e3af095f3887a36

}

   

