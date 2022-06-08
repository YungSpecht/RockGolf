package test;

import org.mariuszgromada.math.mxparser.Function;
import com.rock.golf.Physics.Engine.Derivation;

public class TestDer {

    public static double firstTest() {
        org.mariuszgromada.math.mxparser.Function example = (org.mariuszgromada.math.mxparser.Function) new Function(
                "h(x, y) = 0.1*x+1");
        return Derivation.derivativeX(1.0, 2.0, example);
    }

    public static boolean isEqual(double a, double b) {
        double epsilon = 0.001;
        boolean isEqual = Math.abs(a - b) < epsilon;
        return isEqual;
    }
}
