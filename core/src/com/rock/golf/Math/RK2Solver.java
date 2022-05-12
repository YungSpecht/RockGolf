package com.rock.golf.Math;

import org.mariuszgromada.math.mxparser.*;
import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;

public class RK2Solver {
    
    // y(t + h) ≈ y(t) + hf(t + h/2, y(t) + h/2 f(t, y(t)))

    private double uK;
    private double uS;
    private double h;
    private Function golfCourse;

    public RK2Solver(double uK, double uS, double h, Function golfCourse) {
        this.uK = uK;
        this.uS = uS;
        this.h = h;
        this.golfCourse = golfCourse;
    }

    public static void main(String[] args) {
        Function func = new Function("h(x,y)=0");
        StateVector current = new StateVector(0, 0, 2, 2);
        RK2Solver solv = new RK2Solver(0.1, 0.2, 0.01, func);
        System.out.println(solv.runge_kutta_two(current));

    }

    /**
	 * This method uses the Runge-Kutta 2 midpoint method to perform operations on a state vector
     * object in order to give us the updated position and speed of the golf ball.
     * 
	 * @param current The state vector containing the current x- and y- positions and velocities.
	 * @return A state vector object containing the updated x- and y- positions and velocities after one timestep of size h.
	 */
    public StateVector runge_kutta_two(StateVector current) {
        //k1 = h * f(t, w);
        //k2 = h * f(t + 2/3*h, w + 2/3*k1)
        //w + h = w + 1/4*k1 + 3/4*k2

        //h/2 f(t, y(t))
        StateVector vecOne = StateVector.multiply(function(current), h/2);

        //y(t) + h/2 f(t, y(t))
        StateVector vecTwo = StateVector.add(current, vecOne);

        //f(t + h/2, y(t) + h/2 f(t, y(t)))
        StateVector vecThree = function(vecTwo);

        //hf(t + h/2, y(t) + h/2 f(t, y(t)))
        StateVector vecFour = StateVector.multiply(vecThree, h);

        //y(t) + hf(t + h/2, y(t) + h/2 f(t, y(t)))
        StateVector result = StateVector.add(current, vecFour);

        /*
        StateVector k1 = StateVector.multiply(function(current), h);
        StateVector k2 = StateVector.multiply(function(StateVector.add(current, StateVector.multiply(k1, 2/3))), h);
        StateVector result = StateVector.add(StateVector.add(current, StateVector.multiply(k1, 1/4)), StateVector.multiply(k2, 3/4));
        */
        return result;
    }

    private StateVector function(StateVector vector){
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), golfCourse);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), golfCourse);
        double formulaX;
        double formulaY;
        if(vector.getXSpeed() == 0 && vector.getYSpeed() == 0){
            formulaX = (-PhysicsEngine.g * xSlope) - uS * PhysicsEngine.g * (xSlope / Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2)));
            formulaY = (-PhysicsEngine.g * ySlope) - uS * PhysicsEngine.g * (ySlope / Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2)));
        }
        else{
            formulaX = (-PhysicsEngine.g * xSlope) - uK * PhysicsEngine.g * (vector.getXSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
            formulaY = (-PhysicsEngine.g * ySlope) - uK * PhysicsEngine.g * (vector.getYSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
        }
        return new StateVector(vector.getXSpeed(), vector.getYSpeed(), formulaX, formulaY);
    }
}
