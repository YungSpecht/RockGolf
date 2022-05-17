package com.rock.golf;

import org.mariuszgromada.math.mxparser.Function;
import java.util.ArrayList;
import java.util.List;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Math.Derivation;
import com.rock.golf.Math.RK2Solver;
import com.rock.golf.Math.RK4Solver;

public class PhysicsEngine implements Runnable {

    // constants
    public static final double g = 9.81;
    public double h = 0.03;
    public final double ballRadius = 0.05;

    // fields
    private double uK, uS;
    private double targetX, targetY, targetRadius;
    private Function golfCourse;
    private StateVector vector;
    private double[] input;
    private boolean abort;
    private List<Sandpit> sandpits;
    private RK2Solver solver2;
    private RK4Solver solver4;
    private char rkMode = 'h';

    // constructor
    public PhysicsEngine() {
        input = InputModule.get_input();
        set_variables();
        abort = false;
        sandpits = new ArrayList<Sandpit>();
        //sandpits.add(new Sandpit(new double[]{-4, 4}, 1, uK, uS));
    }

    // Bot constructor
    public PhysicsEngine(double h, char rkMode) {
        this.rkMode=rkMode;
        input = InputModule.get_input();
        set_variables();
        abort = false;
        this.h = h;
        sandpits = new ArrayList<Sandpit>();
        //sandpits.add(new Sandpit(new double[]{-4, 4}, 1, uK, uS));
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
        switch(rkMode){
            case 'l' : solver2 = new RK2Solver(uK, uS, h, golfCourse); break;
            case 'h' : solver4 = new RK4Solver(uK, uS, h, golfCourse); break;
        }
        Double step = h * 1000;
        long timestep = step.longValue();
        long checkpoint = System.currentTimeMillis();
        while ((ball_is_moving() && !ball_in_target() || hill_is_steep() && !ball_in_target()) && !is_in_water(new double[]{vector.getXPos(), vector.getYPos()}) && ball_in_screen(new double[]{vector.getXPos(), vector.getYPos()})) {
            long currentTime = System.currentTimeMillis();
            if(currentTime - checkpoint > timestep){
                Sandpit currentSandpit = current_sandpit();
                if(currentSandpit != null){
                    switch(rkMode){
                        case 'l' : solver2.update_friction(currentSandpit.get_uK(), currentSandpit.get_uS()); break;
                        case 'h' : solver4.update_friction(currentSandpit.get_uK(), currentSandpit.get_uS()); break;
                    }
                }
                else{
                    switch(rkMode){
                        case 'l' : solver2.update_friction(uK, uS); break;
                        case 'h' : solver4.update_friction(uK, uS); break;
                    }
                }
                switch(rkMode){
                    case 'l' : vector = solver2.runge_kutta_two(vector); break;
                    case 'h' : vector = solver4.RK4(vector); break;
                }
                RockGolf.update_position(vector);
                checkpoint = System.currentTimeMillis();
            }
            if (abort) {
                break;
            }
        }
        InputModule.set_new_position(vector.getXPos(), vector.getYPos());
        RockGolf.shotActive = false;
    }

    /**
     * This method simulates shots for the Bots without updating the UI.
     * 
     * @param velX The initial x velocity of the shot.
     * @param velY The initial y velocity of the shot
     * @return     Array containing the final x and y position resulting
     *             from the shot.
     */
    public double[] get_shot(double velX, double velY){
        double[] variables = InputModule.get_input();
        uK = variables[0]; uS = variables[1];
        targetX = variables[2]; targetY = variables[3]; targetRadius = variables[4];
        vector = new StateVector(variables[5], variables[6], velX, velY);
        golfCourse = InputModule.get_profile();
        switch(rkMode){
            case 'l' : solver2 = new RK2Solver(uK, uS, h, golfCourse); break;
            case 'h' : solver4 = new RK4Solver(uK, uS, h, golfCourse); break;
        }
        while((ball_is_moving() && !ball_in_target() || hill_is_steep() && !ball_in_target()) && !is_in_water(new double[]{vector.getXPos(), vector.getYPos()}) && ball_in_screen(new double[]{vector.getXPos(), vector.getYPos()})){
            Sandpit currentSandpit = current_sandpit();
            if(currentSandpit != null){
                switch(rkMode){
                    case 'l' : solver2.update_friction(currentSandpit.get_uK(), currentSandpit.get_uS()); break;
                    case 'h' : solver4.update_friction(currentSandpit.get_uK(), currentSandpit.get_uS()); break;
                }
            }
            else{
                switch(rkMode){
                    case 'l' : solver2.update_friction(uK, uS); break;
                    case 'h' : solver4.update_friction(uK, uS); break;
                }
            }
            switch(rkMode){
                case 'l' : vector = solver2.runge_kutta_two(vector); break;
                case 'h' : vector = solver4.RK4(vector); break;
            }
            if(abort){
                break;
            }
        }
        return new double[]{vector.getXPos(), vector.getYPos()};
    }

    /**
     * This method determines wether the ball is currently inside a sandpit.
     * 
     * @return The sandpit the ball is currently in, otherwise null.
     */
    private Sandpit current_sandpit(){
        for(int i = 0; i < sandpits.size(); i++){
            if(sandpits.get(i).is_in_sandpit(vector.getXPos(), vector.getYPos())){
                return sandpits.get(i);
            }
        }
        return null;
    }

    /**
     * This method reads in all the parameters from the Input.txt file and updates
     * the fields of the physics engine accordingly.
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

    /**
     * This method determines wether the ball is currently moving.
     * 
     * @return Boolean value: true if ball is moving, false if not.
     */
    private boolean ball_is_moving() {
        if (Math.abs(Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2))) < h) {
            vector.setXSpeed(0);
            vector.setYSpeed(0);
            return false;
        }
        return true;
    }

    /**
     * This method determines wether the ball is currently inside the target based
     * on the position and radius of the ball as well as the target.
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
     * This method determines wether the ball is currently in a body of water.
     * 
     * @param ballPos Array containing the current x and y position of the ball.
     * @return        Boolean value: true if ball is inside target, false if not.
     */
    public boolean is_in_water(double[] ballPos) {
        if (Derivation.compute(ballPos[0], ballPos[1], golfCourse) < 0) {
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    /**
     * This method determines wether the downhillforce acting upon the golf ball in rest
     * is greater than the static friction, which would cause it to start rolling again.
     * 
     * @return Boolean value: true if ball is about to start rolling again, false if not.
     */
    private boolean hill_is_steep() {
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), golfCourse);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), golfCourse);
        return uS <= Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2));
    }

    /**
     * This method determines wether the ball is currently located inside of the bounds
     * of the graphical application.
     * 
     * @param ballPos Array containing the current x and y position of the ball.
     * @return        Boolean value: true if ball is about to start rolling again, false if not.
     */
    public boolean ball_in_screen(double[] ballPos) {
        boolean xIn = ballPos[0] < ((RockGolf.width/2)/RockGolf.metertoPixelRatio) && ballPos[0] > -((RockGolf.width/2)/RockGolf.metertoPixelRatio);
        boolean yIn = ballPos[1] < ((RockGolf.height/2)/RockGolf.metertoPixelRatio) && ballPos[1] > -((RockGolf.height/2)/RockGolf.metertoPixelRatio);
        return xIn && yIn;
    }

    public double[] get_input() {
        return input;
    }

    public StateVector getVector() {
        return vector;
    }

    /**
     * This method aborts a golf ball shot in case of closing the program.
     */
    public void abort() {
        abort = true;
    }

    public void resume(){
        abort = false;
    }

    public List<Sandpit> get_sandpits(){
        return sandpits;
    }

}
