package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;

public class Particle extends Bot {
    private double bestFitness = Integer.MAX_VALUE;
    private double[] bestVel;
    private double[] vel;
    private double fitness;
    private double[] ballPos;

    public Particle(PhysicsEngine engine) {
        vel = getRandomVelocities();
        this.engine = engine;
        calculateFitness();
    }

    protected void calculateFitness() {

        ballPos = engine.get_shot(vel[0], vel[1]);
        fitness = getFitness(ballPos, targetPos);

        if (fitness <= bestFitness) {
            bestFitness = fitness;
            bestVel = vel;
            if (fitness == 0)
                System.out.println("hole");
        }
    }

    public double[] getVel() {
        return vel;
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

    @Override
    public double[] getMove() {
        return bestVel;
    }
}
