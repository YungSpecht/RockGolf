package com.rock.golf.Physics.Engine;

import java.lang.Thread.State;

public class StateVector {
    private double XPos;
    private double YPos;
    private double XSpeed;
    private double YSpeed;

    public StateVector(double XPos, double YPos, double XSpeed, double YSpeed) {
        this.XPos = XPos;
        this.YPos = YPos;
        this.XSpeed = XSpeed;
        this.YSpeed = YSpeed;
    }

    public double getXPos() {
        return XPos;
    }

    public double getYPos() {
        return YPos;
    }

    public double getXSpeed() {
        return XSpeed;
    }

    public double getYSpeed() {
        return YSpeed;
    }

    public void setXPos(double XPos) {
        this.XPos = XPos;
    }

    public void setYPos(double YPos) {
        this.YPos = YPos;
    }

    public void setXSpeed(double XSpeed) {
        this.XSpeed = XSpeed;
    }

    public void setYSpeed(double YSpeed) {
        this.YSpeed = YSpeed;
    }

    public static StateVector add(StateVector one, StateVector two) {
        double first = one.getXPos() + two.getXPos();
        double second = one.getYPos() + two.getYPos();
        double third = one.getXSpeed() + two.getXSpeed();
        double fourth = one.getYSpeed() + two.getYSpeed();
        return new StateVector(first, second, third, fourth);
    }

    public static StateVector multiply(StateVector a, double b) {
        return new StateVector(a.getXPos() * b, a.getYPos() * b, a.getXSpeed() * b, a.getYSpeed() * b);
    }

    public String toString() {
        return "X Position: " + XPos + "  Y Position: " + YPos + " || X Velocity: " + XSpeed + "  Y Velocity: " + YSpeed;
    }

    public static boolean isEqual(StateVector vector, StateVector testedVector) {
        double epsilon = 0.001;
        boolean xPos = Math.abs(vector.getXPos() - testedVector.getXPos()) < epsilon;
        boolean yPos = Math.abs(vector.getYPos() - testedVector.getYPos()) < epsilon;
        boolean xSpeed = Math.abs(vector.getXSpeed() - testedVector.getXSpeed()) < epsilon;
        boolean ySpeed =  Math.abs(vector.getYSpeed() - testedVector.getYSpeed()) < epsilon;

        return xPos && yPos && xSpeed && ySpeed;

    }
}
