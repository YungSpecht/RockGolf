package com.rock.golf.Bot;
import java.util.Random;
import com.rock.golf.PhysicsEngine;
import com.rock.golf.Input.InputModule;

public class StochasticBot {
    PhysicsEngine engine;
    int iterations;
    Random rand = new Random();
    double[] targetPos = new double[]{InputModule.get_input()[2], InputModule.get_input()[3]};

    public StochasticBot(PhysicsEngine engine, int i) {
        this.engine = engine;
        iterations = i;
    }

    public double[] getVelocities() {
        double velX = (rand.nextDouble() * 10) - 5;
        double velY = (rand.nextDouble() * 10) - 5;

        double finalVelocity = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));

        while(finalVelocity > 5) {
            if(velX > 0) velX = velX - 0.1;
            else velX = velX + 0.1;
            
            if(velY > 0) velY = velY - 0.1;
            else velY = velY + 0.1;

            finalVelocity = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
        }
        System.out.println(velX);
        System.out.println(velY);
        return new double[]{velX, velY};
    }

    public double[] getBestMove() {

        double[] best = new double[]{0,0};
        double previousFitness = Integer.MAX_VALUE;
        double[] vel = getVelocities();
        for(int i = 0; i < iterations; i++) {
            System.out.println("iteration " + (i+1));
            double[] ballPos = engine.get_shot(vel[0], vel[1]);
            double fitness = getFitness(ballPos, targetPos);

            if(fitness < previousFitness) {
                previousFitness = fitness;
                best = vel;
            }

            vel = getVelocities();
        }

        return new double[]{best[0], best[1], previousFitness};
    }

    private double getFitness(double[] ballPos, double[] targetPos) {
        return Math.sqrt(Math.pow((targetPos[0] - ballPos[0]), 2) + Math.pow((targetPos[1] - ballPos[1]),2));
    }

}