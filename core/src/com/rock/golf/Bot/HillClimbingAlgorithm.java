package com.rock.golf.Bot;

import com.rock.golf.Input.InputModule;

public class HillClimbingAlgorithm {

    private double targetX, targetY, ballPositionX, ballPositionY, ballRadius, targetRadius;
    double[] input= InputModule.get_input();

    public double getHeuristics(){
        
        targetX = input[2];
        targetX = input[3];
        ballPositionX = input[5];
        ballPositionY = input[6];

        double distance = Math.sqrt(Math.pow((targetX - ballPositionX), 2) + Math.pow((targetY - ballPositionY),2));
        
        return distance;
    }

    private double newPosition(double positionX, double positionY){
        return newPosition(positionX, positionY);
        
    }

    private double perform_shot(){
        ballPositionX = input[5] * getHeuristics();
        ballPositionY = input[6] * getHeuristics();
        return newPosition(ballPositionX, ballPositionY);
    }

    
    private boolean ball_in_target(){
        targetRadius = input[4];
        ballRadius = 0.5;
        boolean xCheck = ballPositionX + ballRadius < targetX + targetRadius && ballPositionX - ballRadius > targetX - targetRadius;
        boolean yCheck = ballPositionY + ballRadius < targetY + targetRadius && ballPositionY - ballRadius > targetY - targetRadius;
        return xCheck && yCheck;
    }

    public double getTrajectoryWithHillClimb(){
        //TODO, If ball gets stuck in local minima, aka water
        double currentBallPosition = perform_shot();
        if(!ball_in_target()){
            double newShot = perform_shot() * 0.1;
            // if(newShot != target){
            //     perform_shot();
            // }
        }
        return currentBallPosition;
    }
}

   

