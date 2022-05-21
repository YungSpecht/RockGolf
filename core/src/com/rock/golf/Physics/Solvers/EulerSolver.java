package com.rock.golf.Physics.Solvers;

import com.rock.golf.Physics.Engine.StateVector;

import org.mariuszgromada.math.mxparser.Function;

public class EulerSolver extends Solver{

    public EulerSolver(double uK, double uS, double h, Function golfCourse) {
        super(uK, uS, h, golfCourse);
    }

    @Override
    public StateVector compute_step(StateVector vector) {
        return StateVector.add(vector, StateVector.multiply(function(vector), h));
    }
    
}
