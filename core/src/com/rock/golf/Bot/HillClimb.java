package com.rock.golf.Bot;

import com.rock.golf.Physics.Engine.PhysicsEngine;

public class HillClimb extends Bot {
    private double[] currentShot;
    private double[] currentShotCoords;
    private double currentShotDistance;
    private final double MAX_TEMP;
    private double currentTemp;
    private double coolingRate;

    public HillClimb(PhysicsEngine engine, double MAX_TEMP, double coolingRate) {
        super();
        this.engine = engine;
        this.MAX_TEMP = MAX_TEMP;
        currentTemp = MAX_TEMP;
        this.coolingRate = coolingRate;
    }

    @Override
    public double[] getMove() {
        long checkpoint = System.currentTimeMillis();
        double angle = convert(Math.atan2(targetPos[1] - ballPos[1], targetPos[0] -ballPos[0]));

        double[][][] shots = generate_shot_range(angle, 4, 45, 5, 5, 1);
        currentShot = process_shots(shots, EuclideanDistance(ballPos), 0);
        currentShotCoords = engine.get_shot(currentShot[0], currentShot[1]);
        currentShotDistance = EuclideanDistance(currentShotCoords);
        if(currentShotDistance < targetRadius){
            time = System.currentTimeMillis()-checkpoint;
            return currentShot;
        }

        angle = convert(Math.atan2(currentShot[1], currentShot[0]));
        shots = generate_shot_range(angle, 5, 20, 3, 4.5, 10);
        currentShot = process_shots(shots, currentShotDistance, 0);
        currentShotCoords = engine.get_shot(currentShot[0], currentShot[1]);
        currentShotDistance = EuclideanDistance(currentShotCoords);
        if(currentShotDistance < targetRadius){
            time = System.currentTimeMillis()-checkpoint;
            return currentShot;
        }
        
        int counter = 0;
        while(currentShotDistance >= targetRadius && counter < 3){
            if(counter == 0){
                driver();
            }
            if(engine.is_in_water(currentShotCoords[0], currentShotCoords[1])){
                StochasticBot randomRestart = new StochasticBot(engine, 10);
                currentShot = randomRestart.getMove();
                iterationsCounter += randomRestart.getIterations();
                currentShotCoords = engine.get_shot(currentShot[0], currentShot[1]);
                currentShotDistance = EuclideanDistance(currentShotCoords);
                driver();
            }
            counter++;
        }
       
        time = System.currentTimeMillis()-checkpoint;
        return currentShot;
    }

    private void driver(){
        int counter = 0;
        while (currentShotDistance >= targetRadius && counter < 5) {
            mountain_climber(0.2 - (0.025 * counter));
            System.out.println("LOOP 1 || Iteration: " + ++counter);
        }

        counter = 0;
        while (currentShotDistance >= targetRadius && counter < 4) {
            mountain_climber(0.08 - (0.02 * counter));
            System.out.println("LOOP 2 || Iteration: " + ++counter);
        }

        counter = 0;
        while (currentShotDistance >= targetRadius && counter < 5) {
            mountain_climber(0.01 - (0.002 * counter));
            System.out.println("LOOP 3 || Iteration: " + ++counter);
        }
    }

    private void mountain_climber(double precision) {
        boolean successorAvailable;
        do{
            double[][] successors = new double[8][2];
            successors[0] = new double[]{currentShot[0] - precision, currentShot[1]};
            successors[1] = new double[]{currentShot[0] + precision, currentShot[1]};
            successors[2] = new double[]{currentShot[0], currentShot[1] - precision};
            successors[3] = new double[]{currentShot[0], currentShot[1] + precision};
            successors[4] = new double[]{currentShot[0] - precision, currentShot[1] + precision};
            successors[5] = new double[]{currentShot[0] + precision, currentShot[1] - precision};
            successors[6] = new double[]{currentShot[0] - precision, currentShot[1] - precision};
            successors[7] = new double[]{currentShot[0] + precision, currentShot[1] + precision};

            double[][] successorCoords = new double[successors.length][2];
            for (int i = 0; i < successors.length; i++) {
                if (vel_is_legal(successors[i])) {
                    successorCoords[i] = engine.get_shot(successors[i][0], successors[i][1]);
                    iterationsCounter++;
                } else {
                    successorCoords[i] = null;
                }
            }
            int bestSuccessor = compare_successors(successorCoords);

            if (bestSuccessor != -1) {
                currentShot = successors[bestSuccessor];
                currentShotCoords = successorCoords[bestSuccessor];
                currentShotDistance = EuclideanDistance(currentShotCoords);
                successorAvailable = true;
            } else {
                successorAvailable = false;
            }
            if (currentShotDistance < targetRadius) {
                return;
            }
            if(currentTemp > 0){
                currentTemp -= coolingRate;
                System.out.println("Current Temperature: " + currentTemp);
                if(currentTemp < 0){
                    currentTemp = 0;
                }
            }

        } while (successorAvailable);
    }

    private int compare_successors(double[][] successorCoords) {
        double reference = currentShotDistance;
        int result = -1;
        for (int i = 0; i < successorCoords.length; i++) {
            if (successorCoords[i] != null && !engine.is_in_water(successorCoords[i][0], successorCoords[i][0]) && annealing(successorCoords[i], reference)) {
                result = i;
                reference = EuclideanDistance(successorCoords[i]);
                System.out.println("New Shortest Distance: " + (reference - targetRadius));
                if (reference < targetRadius) {
                    return result;
                }
            }
        }
        return result;
    }

    private boolean annealing(double[] coords, double refDistance){
        if(EuclideanDistance(coords) < refDistance){
            return true;
        }
        double control = currentTemp / (MAX_TEMP * (EuclideanDistance(coords)*12 - refDistance));
        double rando = rand.nextDouble();
        return rando < control;
    }

}
