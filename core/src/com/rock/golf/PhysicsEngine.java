package com.rock.golf;

import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Math.Derivation;
import com.rock.golf.Math.RK2Solver;

import org.mariuszgromada.math.mxparser.Function;

public class PhysicsEngine implements Runnable{

    //constants
    public static final double g = 9.81;
    public static final double h = 0.1;
    public final double ballRadius = 0.05;
    private final double Epsilon = 0.01;

    //fields
    private double uK, uS;
    private double targetX, targetY, targetRadius;
    private StateVector currentVector;
    private Function golfCourse;
    private StateVector vector;
    private double[] input;

    public PhysicsEngine(){
        input = InputModule.get_input();
    }


    @Override
    public void run() {
        new_shot();
    }

    private void new_shot(){
        set_variables();
        RK2Solver solve = new RK2Solver(uK, uS, golfCourse);
        Double step = h * 1000;
        long timestep = step.longValue();
        long checkpoint = System.currentTimeMillis();
        while(ball_is_moving() && !ball_in_target() || hill_is_steep() && !ball_in_target()){
            long currentTime = System.currentTimeMillis();
            if(currentTime - checkpoint > timestep){
                vector = solve.runge_kutta_two(vector);
                RockGolf.update_position(vector);
                checkpoint = System.currentTimeMillis();
            }
        }
        InputModule.set_new_position(vector.getXPos(), vector.getYPos());
    }

    private void set_variables(){
        double[] variables = InputModule.get_input();
        uK = variables[0]; uS = variables[1];
        targetX = variables[2]; targetY = variables[3]; targetRadius = variables[4];
        vector = new StateVector(variables[5], variables[6], variables[7], variables[8]);
        golfCourse = InputModule.get_profile();
    }

    public double[] get_input(){
        return input;
    }

    private boolean ball_is_moving(){
        boolean xCheck = true;
        boolean yCheck = true;
        if(vector.getXSpeed() < Epsilon && vector.getXSpeed() > 0 - Epsilon){
            xCheck = false;
        }
        if(vector.getYSpeed() < Epsilon && vector.getYSpeed() > 0 - Epsilon){
            yCheck = false;
        }
        if(xCheck == false && yCheck == false){
            vector.setXSpeed(0);
            vector.setYSpeed(0);
        }
        return xCheck || yCheck;
    }

    private boolean ball_in_target(){
        boolean xCheck = vector.getXPos() + ballRadius < targetX + targetRadius && vector.getXPos() - ballRadius > targetX - targetRadius;
        boolean yCheck = vector.getYPos() + ballRadius < targetY + targetRadius && vector.getYPos() - ballRadius > targetY - targetRadius;
        return xCheck && yCheck;
    }

    private boolean hill_is_steep(){
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), golfCourse);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), golfCourse);
        return uS <= Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2));
    }

}
