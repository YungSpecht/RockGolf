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

    public double[] getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }
}
