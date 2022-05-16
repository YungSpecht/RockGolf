package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

public class SteepestDescent {
    private PhysicsEngine engine;
    private StateVector currentState;
    private double targetX, targetY, targetRad;
    private double[] currentShot;
    private double[] currentShotCoords;
    private double currentShotDistance;

    public SteepestDescent(PhysicsEngine engine){
        double[] input = InputModule.get_input();
        this.engine = engine;
        this.currentState = new StateVector(input[5], input[6], input[7], input[8]);
        targetX = input[2];
        targetY = input[3];
        targetRad = input[4];
    }

    public double[] get_shot(){
        double xdiff = targetX - currentState.getXPos();
        double ydiff = targetY - currentState.getYPos();
        currentShot = scale_velocity(new double[]{xdiff, ydiff}, 4);
        currentShotCoords = engine.get_shot(currentShot[0], currentShot[1]);
        currentShotDistance = euclidian_distance(currentShotCoords);

        System.out.println("+++FIRST LOOP+++");

        int counter = 0;
        while(currentShotDistance >= targetRad && counter < 5){
            mountain_climber(0.5 - (0.1*counter));
            System.out.println("Iteration: " + ++counter);
        }
        System.out.println("+++SECOND LOOP+++");
        counter = 0;
        while(currentShotDistance >targetRad && counter < 4){
            mountain_climber(0.08 - (0.02*counter));
            System.out.println("Iteration: " + ++counter);
        }
        System.out.println("+++THIRD LOOP+++");
        counter = 0;
        while(currentShotDistance >targetRad && counter < 5){
            mountain_climber(0.01 - (0.002*counter));
            System.out.println("Iteration: " + ++counter);
        }
        return currentShot;
    }

    private void mountain_climber(double precision){
        boolean successorAvailable;
        do{
            double[][] successors = new double[4][2];
            successors[0] = new double[]{currentShot[0] - precision, currentShot[1]};
            successors[1] = new double[]{currentShot[0] + precision, currentShot[1]};
            successors[2] = new double[]{currentShot[0], currentShot[1] - precision};
            successors[3] = new double[]{currentShot[0], currentShot[1] + precision};

            double[][] successorCoords = new double[successors.length][2];
            for(int i = 0; i < successors.length; i++){
                if(vel_is_legal(successors[i])){
                    successorCoords[i] = engine.get_shot(successors[i][0], successors[i][1]);
                }
                else{
                    successorCoords[i] = null;
                }
            }
            int bestSuccessor = compare_successors(successorCoords);

            if(bestSuccessor > -1){
                currentShot = successors[bestSuccessor];
                currentShotCoords = successorCoords[bestSuccessor];
                currentShotDistance = euclidian_distance(currentShotCoords);
                successorAvailable = true;
            }else{
                successorAvailable = false;
            }
            if(currentShotDistance < targetRad){
                return;
            }

        }while(successorAvailable);
    }

    private int compare_successors(double[][] successorCoords){
        double reference = currentShotDistance;
        int result = -1;
        for(int i = 0; i < successorCoords.length; i++){
            if(successorCoords[i] != null && euclidian_distance(successorCoords[i]) < reference){
                result = i;
                reference = euclidian_distance(successorCoords[i]);
                System.out.println("New Shortest Distance: " + (reference - targetRad));
                if(reference < targetRad){
                    return result;
                }
            }
        }
        return result;
    }

    private double euclidian_distance(double[] position) {
        return Math.sqrt(Math.pow((targetX - position[0]), 2) + Math.pow((targetY - position[1]), 2));
    }

    private boolean vel_is_legal(double[] velPair){
        return Math.sqrt(Math.pow(velPair[0], 2) + Math.pow(velPair[1], 2)) <= 5.0;
    }
    
    private double[] scale_velocity(double[] velocities, double velocity) {
        double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
        double scalar = velocity / currentVel;
        return new double[] { velocities[0] * scalar, velocities[1] * scalar };
    }
}
