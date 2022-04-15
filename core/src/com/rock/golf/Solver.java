package com.rock.golf;

public class Solver {
    
    private final double h = 0.01;
    
    
    //after each time step, h remains the same and t updates each step incrementing with the value of the step size
    // get input velocity
    public void RK2(){
        //k_i,1 = h_i*f(t_i, w_i)
        //k_i,2 = h_i*f(t_i + 1/2h_i, w_i + 1/2k_i,1)
        double t = 0, w = 0;
        double k1 = h*(double)function(t, w);
        double k2 = h*(double)function(t + 1/2*h, w + (1/2*k1));
        StateVector stateVector = new StateVector(0, 0, 0, 0);
    }

    public double function(double t, double w){
        return 0;
    }
    
    
    
}
