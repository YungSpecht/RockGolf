package com.rock.golf.Bot;
import com.rock.golf.PhysicsEngine;

public class StochasticBot extends Bot {
    
    int iterations;
    

    public StochasticBot(PhysicsEngine engine, int i) {
        this.engine = engine;
        iterations = i;
    }
    
    @Override
    public double[] getMove() {

        double[] best = new double[] { 0, 0 };
        double previousFitness = Integer.MAX_VALUE;
        double[] vel = getRandomVelocities();
        for (int i = 0; i < iterations; i++) {
            System.out.println("iteration " + (i + 1));
            double[] ballPos = engine.get_shot(vel[0], vel[1]);
            double fitness = getFitness(ballPos, targetPos);
            if (fitness < previousFitness) {
                previousFitness = fitness;
                best = vel;
                if (fitness == 0)
                    break;
            }

            vel = getRandomVelocities();
        }

        return new double[] { best[0], best[1], previousFitness };
    }


}
