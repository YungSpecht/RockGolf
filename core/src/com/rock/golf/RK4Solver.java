package com.rock.golf;

public class RK4Solver{

    private final double h =0.01;
    
    //TODO: add distinction between motion due to velocity vs. motion due to sloped surface
    //TODO: add acceleration formulas
    //TODO: modify f(t,w) method call/use in RK4 formula to include yslope/xslope  and xVelocity/ yVelocity (based on the acceleration formula)

    /**
     * 
     * use RK4 formula to calculate accelatartion (first derivateive of velocity) in both x direction and y direction at given time t
     *
     * @param stateVector 
     * @param xSlope 
     * @param ySlope
     * @param t
     * @return updated state vector
     */
    public StateVector RK4(StateVector stateVector, double xSlope, double ySlope, double t){
        //fX() = function for acceleration in x direction at the current time and velocity 
        //fy() = function for acceleration in y direction at the current time and velocity 
        double wX = stateVector.getXSpeed();
        double wY = stateVector.getYSpeed();

        double k1X = h*fx(t,wX);
        double k2X = h*fx(t + 1/2*h, wX+1/2*k1X);
        double k3X = h*fx(t + 1/2*h, wX+1/2*k2X);
        double k4X = h*fx(t + h, wX+k3X);
        wX = wX + 1/6*(k1X+2*k2X+2*k3X+k4X);

        double k1Y = h*fy(t,wY);
        double k2Y = h*fy(t + 1/2*h, wY+1/2*k1Y);
        double k3Y = h*fy(t + 1/2*h, wY+1/2*k2Y);
        double k4Y = h*fy(t + h, wY+k3Y);
        wY = wY + 1/6*(k1Y+2*k2Y+2*k3Y+k4Y);

        return new StateVector(stateVector.getXSpeed(), stateVector.getYSpeed(), wX, wY);

    }

    private double fx(double t, double w){
        //formula for acceleration
        return 0.0;
    }

    private double fy(double t, double w){
        //formula for acceleration
        return 0.0;

    }
    
}