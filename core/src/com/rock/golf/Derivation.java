package com.rock.golf;

import org.mariuszgromada.math.mxparser.*;
import org.mariuszgromada.math.mxparser.mathcollection.*;

public class Derivation {
    public static double derivativeX(double x, double y, Function f) {
        Argument x1 = new Argument("x1", x);
        Argument y1 = new Argument("y1", y);
        Expression e1 = new Expression("h(x1,y1)", f, x1, y1);
        return Calculus.derivative(e1, x1, x, 3, 0.00000000000000001, 10);
    }

    public static double derivativeY(double x, double y, Function f) {
        Argument x1 = new Argument("x1", x);
        Argument y1 = new Argument("y1", y);
        Expression e1 = new Expression("h(x1,y1)", f, x1, y1);
        return Calculus.derivative(e1, y1, y, 3, 0.00000000000000001, 10);
    }

    public static double compute(double x, double y, Function f) {
        return f.calculate(x,y);
    }
}