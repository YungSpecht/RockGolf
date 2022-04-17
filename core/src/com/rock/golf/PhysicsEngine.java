package com.rock.golf;

import org.mariuszgromada.math.mxparser.Function;

public class PhysicsEngine implements Runnable{

    //constants
    public static final double g = 9.81;
    public static final double h = 0.1;
    public final double ballRadius = 0.05;

    //fields
    private double uK, uS;
    private double targetX, targetY, targetRadius;
    private StateVector currentVector;
    private Function golfCourse;

    public PhysicsEngine(){

    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

    private void new_shot(StateVector initialConditions){

    }

}
