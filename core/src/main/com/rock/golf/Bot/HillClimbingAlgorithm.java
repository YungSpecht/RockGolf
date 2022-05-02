package main.com.rock.golf.Bot;

import main.com.rock.golf.Input.InputModule;

public class HillClimbingAlgorithm {

    private double targetX, targetY, ballPositionX, ballPositionY;
    double[] input= InputModule.get_input();

    public double getHeuristics(){
        
        targetX = input[2];
        targetX = input[3];
        ballPositionX = input[5];
        ballPositionY = input[6];

        double distance = Math.sqrt(Math.pow((targetX - ballPositionX), 2) + Math.pow((targetY - ballPositionY),2));
        
        return distance;
    }


    private double currentStateShot(){
        ballPositionX = input[5] * getHeuristics();
        ballPositionY = input[6] * getHeuristics();
        double distance = Math.sqrt(Math.pow((targetX - ballPositionX), 2) + Math.pow((targetY - ballPositionY),2));
        return distance;
    }

    
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

    // private boolean ball_in_target(){
    //     targetRadius = input[4];
    //     ballRadius = 0.5;
    //     boolean xCheck = ballPositionX + ballRadius < targetX + targetRadius && ballPositionX - ballRadius > targetX - targetRadius;
    //     boolean yCheck = ballPositionY + ballRadius < targetY + targetRadius && ballPositionY - ballRadius > targetY - targetRadius;
    //     return xCheck && yCheck;
    // }
}

   

