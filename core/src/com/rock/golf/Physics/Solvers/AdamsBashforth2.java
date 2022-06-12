package com.rock.golf.Physics.Solvers;

import com.rock.golf.Physics.Engine.StateVector;
import org.mariuszgromada.math.mxparser.*;

public class AdamsBashforth2 extends Solver{

    RK2Solver solve = new RK2Solver(uK, uS, h, golfCourse);

    public AdamsBashforth2(double uK, double uS, double h, Function golfCourse) {
        super(uK, uS, h, golfCourse);
    }
    
    @Override
    public StateVector computeStep(StateVector current) {
        StateVector initialValue = solve.computeStep(current);
        StateVector AdamsPredictor = StateVector.substract(StateVector.add(current, StateVector.multiply(function(initialValue), (3.0 / 2.0)*h)),  StateVector.multiply(function(current), (1.0 / 2.0)*h));
        StateVector AdamsCorrector = StateVector.substract(StateVector.add(StateVector.add(current, StateVector.multiply(function(AdamsPredictor), (5.0 / 12.0)*h)), StateVector.multiply(function(initialValue), (8.0/12.0)*h)),  StateVector.multiply(function(current), (1.0 / 2.0)*h));
        return AdamsCorrector;
    }
}
