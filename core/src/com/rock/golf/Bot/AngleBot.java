package com.rock.golf.Bot;

import com.rock.golf.Physics.Engine.PhysicsEngine;

/**
 * Angle Bot
 */

public class AngleBot extends Bot {
    private double[] bestShot;
    private double[] bestShotCoords;
    private double bestShotDistance;
    private double bestShotAngle;

    public AngleBot(PhysicsEngine engine) {

        super();
        this.engine = engine;
    }

    @Override

    /**
     * Inherited abstract class from super
     */

    public double[] getMove() {

        long checkpoint = System.currentTimeMillis();
        bestShotAngle = convert(Math.atan2(targetPos[1] - ballPos[1], targetPos[0] - ballPos[0]));
        bestShotDistance = EuclideanDistance(ballPos);

        double[][][] shots = GenerateShotRange(bestShotAngle, 5, 40, 4, 5, 5);
        bestShot = processShots(shots, bestShotDistance, 0.2);
        bestShotCoords = engine.getSimulatedShot(bestShot[0], bestShot[1]);
        iterationsCounter++;
        bestShotDistance = EuclideanDistance(bestShotCoords);
        if (bestShotDistance < targetRadius) {
            time = System.currentTimeMillis() - checkpoint;
            return bestShot;
        }

        int counter = 0;
        while (bestShotDistance >= targetRadius && counter < 3) {
            bestShotAngle = convert(Math.atan2(bestShot[1], bestShot[0]));
            double[] velRange = getVelocityRange(bestShot, 0.4 - (counter * 0.05));

            shots = GenerateShotRange(bestShotAngle, 10, 30 - ((1 + counter) * 7.5), velRange[0], velRange[1],
                    10 + ((counter + 1) * 4));
            bestShot = processShots(shots, bestShotDistance, 0);
            bestShotCoords = engine.getSimulatedShot(bestShot[0], bestShot[1]);
            iterationsCounter++;
            bestShotDistance = EuclideanDistance(bestShotCoords);

            if (bestShotDistance < targetRadius) {
                time = System.currentTimeMillis() - checkpoint;
                return bestShot;
            }
            System.out.println("Iteration: " + ++counter);
        }
        time = System.currentTimeMillis() - checkpoint;
        return bestShot;
    }
}
