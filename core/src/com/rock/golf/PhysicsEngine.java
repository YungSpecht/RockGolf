package com.rock.golf;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.List;

import com.rock.golf.Input.InputModule;
import com.rock.golf.Math.Derivation;
import com.rock.golf.Math.RK2Solver;

public class PhysicsEngine implements Runnable{

    //constants
    public static final double g = 9.81;
    public static double h = 0.01;
    public final double ballRadius = 0.05;
    private double Epsilon = 0.01;

    //fields
    private double uK, uS;
    private double targetX, targetY, targetRadius;
    private Function golfCourse;
    private StateVector vector;
    private double[] input;
    private boolean isInWater;
    private List<Sandpit> sandpits;
    private boolean abort;

    //constructor
    public PhysicsEngine(double h){
        input = InputModule.get_input();
        set_variables();
        abort = false;
        sandpits = new ArrayList<Sandpit>();
        isInWater = is_in_water();
        this.h = h;
        this.Epsilon = h;
    }

    public PhysicsEngine(){
        input = InputModule.get_input();
        set_variables();
        abort = false;
        sandpits = new ArrayList<Sandpit>();
        isInWater = is_in_water();
    }

    public StateVector getVector() {
        return vector;
    }

    @Override
    public void run() {
        new_shot();
    }

    /**
	 * This method starts a new golf shot based on the current parameters that are set in the Input.txt file.
	 */
    private void new_shot(){
        RockGolf.shotActive = true;
        RockGolf.shotCounter++;
        set_variables();
        RK2Solver solve = new RK2Solver(uK, uS, golfCourse);
        while((ball_is_moving() && !ball_in_target() || hill_is_steep() && !ball_in_target()) && !is_in_water() && ball_in_screen()){
            vector = solve.runge_kutta_two(vector);
            RockGolf.update_position(vector);
            if(abort){
                break;
            }
        }
        InputModule.set_new_position(vector.getXPos(), vector.getYPos());
        RockGolf.shotActive = false;
    }

    private boolean ball_in_screen() {
        boolean xIn = vector.getXPos() < 4.5 && vector.getXPos() > -4.5;
        boolean yIn = vector.getYPos() < 3.5 && vector.getYPos() > -3.5;
        return xIn && yIn;
    }


    /**
	 * This method reads in all the parameters from the Input.txt file and updates the fields
     * of the physics engine accordingly.
	 */
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

    /**
	 * This method determines wether the ball is currently moving by checking wether both the 
     * x and y velocities are in a range of error epsilon of 0.
     * 
     * @return Boolean value: true if ball is moving, false if not.
	 */
    public boolean ball_is_moving(){
        boolean xCheck = true;
        boolean yCheck = true;
        if(vector.getXSpeed() < Epsilon && vector.getXSpeed() > 0 - Epsilon){
            xCheck = false;
        }

        if(vector.getYSpeed() < Epsilon && vector.getYSpeed() > 0 - Epsilon){
            yCheck = false;
        }

        if(xCheck == false && yCheck == false) {
            vector.setXSpeed(0);
            vector.setYSpeed(0);
        }

        return xCheck || yCheck;
    }

    /**
	 * This method determines wether the ball is currently inside the target based on the position and
     * radius of the ball as well as the target.
     * 
     * @return Boolean value: true if ball is inside target, false if not.
	 */
    private boolean ball_in_target(){

        //((x - circle_x) * (x - circle_x) + (y - circle_y) * (y - circle_y) <= rad * rad);

        if(Math.pow(vector.getXPos() - targetX, 2) + Math.pow(vector.getYPos() - targetY, 2) <= Math.pow(targetRadius, 2)){
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    /**
	 * This method determines wether the downhillforce acting upon the golf ball in rest is
     * greater than the static friction, which would cause it to start rolling again.
     * 
     * @return Boolean value: true if ball is about to start rolling again, false if not.
	 */
    private boolean hill_is_steep(){
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), golfCourse);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), golfCourse);
        return uS <= Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2));
    }

    /**
	 * This method determines wether the ball has fallen into water by evaluating the golf course
     * function at the current position of the bal
     * 
     * @return Boolean value: true if ball is currently in water, false if not.
	 */
    public boolean is_in_water(){
        if(Derivation.compute(vector.getXPos(), vector.getYPos(), golfCourse) < 0){
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    public double[] get_shot(double velX, double velY){
        double[] variables = InputModule.get_input();
        uK = variables[0]; uS = variables[1];
        targetX = variables[2]; targetY = variables[3]; targetRadius = variables[4];
        vector = new StateVector(variables[5], variables[6], velX, velY);
        golfCourse = InputModule.get_profile();
        RK2Solver solve = new RK2Solver(uK, uS, golfCourse);
        while((ball_is_moving() && !ball_in_target() || hill_is_steep() && !ball_in_target()) && !is_in_water()){
            vector = solve.runge_kutta_two(vector);
            if(abort){
                break;
            }
        }
        return new double[]{vector.getXPos(), vector.getYPos()};
    }


    /**
	 * This method aborts a golf ball shot in case of closing the program.
	 */
    public void abort(){
        abort = true;
    }
}
