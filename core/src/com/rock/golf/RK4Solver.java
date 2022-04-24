package com.rock.golf;

import org.mariuszgromada.math.mxparser.Function;

public class RK4Solver{

    private final double h;
    private final double uK, uS;
    private final Function f;
    private final static double G = 9.81;


    public RK4Solver(Function f, double h, double uK, double uS){
        this.f = f;
        this.h = h;
        this.uK = uK;
        this.uS = uS;
    }

    //TODO: add distinction between motion due to velocity vs. motion due to sloped surface
   

    /**
     * 
     * use RK4 formula to calculate accelatartion (first derivateive of velocity) at given time t
     *
     * @param stateVector 
     * @param t
     * @return updated state vector
     */
    public StateVector RK4(StateVector stateVector, double t){
        

        StateVector k1 = StateVector.multiply(function(stateVector),h);
        StateVector k2 = StateVector.multiply(function(StateVector.add(stateVector,StateVector.multiply(k1, 1/2))),h);
        StateVector k3 = StateVector.multiply(function(StateVector.add(stateVector,StateVector.multiply(k2, 1/2))),h);
        StateVector k4 = StateVector.multiply(function(StateVector.add(stateVector,k3)),h);


        //w(i+1) = w(i) + 1/6*(k1+2*k2+2*k3+k4) - final formula for RK4 
       return StateVector.add(stateVector,StateVector.multiply(StateVector.add(StateVector.add(StateVector.multiply(k2,2),StateVector.multiply(k3,2)),StateVector.add(k1,k4)), 1/6));

    

    }

    /**
     * 
     * @param stateVector
     * @return stateVector after one function evaluation
     */
    private StateVector function(StateVector stateVector){
        
        double xSlope = Derivation.derivativeX(stateVector.getXPos(), stateVector.getYPos(), f);
        double ySlope = Derivation.derivativeY(stateVector.getXPos(), stateVector.getYPos(), f);
        double formulaX = (-G * xSlope) - uK  * G * (stateVector.getXSpeed() / Math.sqrt(Math.pow(stateVector.getXSpeed(), 2) + Math.pow(stateVector.getYSpeed(), 2)));
        double formulaY = (-G * ySlope) - uK  * G * (stateVector.getYSpeed() / Math.sqrt(Math.pow(stateVector.getXSpeed(), 2) + Math.pow(stateVector.getYSpeed(), 2)));

        return new StateVector(stateVector.getXSpeed(), stateVector.getYSpeed(), formulaX, formulaY);
    }

    
    
}