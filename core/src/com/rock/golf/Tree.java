package com.rock.golf;

public class Tree{

    private double position[];
    private double radius;

    public Tree(double[] position, double radius){
        this.position = position;
        this.radius = radius;
    }

    public boolean collidedWithTree(double xPos, double yPos){
        if(Math.pow(xPos - position[0], 2) + Math.pow(yPos - position[1], 2) <= Math.pow(radius, 2)){
            return true;
        }
        return false;
    }

    public double[] get_position() {
        return position;
    }

    public double get_radius() {
        return radius;
    }
}
