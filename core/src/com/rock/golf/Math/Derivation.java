package com.rock.golf.Math;

import org.mariuszgromada.math.mxparser.*;

public class Derivation {
    private static double h = 0.000000000000001;

    public static double derivativeX(double x, double y, Function f) {
        return (f.calculate(x+h, y) - f.calculate(x, y)) / h;
    }

    public static double derivativeY(double x, double y, Function f) {
        return (f.calculate(x, y+h) - f.calculate(x, y)) / h;
    }

    public static double compute(double x, double y, Function f) {
        return f.calculate(x, y);
    }
}