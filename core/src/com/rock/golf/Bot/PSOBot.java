package com.rock.golf.Bot;

import com.rock.golf.PhysicsEngine;

public class PSOBot {
    private PhysicsEngine engine;
    private int max;
    private int numDimensions = 2;
    private int population = 10;
    private double[] best;
    private double[] r1;
    private double[] r2;
    Particle[] particles;
    private double c1 = 1;
    private double c2 = 1.496180;
    private double w = 0.729844;

    public PSOBot(PhysicsEngine engine, int max) {
        this.engine = engine;
        this.max = max;
        particles = new Particle[population];
        initializeParticles();
    }

    public void initializeParticles() {

        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(engine);
        }

    }

    public double[] run() {
        int i = 0;
        while (i < max) {
            best = findBest();
            System.out.println("iteration " + i);
            r1 = new double[numDimensions];
            r2 = new double[numDimensions];

            for (int k = 0; k < numDimensions; k++) {
                r1[k] = Math.random();
                r2[k] = Math.random();
            }

            for (int k = 0; k < particles.length; k++) {
                updateParticle(particles[k], best, r1, r2);
            }

            i++;
            System.out.println("velX: " + best[0] + "\n velY: " + best[1]);
        }
        return best;
    }

    public void updateParticle(Particle particle, double[] best, double[] r1, double[] r2) {
        double[] vel = particle.getVel();
        double[] personalBest = particle.getMove();
        double[] positions = particle.getPos();
        double[] bestNeigh = best.clone();

        double[] inertiaTerm = new double[numDimensions];
        double[] difference1 = new double[numDimensions];
        double[] difference2 = new double[numDimensions];
        double[] c1Timesr1 = new double[numDimensions];
        double[] c2Timesr2 = new double[numDimensions];
        double[] cognitiveTerm = new double[numDimensions];
        double[] socialTerm = new double[numDimensions];

        for (int i = 0; i < numDimensions; i++) {
            inertiaTerm[i] = w * vel[i];
        }

        for (int i = 0; i < numDimensions; i++) {
            difference1[i] = personalBest[i] - positions[i];
        }

        for (int i = 0; i < numDimensions; i++) {
            c1Timesr1[i] = c1 * r1[i];
        }

        for (int i = 0; i < numDimensions; i++) {
            cognitiveTerm[i] = c1Timesr1[i] * difference1[i];
        }

        for (int i = 0; i < numDimensions; i++) {
            difference2[i] = bestNeigh[i] - positions[i];
        }

        for (int i = 0; i < numDimensions; i++) {
            c2Timesr2[i] = c2 * r2[i];
        }

        for (int i = 0; i < numDimensions; i++) {
            socialTerm[i] = c2Timesr2[i] * difference2[i];
        }

        for (int i = 0; i < numDimensions; i++) {
            particle.setVel(i, inertiaTerm[i] + cognitiveTerm[i] + socialTerm[i]);
        }

        particle.normalizeVelocity(new double[] { particle.getVel()[0], particle.getVel()[1] }, 5);
        particle.calculateFitness();
    }

    public double[] findBest() {
        double[] best = null;
        double bestFitness = Integer.MAX_VALUE;
        for (Particle particle : particles) {
            if (particle.getBestFitness() < bestFitness) {
                bestFitness = particle.getBestFitness();
                best = particle.getMove();
            }
        }
        System.out.println(bestFitness);
        return new double[] { best[0], best[1] };
    }

}
