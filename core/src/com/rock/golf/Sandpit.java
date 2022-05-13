package com.rock.golf;

public class Sandpit {
    private double[] position;
    private double radius;
    private double uK;
    private double uS;

<<<<<<< HEAD
    public Sandpit(double[] position, double radius, double uK, double uS){
        this.position = position;
        this.radius = radius;
        this.uK = Math.random() * (1 - uK) + uK;
        this.uS = Math.random() * (1 - this.uK) + this.uK;
    }

    public boolean is_in_sandpit(double xPos, double yPos){
        if (Math.pow(xPos - position[0], 2) + Math.pow(yPos - position[1], 2) <= Math.pow(radius, 2)) {
            return true;
        }
        return false;
    }

    public double[] get_position(){
        return position;
    }

    public double get_radius(){
        return radius;
    }

    public double get_uK(){
        return uK;
    }

    public double get_uS(){
        return uS;
    }
=======
    public Sandpit() {
        uK = 0.03;
        uS = 0.2;
    }

>>>>>>> 8b4329554e3806a22f974143751c5c38f41acc61
}
