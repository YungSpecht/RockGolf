package com.rock.golf.Physics.Engine;

public class Tree{

    private double position[];
    private double radius;

    public Tree(double[] position, double radius){
        this.position = position;
        this.radius = radius;
    }

    public boolean collidedWithTree(double xPos, double yPos, double ballRadius){
        if(Math.sqrt(Math.pow(xPos-position[0], 2)+Math.pow(yPos-position[1], 2)) < radius+ballRadius){
            return true;
        }
        return false;
    }

    /**
     * This method takes care of bouncing off of trees
     * @param radius
     * @param vector
     * @param h
     * @return x and y bounce-velocity components 
     */
    public StateVector bounce(double radius, StateVector vector, double h){
        //Find previous ball position
        double xOriginal = vector.getXPos()- vector.getXSpeed()*h;
        double yOriginal = vector.getYPos() - vector.getYSpeed()*h;

        //Find parallel to tangent and radiusLine equation components to decompose velocity 
        double radiusLineSlope = (position[1]-vector.getYPos())/(position[0]-vector.getXPos());
        double tangentSlope = -1/radiusLineSlope;
        double cRadius = vector.getYPos()-vector.getXPos()*radiusLineSlope;
        double cParallel = yOriginal-xOriginal*tangentSlope;
        double xIntercept = (cRadius-cParallel)/(tangentSlope-radiusLineSlope);
        double yIntercept = radiusLineSlope*xIntercept+cRadius;

        //Decompose parallel and perpendicular to the tangent velocity components in terms of x/y velocities
        double[] velParallel = {xOriginal-xIntercept, yOriginal-yIntercept};
        double[] velPerpend = {vector.getXPos()-xIntercept,vector.getYPos()-yIntercept};

        return new StateVector(vector.getXPos(), vector.getYPos(),velParallel[0]-velPerpend[0],velParallel[1]-velPerpend[1]);
    }

    public double[] getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }
}
