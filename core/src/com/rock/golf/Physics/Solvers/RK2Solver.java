package com.rock.golf.Physics.Solvers;

import com.rock.golf.Physics.Engine.StateVector;

import org.mariuszgromada.math.mxparser.*;

public class RK2Solver extends Solver{

    public RK2Solver(double uK, double uS, double h, Function golfCourse) {
        super(uK, uS, h, golfCourse);
    }

    /**
     * This method uses the Runge-Kutta 2 midpoint method to perform operations on a state
     * vector object in order to give us the updated position and speed of the golf ball.
     * 
     * @param current The state vector containing the current x- and y- positions and
     *                velocities.
     * @return        A state vector object containing the updated x- and y- positions and
     *                velocities after one timestep of size h.
     */
    @Override
    public StateVector compute_step(StateVector current) {
        // k1 = h * f(t, w);
        // k2 = h * f(t + 2/3*h, w + 2/3*k1)
        // w + h = w + 1/4*k1 + 3/4*k2

        StateVector k1 = StateVector.multiply(function(current), h);
        StateVector k2 = StateVector.multiply(function(StateVector.add(current, StateVector.multiply(k1, 2.0 / 3.0))), h);
        StateVector result = StateVector.add(StateVector.add(current, StateVector.multiply(k1, 1.0 / 4.0)),  StateVector.multiply(k2, 3 / 4));

        return result;
    }
}
