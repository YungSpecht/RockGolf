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
    public double[] bounce(double radius, double[] vector, double h){
        //Find previous ball position
        double xOriginal = vector[0]- vector[2]*h;
        double yOriginal = vector[1] - vector[3]*h;

        //Find parallel to tangent and radiusLine equation components to decompose velocity 
        double radiusLineSlope = (position[1]-vector[1])/(position[0]-vector[0]);
        double tangentSlope = -1/radiusLineSlope;
        double cRadius = vector[1]-vector[0]*radiusLineSlope;
        double cParallel = yOriginal-xOriginal*tangentSlope;
        double xIntercept = (cRadius-cParallel)/(tangentSlope-radiusLineSlope);
        double yIntercept = radiusLineSlope*xIntercept+cRadius;

        //Decompose parallel and perpendicular to the tangent velocity components in terms of x/y velocities
        double[] velParallel = {xOriginal-xIntercept, yOriginal-yIntercept};
        double[] velPerpend = {vector[0]-xIntercept,vector[1]-yIntercept};

        return new double[] {velParallel[0]-velPerpend[0],velParallel[1]-velPerpend[1]};
    }

    public double[] getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }
}
