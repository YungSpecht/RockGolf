package com.rock.golf.Bot;
import java.util.Random;
import com.rock.golf.PhysicsEngine;
import com.rock.golf.Input.InputModule;

public abstract class Bot {

    PhysicsEngine engine;
    double[] targetPos = new double[]{InputModule.get_input()[2], InputModule.get_input()[3]};
    double[] ballPos = new double[]{InputModule.get_input()[5], InputModule.get_input()[6]};
    double targetRadius = InputModule.get_input()[4];
    Random rand = new Random();
    
    protected double getFitness(double[] ballPos, double[] targetPos) {
        if (engine.is_in_water(ballPos) || !engine.ball_in_screen(ballPos))
            return Integer.MAX_VALUE;
        if (Math.pow(ballPos[0] - targetPos[0], 2) + Math.pow(ballPos[1] - targetPos[1], 2) <= Math.pow(targetRadius,
                2))
            return 0;
        return Math.sqrt(Math.pow((targetPos[0] - ballPos[0]), 2) + Math.pow((targetPos[1] - ballPos[1]), 2));
    }

    protected double[] normalizeVelocity(double[] velocities, double velocity) {
        double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
        double scalar = velocity / currentVel;
        return new double[] { velocities[0] * scalar, velocities[1] * scalar };

    }

    
    protected double EuclideanDistance(double[] position) {
        return Math.sqrt(Math.pow((targetPos[0] - position[0]), 2) + Math.pow((targetPos[1] - position[1]), 2));
    }



    protected double convert(double radian){
        if(radian > 0){
            return Math.toDegrees(radian);
        } else {
            return Math.toDegrees(2 * Math.PI + radian);
        }
    }

    public double[] getRandomVelocities() {
        double velX = (rand.nextDouble() * 10) - 5;
        double velY = (rand.nextDouble() * 10) - 5;

        return normalizeVelocity(new double[]{velX,velY},5);
    }

    public abstract double[] getMove();
}
