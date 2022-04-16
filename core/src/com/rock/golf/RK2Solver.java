package com.rock.golf;

public class RK2Solver {
    
    private final double h = 0.01;
    StateVector stateVector;
    
    //after each time step, h remains the same and t updates each step incrementing with the value of the step size
    // get input velocity
    // update variables
    public void RK2(){
        //k_i,1 = h_i*f(t_i, w_i)
        //k_i,2 = h_i*f(t_i + 1/2h_i, w_i + 1/2k_i,1)
        
        double t = 0, w = 0;
        double wX = stateVector.getXSpeed();
        double wY = stateVector.getYSpeed();

        double k1X = h*(double)functionX(t, wX);
        double k2X = h*(double)functionX(t + 1/2*h, wX + (1/2*k1X));
        wX = wX + k2X;

        double k1Y = h*(double)functionX(t, wY);
        double k2Y = h*(double)functionX(t + 1/2*h, wX + (1/2*k1Y));
        wY = wY + k2Y;
        stateVector = new StateVector(wX, wY, stateVector.getXSpeed(), stateVector.getYSpeed());
    }

    public double functionX(double t, double w){
        return 0;
    }
    
    public double functionY(double t, double w){
        return 0;
    }
        
}
