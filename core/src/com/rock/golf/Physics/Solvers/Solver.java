package com.rock.golf.Physics.Solvers;

import com.rock.golf.Physics.Engine.Derivation;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.StateVector;

import org.mariuszgromada.math.mxparser.Function;

public abstract class Solver {
    protected double uK;
    protected double uS;
    protected double h;
    protected Function golfCourse;

    public Solver(double uK, double uS, double h, Function golfCourse){
        this.uK = uK;
        this.uS = uS;
        this.h = h;
        this.golfCourse = golfCourse;
    }

    public abstract StateVector compute_step(StateVector vector);

    protected StateVector function(StateVector vector) {
        double xSlope = Derivation.derivativeX(vector.getXPos(), vector.getYPos(), golfCourse);
        double ySlope = Derivation.derivativeY(vector.getXPos(), vector.getYPos(), golfCourse);
        double formulaX;
        double formulaY;
        if (vector.getXSpeed() == 0 && vector.getYSpeed() == 0) {
            formulaX = (-PhysicsEngine.g * xSlope) - uS * PhysicsEngine.g * (xSlope / Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2)));
            formulaY = (-PhysicsEngine.g * ySlope) - uS * PhysicsEngine.g * (ySlope / Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2)));
        } else {
            formulaX = (-PhysicsEngine.g * xSlope) - uK * PhysicsEngine.g * (vector.getXSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
            formulaY = (-PhysicsEngine.g * ySlope) - uK * PhysicsEngine.g * (vector.getYSpeed() / Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2)));
        }
        return new StateVector(vector.getXSpeed(), vector.getYSpeed(), formulaX, formulaY);
    }

    public void update_friction(double uK, double uS) {
        this.uK = uK;
        this.uS = uS;
    }
}
