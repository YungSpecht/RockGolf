package com.rock.golf.Math;

import org.mariuszgromada.math.mxparser.*;
import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;

public class RK2Solver {
    
    // y(t + h) ≈ y(t) + hf(t + h/2, y(t) + h/2 f(t, y(t)))

    private double uK;
    private double uS;
    private Function golfCourse;

    public RK2Solver(double uK, double uS, Function golfCourse) {
        this.uK = uK;
        this.uS = uS;
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
        StateVector temp = StateVector.add(current, euler(current, PhysicsEngine.h / 2));
        return StateVector.add(current, euler(temp, PhysicsEngine.h));
    }

    /**
	 * This method does operations on a state vector object to calculate the change in position and speed of the golf
     * ball using the euler method.
     * 
	 * @param vector A state vector object containing the current x- and y- positions and velocities.
	 * @return A state vector object that contains information about the change in speed and position. Needs to be added
     * with the current state vecotr object to give us the new positions and velocities.
	 */
    private StateVector euler(StateVector vector, double timeStep) {
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

        return StateVector.multiply(new StateVector(vector.getXSpeed(), vector.getYSpeed(), formulaX, formulaY), timeStep);
    }
}
