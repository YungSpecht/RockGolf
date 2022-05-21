package com.rock.golf.Bot;

import com.rock.golf.Physics.Engine.PhysicsEngine;

public class RuleBasedBot extends Bot {
    double powerCoefficient = 1;

    public RuleBasedBot(PhysicsEngine engine) {
        this.engine = engine;
    }

    @Override
    public double[] getMove() {

        double angleToShot = Math.atan2(targetPos[1] - ballPos[1], targetPos[0] - ballPos[0]);
        double velX = Math.cos(angleToShot);
        double velY = Math.sin(angleToShot);
        while(somethingsInTheWay(velX, velY)){
            double[] divShot = getDivergentShot(new double[]{velX, velY}, 5);
            velX = divShot[0];
            velY = divShot[1];
        }
        return normalizeVelocity(new double[]{velX, velY}, 5);

    }

    private boolean somethingsInTheWay(double xDir, double yDir){
        double distance = EuclideanDistance(ballPos);
        double[] directionsFullScaled = normalizeVelocity(new double[]{xDir, yDir}, distance);
        double steps =  distance / 0.05;
        double downScaledXDir = directionsFullScaled[0]/steps;
        double downScaledyDir = directionsFullScaled[1]/steps;
        int counter = 0;
        for(double i = 0; i < distance; i+=0.05){
            counter++;
            if(engine.is_in_water(ballPos[0] + counter*downScaledXDir, ballPos[1] + counter*downScaledyDir) || engine.collidedWithTree(ballPos[0] + counter*downScaledXDir, ballPos[1] + counter*downScaledyDir)){
                return true;
            }
        }
        return false;
    }

    private double[] getDivergentShot(double[] currentShot, double angle){
        double currentDegreeAngle = convert(Math.atan2(currentShot[1], currentShot[0]));
        double newRadianAngle = Math.toRadians(currentDegreeAngle - angle);
        return normalizeVelocity(new double[]{Math.cos(newRadianAngle), Math.sin(newRadianAngle)}, 4);
    }

}
