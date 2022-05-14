package com.rock.golf.Math;

import org.mariuszgromada.math.mxparser.Function;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;

public class RK4Solver {

    private final double h;
    private final double uK, uS;
    private final Function golfCourse;

    public RK4Solver(Function golfCourse, double h, double uK, double uS) {
        this.golfCourse = golfCourse;
        this.h = h;
        this.uK = uK;
        this.uS = uS;
    }

    /**
     * 
     * use RK4 formula to calculate accelatartion (first derivateive of velocity) at
     * given point/state
     *
     * @param stateVector holds current state of the ball
     * @return            updated state vector after one RK4 iteration
     */
    public StateVector RK4(StateVector stateVector) {

        StateVector k1 = StateVector.multiply(function(stateVector), h);
        StateVector k2 = StateVector.multiply(function(StateVector.add(stateVector, StateVector.multiply(k1, 1.0/2.0))), h);
        StateVector k3 = StateVector.multiply(function(StateVector.add(stateVector, StateVector.multiply(k2, 1.0/2.0))), h);
        StateVector k4 = StateVector.multiply(function(StateVector.add(stateVector, k3)), h);

        // w(i+1) = w(i) + 1/6*(k1+2*k2+2*k3+k4) - final formula for RK4
        return StateVector.add(stateVector, StateVector.multiply(StateVector.add(StateVector.add(StateVector.multiply(k2, 2.0), StateVector.multiply(k3, 2.0)), StateVector.add(k1, k4)), 1.0/6.0));
    }

    /**
     * evaluates acceleration function at given state/point 
     * @param stateVector holds current state of the ball
     * @return stateVector after one function evaluation
     */
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