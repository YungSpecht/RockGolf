package com.rock.golf;

public class RK2 {
    final static double H = 1;
    final static double G = 9.81;
    
    static double uK = 0.1;
    static double uS = 0.2;

    //y(t + h) ≈ y(t) + hf(t + h/2, y(t) + h/2 f(t, y(t))).

    public static StateVector runge_kutta_two(StateVector current){
        StateVector temp = StateVector.add(current, euler(current, H/2));
        return StateVector.add(current, euler(temp, H));
    }

    private static StateVector euler(StateVector vector, double timeStep){
        double xSlope = 1;
        double ySlope = 1;
        double formulaX = -G * xSlope - uK  * G * (vector.getXSpeed() / Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2)));
        double formulaY = -G * ySlope - uK  * G * (vector.getYSpeed() / Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2)));
        return StateVector.add(vector, StateVector.multiply(new StateVector(vector.getXSpeed(), vector.getYSpeed(), formulaX, formulaY), H));
    }  
}
