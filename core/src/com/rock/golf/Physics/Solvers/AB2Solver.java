package com.rock.golf.Physics.Solvers;

import com.rock.golf.Physics.Engine.StateVector;
import org.mariuszgromada.math.mxparser.Function;

public class AB2Solver extends Solver {

    public AB2Solver(double uK, double uS, double h, Function golfCourse) {
        super(uK, uS, h, golfCourse);

    }

    @Override
    public StateVector computeStep(StateVector vector) {
        StateVector f_ti_wi = function(vector);
        double new_h = h / 2;
        double new_new_h = h / 12;
        StateVector adamscalculation = StateVector.add(vector,
                StateVector.multiply(StateVector.substract(StateVector.multiply(f_ti_wi, 3), vector), new_h));
        StateVector adamsolver = StateVector.add(f_ti_wi, StateVector.multiply(StateVector.substract(
                StateVector.add(StateVector.multiply(adamscalculation, 5), StateVector.multiply(function(f_ti_wi), 8)),
                vector), new_new_h));
        return adamsolver;
    }
}
