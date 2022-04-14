package com.rock.golf;

public class StateVector {
    private double XPos;
    private double YPos;
    private double XSpeed;
    private double YSpeed;

    public StateVector(double XPos, double YPos, double XSpeed, double YSpeed){
        this.XPos = XPos;
        this.YPos = YPos;
        this.XSpeed = XSpeed;
        this.YSpeed = YSpeed;
    }

    public double getXPos(){
        return XPos;
    }

    public double getYPos(){
        return YPos;
    }

    public double getXSpeed(){
        return XSpeed;
    }

    public double getYSPeed(){
        return YSpeed;
    }

    public void setXPos(double XPos){
        this.XPos = XPos;
    }

    public void setYPos(double YPos){
        this.YPos = YPos;
    }

    public void setXSpeed(double XSpeed){
        this.XSpeed = XSpeed;
    }

    public void setYSPeed(double YSpeed){
        this.YSpeed = YSpeed;
    }
}

