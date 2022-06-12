package com.rock.golf.Bot;

import com.rock.golf.Physics.Engine.PhysicsEngine;

/**
 * BruteForce Bot
 */

public class Bruteforce extends Bot {
    double precision;
    long checkpoint;

    public Bruteforce(PhysicsEngine engine, double precision) {
        this.engine = engine;
        this.precision = precision;
    }

    /**
     *
     * Check if the current move is considered an hole
     *
     * @return null / hole velocities
     */

    public double[] isMoveGoal(double[] velocities) {
        double[] ballPos = engine.getSimulatedShot(velocities[0], velocities[1]);
        iterationsCounter++;
        boolean isGoal = Math.pow(ballPos[0] - targetPos[0], 2) + Math.pow(ballPos[1] - targetPos[1], 2) <= Math
                .pow(targetRadius, 2);
        if (isGoal)
            return velocities;
        else
            return null;
    }

    /**
     * Inherited abstract class from super
     */

    public double[] getMove(double precision) {
        for (double i = -5; i < 5; i += precision) {
            for (double j = -5; j < 5; j += precision) {
                double distanceX = i;
                double distanceY = j;
                double finalVelocity = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

                if (finalVelocity > 5) {
                    double[] vel = normalizeVelocity(new double[] { distanceX, distanceY }, 5);
                    distanceX = vel[0];
                    distanceY = vel[1];
                }
                if (isMoveGoal(new double[] { i, j }) != null) {
                    time = System.currentTimeMillis() - checkpoint;
                    return new double[] { distanceX, distanceY };
                }
            }
        }
        return getMove(precision - (precision / 10));
    }

    @Override
    public double[] getMove() {
        checkpoint = System.currentTimeMillis();
        return getMove(precision);
    }
}
