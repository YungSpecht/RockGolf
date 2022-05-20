package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;

public class AIBot extends Bot{
    private double[] bestShot;
    private double[] bestShotCoords;
    private double bestShotDistance;
    private double bestShotAngle;

    // constructor
    public AIBot(PhysicsEngine engine) {
        super();
        this.engine = engine;
    }

    @Override
    public double[] getMove() {
        long time = System.currentTimeMillis();
        bestShotAngle = convert(Math.atan2(targetPos[1] - ballPos[1], targetPos[0] - ballPos[0]));
        bestShotDistance = EuclideanDistance(ballPos);

        double[][][] shots = generate_shot_range(bestShotAngle, 4, 45, 5, 5, 1);
        evaluate(shots, 0.2);
        if(bestShotDistance < targetRadius){
            System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
            return bestShot;
        }
        
        shots = generate_shot_range(bestShotAngle, 5, 20, 3.5, 4.5, 10);
        evaluate(shots, 0.1);
        if(bestShotDistance < targetRadius){
            System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
            return bestShot;
        }

        double[] velRange = get_velocity_range(bestShot, 0.2);
        shots = generate_shot_range(bestShotAngle, 5, 5, velRange[0], velRange[1], 10);
        evaluate(shots, 0.05);
        if(bestShotDistance < targetRadius){
            System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
            return bestShot;
        }

        double rangeBackX = 0.3;
        double rangeForwardX = 0.3;
        double rangeBackY = 0.3;
        double rangeForwardY = 0.3;
        double prezi = 0.1;

        int iteration = 0;
        while (bestShotDistance >= targetRadius && iteration < 5) {
            prezi *= 0.8;
            for (double i = bestShot[0] - rangeBackX; i < bestShot[0] + rangeForwardX; i += prezi) {
                for (double j = bestShot[1] - rangeBackY; j < bestShot[1] + rangeForwardY; j += prezi) {
                    double[] shotCoords = engine.get_shot(i, j);
                    double distance = EuclideanDistance(shotCoords);
                    if(distance < bestShotDistance && !engine.is_in_water(shotCoords[0], shotCoords[1])){
                        bestShot = new double[]{i, j};
                        bestShotCoords = shotCoords;
                        bestShotDistance = distance;
                        System.out.println("New Shortest Distance: " + (bestShotDistance - targetRadius));
                    }
                    if(bestShotDistance < targetRadius){
                        System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
                        return bestShot;
                    }
                }
            }
            iteration++;
        }
        System.out.println("Shot found in " + (System.currentTimeMillis()-time) + "ms");
        return bestShot;
    }

    private void evaluate(double[][][] shots, double instantReturn){
        bestShot = process_shots(shots, bestShotDistance, instantReturn);
        bestShotCoords = engine.get_shot(bestShot[0], bestShot[1]);
        bestShotDistance = EuclideanDistance(bestShotCoords);
        bestShotAngle = convert(Math.atan2(bestShot[1], bestShot[0]));
    }

}
