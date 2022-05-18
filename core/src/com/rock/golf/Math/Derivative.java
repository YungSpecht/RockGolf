package com.rock.golf.Math;

public class Derivative {
    private static double h = 0.000000000000001;

    private static double f(double x, double y){
        return 0.4 * (0.9 - Math.pow(Math.E, -((Math.pow(x, 2) + Math.pow(y, 2)) / 8)));
    }

    public static double derivativeX(double x, double y){
        return (f(x+h, y) - f(x, y)) / h;
    }

    public static double derivativeY(double x, double y){
        return (f(x, y+h) - f(x, y)) / h;
    }
}
