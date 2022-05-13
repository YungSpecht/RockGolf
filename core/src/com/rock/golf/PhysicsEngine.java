package com.rock.golf;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.List;

import com.rock.golf.Input.InputModule;
import com.rock.golf.Math.Derivation;
import com.rock.golf.Math.RK2Solver;

public class PhysicsEngine implements Runnable {

    // constants
    public static final double g = 9.81;
    public double h = 0.01;
    public final double ballRadius = 0.05;

    // fields
    private double uK, uS;
    private double targetX, targetY, targetRadius;
    private Function golfCourse;
    private StateVector vector;
    private double[] input;
    private boolean abort;
    private List<Sandpit> sandpits;

    // constructor
    public PhysicsEngine(double h) {
        input = InputModule.get_input();
        set_variables();
        abort = false;
        this.h = h;
        sandpits = new ArrayList<Sandpit>();
        sandpits.add(new Sandpit(new double[]{-3, 3}, 2, uK, uS));
    }

    public PhysicsEngine() {
        input = InputModule.get_input();
        set_variables();
        abort = false;
        sandpits = new ArrayList<Sandpit>();
        sandpits.add(new Sandpit(new double[]{-3, 3}, 2, uK, uS));
    }

    public StateVector getVector() {
        return vector;
    }

    @Override
    public void run() {
        new_shot();
    }

    /**
     * This method starts a new golf shot based on the current parameters that are
     * set in the Input.txt file.
     */
    private void new_shot() {
        RockGolf.shotActive = true;
        RockGolf.shotCounter++;
        set_variables();
        RK2Solver solve = new RK2Solver(uK, uS, h, golfCourse);
        while ((ball_is_moving() && !ball_in_target() || hill_is_steep() && !ball_in_target()) && !is_in_water() && ball_in_screen()) {
            Sandpit currentSandpit = current_sandpit();
            if(currentSandpit != null){
                solve.update_friction(currentSandpit.get_uK(), currentSandpit.get_uS());
            }
            else{
                solve.update_friction(uK, uS);
            }
            vector = solve.runge_kutta_two(vector);
            RockGolf.update_position(vector);
            if (abort) {
                break;
            }
        }
        InputModule.set_new_position(vector.getXPos(), vector.getYPos());
        RockGolf.shotActive = false;
    }

    private Sandpit current_sandpit(){
        for(int i = 0; i < sandpits.size(); i++){
            if(sandpits.get(i).is_in_sandpit(vector.getXPos(), vector.getYPos())){
                return sandpits.get(i);
            }
        }
        return null;
    }

    private boolean ball_in_screen() {
        boolean xIn = vector.getXPos() < 4.5 && vector.getXPos() > -4.5;
        boolean yIn = vector.getYPos() < 3.5 && vector.getYPos() > -3.5;
        return xIn && yIn;
    }

    public boolean ball_in_screen(double[] ballPos) {
        boolean xIn = ballPos[0] < 4.5 && ballPos[0] > -4.5;
        boolean yIn = ballPos[1] < 3.5 && ballPos[1] > -3.5;
        return xIn && yIn;
    }

    /**
     * This method reads in all the parameters from the Input.txt file and updates
     * the fields
     * of the physics engine accordingly.
     */
    private void set_variables() {
        double[] variables = InputModule.get_input();
        uK = variables[0];
        uS = variables[1];
        targetX = variables[2];
        targetY = variables[3];
        targetRadius = variables[4];
        vector = new StateVector(variables[5], variables[6], variables[7], variables[8]);
        golfCourse = InputModule.get_profile();
    }

    public double[] get_input() {
        return input;
    }

    /**
     * This method determines wether the ball is currently moving.
     * 
     * @return Boolean value: true if ball is moving, false if not.
     */
    public boolean ball_is_moving() {
        if (Math.abs(Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2))) < h) {
            vector.setXSpeed(0);
            vector.setYSpeed(0);
            return false;
        }
        return true;
    }


    /**
     * This method determines wether the ball is currently inside the target based
     * on the position and
     * radius of the ball as well as the target.
     * 
     * @return Boolean value: true if ball is inside target, false if not.
     */
    private boolean ball_in_target() {
        if (Math.pow(vector.getXPos() - targetX, 2) + Math.pow(vector.getYPos() - targetY, 2) <= Math.pow(targetRadius, 2)) {
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    /**
     * This method determines wether the downhillforce acting upon the golf ball in
     * rest is
     * greater than the static friction, which would cause it to start rolling
     * again.
     * 
     * @return Boolean value: true if ball is about to start rolling again, false if
     *         not.
     */
    private boolean hill_is_steep() {
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), golfCourse);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), golfCourse);
        return uS <= Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2));
    }

    /**
     * This method determines wether the ball has fallen into water by evaluating
     * the golf course
     * function at the current position of the bal
     * 
     * @return Boolean value: true if ball is currently in water, false if not.
     */
    public boolean is_in_water() {
        if (Derivation.compute(vector.getXPos(), vector.getYPos(), golfCourse) < 0) {
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    public boolean is_in_water(double[] ballPos) {
        if (Derivation.compute(ballPos[0], ballPos[1], golfCourse) < 0) {
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    public boolean sandpitCollision(float x1, float x2, float y1, float y2, int c1, int c2) {
        double d = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        if (c1 > (d + c2)) {
            return true;
        }
        return false;
    }

    public double[] get_shot(double velX, double velY) {
        double[] variables = InputModule.get_input();
        uK = variables[0];
        uS = variables[1];
        targetX = variables[2];
        targetY = variables[3];
        targetRadius = variables[4];
        vector = new StateVector(variables[5], variables[6], velX, velY);
        golfCourse = InputModule.get_profile();
        RK2Solver solve = new RK2Solver(uK, uS, h, golfCourse);
        while ((ball_is_moving() && !ball_in_target() || hill_is_steep() && !ball_in_target()) && !is_in_water()) {
            vector = solve.runge_kutta_two(vector);
            if (abort) {
                break;
            }
        }
        return new double[] { vector.getXPos(), vector.getYPos() };
    }

    /**
     * This method aborts a golf ball shot in case of closing the program.
     */
    public void abort() {
        abort = true;
    }

    public List<Sandpit> get_sandpits(){
        return sandpits;
    }

    public void resume() {
        abort = false;
    }
}
