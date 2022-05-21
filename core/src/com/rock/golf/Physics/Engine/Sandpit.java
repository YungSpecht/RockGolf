package com.rock.golf.Physics.Engine;

public class Sandpit {
    private double[] position;
    private double radius;
    private double uK;
    private double uS;

    public Sandpit(double[] position, double radius, double uK, double uS) {
        this.position = position;
        this.radius = radius;
        this.uK = Math.random() * (1 - uK) + uK;
        this.uS = Math.random() * (1 - this.uK) + this.uK;
    }

    public boolean is_in_sandpit(double xPos, double yPos) {
        if (Math.pow(xPos - position[0], 2) + Math.pow(yPos - position[1], 2) <= Math.pow(radius, 2)) {
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

    public double getUK() {
        return uK;
    }

    public double getUS() {
        return uS;
    }

    public Sandpit() {
        uK = 0.03;
        uS = 0.2;
    }
}
