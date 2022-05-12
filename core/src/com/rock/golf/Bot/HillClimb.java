package com.rock.golf.Bot;

import java.util.Random;
import com.rock.golf.PhysicsEngine;
import com.rock.golf.Input.InputModule;

public class HillClimb {
    PhysicsEngine engine;
    double[] targetPos = new double[] { InputModule.get_input()[2], InputModule.get_input()[3] };
    double[] currentPos = new double[] { InputModule.get_input()[5], InputModule.get_input()[6] };
    double targetRadius = InputModule.get_input()[4];

    public HillClimb(PhysicsEngine engine) {
        this.engine = engine;
    }

    public double[] isMoveGoal(double[] velocities) {
        double[] ballPos = engine.get_shot(velocities[0], velocities[1]);
        boolean isGoal = Math.pow(ballPos[0] - targetPos[0], 2) + Math.pow(ballPos[1] - targetPos[1], 2) <= Math
                .pow(targetRadius, 2);
        if (isGoal)
            return velocities;
        else
            return null;
    }

    public double[] getMove(double precision) {

        for(double i = -5; i < 5; i += precision) {
            for(double j = -5; j < 5; j += precision) {
                double distanceX = i;
                double distanceY = j;
                double finalVelocity = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

                while(finalVelocity > 5) {
                    if(distanceX > 0) distanceX = distanceX - 0.1;
                    else distanceX = distanceX + 0.1;
                    
                    if(distanceY > 0) distanceY = distanceY - 0.1;
                    else distanceY = distanceY + 0.1;

                    finalVelocity = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
                }
                
                if(isMoveGoal(new double[]{i,j}) != null) {
                    return new double[]{distanceX, distanceY};
                } else {
                    System.out.println("not a goal :(");
                }
            }
        }
        return getMove(precision - (precision / 10));
    }
}