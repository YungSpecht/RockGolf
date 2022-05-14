package com.rock.golf.Math;

import com.rock.golf.PhysicsEngine;
import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;

import org.mariuszgromada.math.mxparser.*;

/**
 * runge_kutta_two()
 *
 * @param current current statevector of the golfball
 * @return the new vector of the golfball, calculated with RK2
 *
 *
 *         euler()
 * @param vector   current statevector of the golfball
 * @param timeStep time between each step in the euler method
 *                 the bigger the timestep, the more accurate the euler method
 *                 the bigger the timestep, the slower the euler method
 * @return the new statevector of the golfball, calculated with euler
 */

public class RK2Solver {
    // y(t + h) ≈ y(t) + hf(t + h/2, y(t) + h/2 f(t, y(t)))

    private double uK;
    private double uS;
    private Function golfCourse;

    public RK2Solver(double uK, double uS, Function golfCourse) {
        this.uK = uK;
        this.uS = uS;
        this.golfCourse = golfCourse;
    }

    public StateVector runge_kutta_two(StateVector current) { // [0 0 1 1] == [xPosition yPosition xSpeed ySpeed]
        StateVector temp = StateVector.add(current, euler(current, PhysicsEngine.h / 2));
        return StateVector.add(current, euler(temp, PhysicsEngine.h));
    }

    private StateVector euler(StateVector vector, double timeStep) { // [0 0 1 1] and 0.01
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), golfCourse);
        System.out.println(xSlope);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), golfCourse);
        System.out.println(ySlope);
        double formulaX = (-PhysicsEngine.g * xSlope) - uK * PhysicsEngine.g
                * (vector.getXSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
        System.out.println(formulaX);
        double formulaY = (-PhysicsEngine.g * ySlope) - uK * PhysicsEngine.g
                * (vector.getYSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
        System.out.println(formulaY);
        return StateVector.multiply(new StateVector(vector.getXSpeed(), vector.getYSpeed(), formulaX, formulaY),
                PhysicsEngine.h);
    }

    public static void main(String[] args) {
        StateVector current = new StateVector(0, 0, 1, 1);
        Function function = InputModule.get_profile();
        RK2Solver solver1 = new RK2Solver(0.1, 0.1, function);
        StateVector temp = StateVector.add(current, solver1.euler(current, PhysicsEngine.h / 2));
        // System.out.println("\n"+ temp);
        // System.out.println("\n"+ solver1.runge_kutta_two(current));
    }
}
