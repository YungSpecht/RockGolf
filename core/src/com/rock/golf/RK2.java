package com.rock.golf;

import org.mariuszgromada.math.mxparser.*;

public class RK2 {
    //y(t + h) ≈ y(t) + hf(t + h/2, y(t) + h/2 f(t, y(t)))

    static Function heightProfile = new Function("h(x,y)=0");
    final static double H = 0.1;
    final static double G = 9.81;
    
    static double uK = 0.1;
    static double uS = 0.2;

    public static StateVector runge_kutta_two(StateVector current){
        StateVector temp =  StateVector.add(current, euler(current, H/2));
        return StateVector.add(current, euler(temp, H));
    }

    private static StateVector euler(StateVector vector, double timeStep){
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), heightProfile);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), heightProfile);
        double formulaX = (-G * xSlope) - uK  * G * (vector.getXSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
        double formulaY = (-G * ySlope) - uK  * G * (vector.getYSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
        return StateVector.multiply(new StateVector(vector.getXSpeed(), vector.getYSpeed(), formulaX, formulaY), H);
    }
    public static void main(String[] args) {
        StateVector test = new StateVector(0, 0, 1, 0);
        System.out.println(runge_kutta_two(test));
    }
}
