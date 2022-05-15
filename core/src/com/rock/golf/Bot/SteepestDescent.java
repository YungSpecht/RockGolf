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
        int counter = 0;
        while(currentShotDistance >targetRad && counter < 5){
            mountain_climber(0.5 - (0.1*counter));
            if(currentShotDistance < targetRad){
                return currentShot;
            }
            System.out.println("Iteration: " + ++counter);
        }
        System.out.println("+++SECOND LOOP+++");
        counter = 0;
        while(currentShotDistance >targetRad && counter < 5){
            mountain_climber(0.1 - (0.02*counter));
            if(currentShotDistance < targetRad){
                return currentShot;
            }
            System.out.println("Iteration: " + ++counter);
        }

        return currentShot;
    }

    private void mountain_climber(double precision){
        boolean successorAvailable;
        do{
            double[] successor1 = new double[]{currentShot[0] - precision, currentShot[1]};
            double[] successor2 = new double[]{currentShot[0] + precision, currentShot[1]};
            double[] successor3 = new double[]{currentShot[0], currentShot[1] - precision};
            double[] successor4 = new double[]{currentShot[0], currentShot[1] + precision};

            double[] successor1Coords = null;
            double[] successor2Coords = null;
            double[] successor3Coords = null;
            double[] successor4Coords = null;

            if(vel_is_legal(successor1)){
                successor1Coords = engine.get_shot(successor1[0], successor1[1]);
            }
            if(vel_is_legal(successor2)){
                successor2Coords = engine.get_shot(successor2[0], successor2[1]);
            }
            if(vel_is_legal(successor3)){
                successor3Coords = engine.get_shot(successor3[0], successor3[1]);
            }
            if(vel_is_legal(successor4)){
                successor4Coords = engine.get_shot(successor4[0], successor4[1]);
            }

            int bestSuccessor = compare_successors(successor1Coords, successor2Coords, successor3Coords, successor4Coords);

            if(bestSuccessor > 0){
                switch(bestSuccessor){
                    case 1 : currentShot = successor1; currentShotCoords = successor1Coords; currentShotDistance = euclidian_distance(currentShotCoords); break;
                    case 2 : currentShot = successor2; currentShotCoords = successor2Coords; currentShotDistance = euclidian_distance(currentShotCoords); break;
                    case 3 : currentShot = successor3; currentShotCoords = successor3Coords; currentShotDistance = euclidian_distance(currentShotCoords); break;
                    case 4 : currentShot = successor4; currentShotCoords = successor4Coords; currentShotDistance = euclidian_distance(currentShotCoords); break;
                }
                successorAvailable = true;
            }
            else{
                successorAvailable = false;
            }
            if(currentShotDistance < targetRad){
                return;
            }

        }while(successorAvailable);
    }

    private int compare_successors( double[] one, double[] two, double[] three, double[] four){
        double reference = currentShotDistance;
        int result = 0;
        if(one != null && euclidian_distance(one) < reference){
            result = 1;
            reference = euclidian_distance(one);
            System.out.println("New Shortest Distance: " + reference);
            if(reference < targetRad){
                return result;
            }
        }
        if(two != null && euclidian_distance(two) < reference){
            result = 2;
            reference = euclidian_distance(two);
            System.out.println("New Shortest Distance: " + reference);
            if(reference < targetRad){
                return result;
            }
        }
        if(three != null && euclidian_distance(three) < reference){
            result = 3;
            reference = euclidian_distance(three);
            System.out.println("New Shortest Distance: " + reference);
            if(reference < targetRad){
                return result;
            }
        }
        if(four != null && euclidian_distance(four) < reference){
            result = 4;
            reference = euclidian_distance(four);
            System.out.println("New Shortest Distance: " + reference);
            if(reference < targetRad){
                return result;
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
