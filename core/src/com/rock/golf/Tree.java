package com.rock.golf;

public class Tree {

    private double position[] = { 1, 5, 8, 3 };
    private double radius = 0.4;

    public boolean collision_with_tree(double xPos, double yPos) {
        if (Math.pow(xPos - position[0], 2) + Math.pow(yPos - position[1], 2) <= Math.pow(radius, 2)) {
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
