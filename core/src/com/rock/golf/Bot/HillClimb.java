package com.rock.golf.Bot;
import java.util.Random;
import com.rock.golf.PhysicsEngine;
import com.rock.golf.Input.InputModule;

public class HillClimb {
    PhysicsEngine engine;
    double[] targetPos = new double[]{InputModule.get_input()[2], InputModule.get_input()[3]};
    double[] currentPos = new double[]{InputModule.get_input()[5], InputModule.get_input()[6]};

    public HillClimb(PhysicsEngine engine) {
        this.engine = engine;
    }

    public double[] isMoveGoal(double[] velocities) {
        double[] ballPos = engine.get_shot(velocities[0], velocities[1]);
        boolean isGoal = (getFitness(ballPos, targetPos) < 1);
        if(isGoal) return velocities;
        else return null;
    }

    public double[] getMove(double precision) {
        for(double i = -5; i < 5; i += precision) {
            for(double j = -5; j < 5; j += precision) {
                if(isMoveGoal(new double[]{i,j}) != null) {
                    return new double[]{i, j};
                } else {
                    System.out.println("not a goal :(");
                }
            }
        }
        return getMove(precision - 0.1);
    }

    private double getFitness(double[] ballPos, double[] targetPos) {
        return Math.sqrt(Math.pow((targetPos[0] - ballPos[0]), 2) + Math.pow((targetPos[1] - ballPos[1]),2));
    }

}
