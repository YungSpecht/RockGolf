package com.rock.golf.Bot;

import com.rock.golf.Physics.Engine.PhysicsEngine;

public class StochasticBot extends Bot {

    int iterations;

    public StochasticBot(PhysicsEngine engine, int i) {
        this.engine = engine;
        iterations = i;
    }

    /** 
     * Inherited abstract class from super
     */

    @Override
    public double[] getMove() {
        long checkpoint = System.currentTimeMillis();
        double[] best = new double[] { 0, 0 };
        double previousFitness = Integer.MAX_VALUE;
        double[] vel = getRandomVelocities();
        for (int i = 0; i < iterations; i++) {
            double[] ballPos = engine.getSimulatedShot(vel[0], vel[1]);
            iterationsCounter++;
            double fitness = getFitness(ballPos, targetPos);
            if (fitness < previousFitness) {
                previousFitness = fitness;
                best = vel;
                if (fitness == 0)
                    break;
            }
            vel = getRandomVelocities();
        }
        
        time = System.currentTimeMillis()-checkpoint;
        return new double[] { best[0], best[1] };
    }
}
