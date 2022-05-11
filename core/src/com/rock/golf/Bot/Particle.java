package com.rock.golf.Bot;
import java.util.Random;
import com.rock.golf.PhysicsEngine;
import com.rock.golf.Input.InputModule;

public class Particle {
    private double bestFitness = Integer.MAX_VALUE;
    private double[] bestVel;
    private double[] vel;
    private Random rand = new Random();
    private double fitness;
    private PhysicsEngine engine;
    private double[] input = InputModule.get_input();
    private double[] targetPos = new double[]{input[2], input[3]};
    private double targetRadius = input[4];
    private double[] ballPos;

    public Particle(PhysicsEngine engine) {
        vel = getVelocities();
        this.engine = engine;
        calculateFitness();
    }

    private double[] getVelocities() {
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
        return new double[]{velX, velY};
    }

    protected void calculateFitness() {

        ballPos = engine.get_shot(vel[0], vel[1]);
        fitness = getHeuristic(ballPos);

        if (fitness <= bestFitness) {
            bestFitness = fitness;
            bestVel = vel;
            if (fitness == 0) System.out.println("hole");
        }
    }

    private double getHeuristic(double[] ballPos) {
        if (engine.is_in_water(ballPos) || !engine.ball_in_screen(ballPos))
            return Integer.MAX_VALUE;
        if (Math.pow(ballPos[0] - targetPos[0], 2) + Math.pow(ballPos[1] - targetPos[1], 2) <= Math.pow(targetRadius,
                2))
            return 0;
        return Math.sqrt(Math.pow((targetPos[0] - ballPos[0]), 2) + Math.pow((targetPos[1] - ballPos[1]), 2));
    }

    public double[] getVel() {
        return vel;
    }

    public double[] getBestVel() {
        return bestVel;
    }

    public double[] getPos() {
        return ballPos;
    }
    public double getFitness() {
        return fitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }
    
    public void setVel(int i, double j) {
        vel[i] = j;
    }

    public void normalizeVel() {
        double finalVelocity = Math.sqrt(Math.pow(vel[0], 2) + Math.pow(vel[1], 2));

        while (finalVelocity > 5) {
            if (vel[0] > 0)
                vel[0] = vel[0] - 0.1;
            else
                vel[0] = vel[0] + 0.1;

            if (vel[1] > 0)
                vel[1] = vel[1] - 0.1;
            else
                vel[1] = vel[1] + 0.1;

            finalVelocity = Math.sqrt(Math.pow(vel[0], 2) + Math.pow(vel[1], 2));
        }

    }

}
