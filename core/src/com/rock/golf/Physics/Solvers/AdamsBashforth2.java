package com.rock.golf.Physics.Solvers;

import com.rock.golf.Physics.Engine.StateVector;
import org.mariuszgromada.math.mxparser.*;

public class AdamsBashforth2 extends Solver{

    RK4Solver solve = new RK4Solver(uK, uS, h, golfCourse);
    int counter = 0;
    StateVector previousState;
    public AdamsBashforth2(double uK, double uS, double h, Function golfCourse) {
        super(uK, uS, h, golfCourse);
    }

    
    @Override
    public StateVector computeStep(StateVector current) {
        // wi+1 = wi + (h/2)(3f(ti,wi) −f(ti−1,wi−1)).
        // wi+1 = wi + (h/12)(5f(ti+1,wi+1) + 8f(ti,wi) −f(ti−1,wi−1))).
         
        
        if(counter == 0) {
            StateVector initialV = solve.computeStep(current);
            previousState = initialV;
            counter++;
            return initialV;
        } else {
            StateVector predictor = StateVector.substract(StateVector.add(current, StateVector.multiply(function(current), (3.0 * h / 2.0))),  StateVector.multiply(function(previousState), (1.0 *h / 2.0)));
            StateVector corrector = StateVector.substract(StateVector.add(StateVector.add(current, StateVector.multiply(function(predictor), (5.0 *h / 12.0))), StateVector.multiply(function(current), (8.0 *h/12.0))),  StateVector.multiply(function(previousState), (1.0 * h / 12.0)));
            previousState = corrector;
            return corrector;
        }
    }

}
