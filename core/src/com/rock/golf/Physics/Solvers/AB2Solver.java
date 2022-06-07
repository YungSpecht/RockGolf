package com.rock.golf.Physics.Solvers;

import java.lang.Thread.State;

import com.rock.golf.Physics.Engine.StateVector;
import org.mariuszgromada.math.mxparser.Function;

public class AB2Solver extends Solver {

    public AB2Solver(double uK, double uS, double h, Function golfCourse) {
        super(uK, uS, h, golfCourse);
    
    }

    

    @Override
    public StateVector computeStep(StateVector vector) {
        double new_h = h/2;
        
        return StateVector.add(vector, StateVector.multiply(StateVector.,new_h )));
    }
    
}
