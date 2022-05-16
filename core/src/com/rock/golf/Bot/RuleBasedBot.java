package com.rock.golf.Bot;
import com.rock.golf.PhysicsEngine;

public class RuleBasedBot extends Bot {
    double powerCoefficient = 1;

    public RuleBasedBot(PhysicsEngine engine) {
        this.engine = engine;
    }
    
    @Override
    public double[] getMove() {
        
        double angleToShot = convert(Math.atan2(targetPos[1] - ballPos[1], targetPos[0] - ballPos[0]));
        double distanceToTarget = EuclideanDistance(ballPos);
        double velX = (powerCoefficient*distanceToTarget) * Math.cos(Math.toRadians(angleToShot));
        double velY = (powerCoefficient*distanceToTarget) * Math.sin(Math.toRadians(angleToShot));

        return normalizeVelocity(new double[]{velX,velY},5);
    }

    

}
