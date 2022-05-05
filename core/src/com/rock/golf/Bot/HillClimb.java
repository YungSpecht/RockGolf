package com.rock.golf.Bot;

import java.lang.Thread.State;

import com.rock.golf.StateVector;

public class HillClimb {
    private double targetX;
    private double targetY;
    private double targetRadius;
    private double ballX;
    private double ballY;

    public HillClimb(StateVector vector, double targetX, double targetY, double targetRadius){
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetRadius = targetRadius;
        ballX = vector.getXPos();
        ballY = vector.getYPos();
    }

    public StateVector get_data(){
        return null;
    }
}
