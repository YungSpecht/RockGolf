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
        StateVector k1 = StateVector.multiply(function(current), h);
        StateVector k2 = StateVector.multiply(function(StateVector.add(current, StateVector.multiply(k1, 2.0/3.0))), h);
        StateVector result = StateVector.add(StateVector.add(current, StateVector.multiply(k1, 1.0/4.0)), StateVector.multiply(k2, 3/4));
        return result;
    }

    private double derivativeX(double x, double y){
        double h = 0.000000000000001;
        return (f(x+h, y) - f(x, y))/h;
    }

    private double derivativeY(double x, double y){
        double h = 0.000000000000001;
        return (f(x, y+h) - f(x, y))/h;
    }


    private double f(double x, double y){
        return 0.4 * (0.9 - Math.pow(Math.E, -((Math.pow(x, 2)+Math.pow(y, 2))/8)));
    }

    public void update_friction(double uK, double uS){
        this.uK = uK;
        this.uS = uS;
    }

    private StateVector function(StateVector vector){
        double xSlope = derivativeX(vector.getXPos(), vector.getYPos());
        double ySlope = derivativeY(vector.getXPos(), vector.getYPos());
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
