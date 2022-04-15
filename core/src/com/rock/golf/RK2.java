package com.rock.golf;

public class RK2 {
    final double h = 1;

    //y(t + h) ≈ y(t) + hf(t + h/2, y(t) + h/2 f(t, y(t))).

    public static StateVector runge_kutta_two(StateVector current){
        StateVector wZero = calc_wZero(current);


        return null;
    }

    private static StateVector calc_wZero(StateVector vector){
        //TODO: calculate the second argument (stateVector)
        return null;
    }

    private static StateVector add(StateVector one, StateVector two){
        double first = one.getXPos() + two.getXPos();
        double second = one.getYPos() + two.getYPos();
        double third = one.getXSpeed() + two.getXSpeed();
        double fourth = one.getYSpeed() + two.getYSpeed();
        return new StateVector(first, second, third, fourth);
    }

    private static StateVector multiply(StateVector a, double b){
        return new StateVector(a.getXPos() * b, a.getYPos() * b, a.getXSpeed() * b, a.getYSpeed() * b);
    }

    private static StateVector euler(StateVector vector, double timeStep){
        //TODO: reimplement euler using the new stateVector class

        return null;
    }
    
}
