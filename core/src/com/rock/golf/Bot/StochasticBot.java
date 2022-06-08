package com.rock.golf.Bot;

import com.rock.golf.Physics.Engine.PhysicsEngine;

public class StochasticBot extends Bot {

    int iterations;
<<<<<<< HEAD
    Random rand = new Random();
    double[] targetPos = new double[] { InputModule.get_input()[2], InputModule.get_input()[3] };
    double targetRadius = InputModule.get_input()[4];
=======
>>>>>>> main

    public StochasticBot(PhysicsEngine engine, int i) {
        this.engine = engine;
        iterations = i;
    }

<<<<<<< HEAD
    public double[] getVelocities() {
        double velX = (rand.nextDouble() * 10) - 5;
        double velY = (rand.nextDouble() * 10) - 5;

        double finalVelocity = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));

        while (finalVelocity > 5) {
            if (velX > 0)
                velX = velX - 0.1;
            else
                velX = velX + 0.1;

            if (velY > 0)
                velY = velY - 0.1;
            else
                velY = velY + 0.1;

            finalVelocity = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
        }
        return new double[] { velX, velY };
    }

    public double[] getBestMove() {
=======
    /** 
     * Inherited abstract class from super
     */
>>>>>>> main

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
