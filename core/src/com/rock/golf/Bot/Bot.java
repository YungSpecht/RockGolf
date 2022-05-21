package com.rock.golf.Bot;

import java.util.Random;

import com.rock.golf.Input.InputModule;
import com.rock.golf.Physics.Engine.PhysicsEngine;

public abstract class Bot {

    PhysicsEngine engine;
    double[] input = InputModule.get_input();
    double[] targetPos = new double[] {input[2], input[3] };
    double[] ballPos = new double[] {input[5], input[6] };
    double targetRadius = input[4];
    Random rand = new Random();
    int iterationsCounter = 0;
    long time = 0;

    protected double getFitness(double[] ballPos, double[] targetPos) {
        if (engine.is_in_water(ballPos[0], ballPos[1])) return Integer.MAX_VALUE;
        if (Math.pow(ballPos[0] - targetPos[0], 2) + Math.pow(ballPos[1] - targetPos[1], 2) <= Math.pow(targetRadius, 2))
            return 0;
        return Math.sqrt(Math.pow((targetPos[0] - ballPos[0]), 2) + Math.pow((targetPos[1] - ballPos[1]), 2));
    }

    protected double[] normalizeVelocity(double[] velocities, double velocity) {
        double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
        double scalar = velocity / currentVel;
        return new double[] { velocities[0] * scalar, velocities[1] * scalar };

    }

    protected double[][][] generate_shot_range(double currentAngle, int divergentShots, double outermostAngle, double velocityStart, double velocityEnd, int velAmount) {
        double[][][] result = new double[1 + 2 * divergentShots][velAmount][2];
        for (int i = 0; i < velAmount; i++) {
            if (velAmount == 1)
                result[0][0] = get_velocity(currentAngle, velocityStart);
            else
                result[0][i] = get_velocity(currentAngle, velocityStart + i * ((velocityEnd - velocityStart) / (velAmount - 1)));
        }
        for (int i = 1; i < result.length; i = i + 2) {
            for (int j = 0; j < result[0].length; j++) {
                if (velAmount == 1) {
                    result[i][j] = get_velocity(currentAngle + ((i + 1) / 2) * (outermostAngle / divergentShots), velocityStart);
                    result[i + 1][j] = get_velocity(currentAngle - ((i + 1) / 2) * (outermostAngle / divergentShots), velocityStart);
                } else {
                    result[i][j] = get_velocity(currentAngle + ((i + 1) / 2) * (outermostAngle / divergentShots), velocityStart + j * ((velocityEnd - velocityStart) / (velAmount - 1)));
                    result[i + 1][j] = get_velocity(currentAngle - ((i + 1) / 2) * (outermostAngle / divergentShots), velocityStart + j * ((velocityEnd - velocityStart) / (velAmount - 1)));
                }
            }
        }
        return result;
    }

    protected double[] process_shots(double[][][] shots, double referenceDistance, double instantReturn) {
        double[] result = new double[2];
        double refDist = referenceDistance;
        for (int i = 0; i < shots[0].length; i++) {
            double[] shotCoords = engine.get_shot(shots[0][i][0], shots[0][i][1]);
            iterationsCounter++;
            double distance = EuclideanDistance(shotCoords);
            if (distance < refDist && !engine.is_in_water(shotCoords[0], ballPos[1])) {
                result = shots[0][i];
                refDist = distance;
                System.out.println("New Shortest Distance: " + (refDist - targetRadius));
            }
            if (refDist < targetRadius || refDist < (instantReturn + targetRadius)) {
                return result;
            }
        }
        for (int i = 1; i < shots.length; i = i + 2) {
            for (int j = 0; j < shots[0].length; j++) {
                double[] shotCoords = engine.get_shot(shots[i][j][0], shots[i][j][1]);
                iterationsCounter++;
                double distance = EuclideanDistance(shotCoords);
                if (distance < refDist && !engine.is_in_water(shotCoords[0], ballPos[1])) {
                    result = shots[i][j];
                    refDist = distance;
                    System.out.println("New Shortest Distance: " + (refDist - targetRadius));
                }
                if (refDist < targetRadius || refDist < (instantReturn + targetRadius)) {
                    return result;
                }

                shotCoords = engine.get_shot(shots[i+1][j][0], shots[i+1][j][1]);
                iterationsCounter++;
                distance = EuclideanDistance(shotCoords);
                if (distance < refDist && !engine.is_in_water(shotCoords[0], ballPos[1])) {
                    result = shots[i+1][j];
                    refDist = distance;
                    System.out.println("New Shortest Distance: " + (refDist - targetRadius));
                }
                if (refDist < targetRadius || refDist < (instantReturn + targetRadius)) {
                    return result;
                }
            }
        }
        return result;
    }

    protected double EuclideanDistance(double[] position) {
        return Math.sqrt(Math.pow((targetPos[0] - position[0]), 2) + Math.pow((targetPos[1] - position[1]), 2));
    }

    protected double convert(double radian) {
        if (radian > 0) {
            return Math.toDegrees(radian);
        } else {
            return Math.toDegrees(2 * Math.PI + radian);
        }
    }

    public double[] getRandomVelocities() {
        double velX = (rand.nextDouble() * 10) - 5;
        double velY = (rand.nextDouble() * 10) - 5;
        double finalVelocity = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));

        if(finalVelocity > 5) {
            double[] vel = normalizeVelocity(new double[]{velX, velY}, 5);
            velX = vel[0];
            velY = vel[1];
        }

        return new double[]{velX,velY};
    }

    protected double[] get_velocity_range(double[] velocity, double range) {
        double currentVel = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
        double[] result = new double[] { currentVel - range, currentVel + range };
        if (result[0] < 0) {
            result[0] = 0;
        }
        if (result[1] > 5) {
            result[1] = 5;
        }
        return result;
    }

    protected boolean vel_is_legal(double[] velPair) {
        return Math.sqrt(Math.pow(velPair[0], 2) + Math.pow(velPair[1], 2)) <= 5.0;
    }

    protected double[] get_velocity(double angle, double velocity) {
        double[] result = new double[] { Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)) };
        return normalizeVelocity(result, velocity);
    }
    
    public long getTime(){
        return time;
    }

    public int getIterations(){
        return iterationsCounter;
    }

    public abstract double[] getMove();
}
