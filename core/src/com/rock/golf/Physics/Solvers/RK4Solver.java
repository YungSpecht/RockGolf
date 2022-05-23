package com.rock.golf.Physics.Solvers;

import com.rock.golf.Physics.Engine.StateVector;

import org.mariuszgromada.math.mxparser.*;

public class RK4Solver extends Solver{

    public RK4Solver(double uK, double uS, double h, Function golfCourse) {
        super(uK, uS, h, golfCourse);
    }

    /**
     * use RK4 formula to calculate accelatartion (first derivateive of velocity) at
     * given point/state
     *
     * @param stateVector holds current state of the ball
     * @return            updated state vector after one RK4 iteration
     */
    @Override
    public StateVector computeStep(StateVector vector) {
        StateVector k1 = StateVector.multiply(function(vector), h);
        StateVector k2 = StateVector.multiply(function(StateVector.add(vector, StateVector.multiply(k1, 1.0 / 2.0))), h);
        StateVector k3 = StateVector.multiply(function(StateVector.add(vector, StateVector.multiply(k2, 1.0 / 2.0))), h);
        StateVector k4 = StateVector.multiply(function(StateVector.add(vector, k3)), h);

        // w(i+1) = w(i) + 1/6*(k1+2*k2+2*k3+k4) - final formula for RK4
        return StateVector.add(vector, StateVector.multiply(StateVector.add(StateVector.add(StateVector.multiply(k2, 2.0), StateVector.multiply(k3, 2.0)), StateVector.add(k1, k4)), 1.0 / 6.0));
    }
}