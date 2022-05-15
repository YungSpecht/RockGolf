package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.rock.golf.StateVector;

public class NumericalTests {

    RKMethods computationEngine = new RKMethods();

    @Test
    public void Test_For_Flat_Surface_RK2(){
        StateVector vector = new StateVector(1.825, 0, -1.7903, 0);
        StateVector testedVector = computationEngine.Test_For_Flat_Surface_RK2();
        assertEquals(vector, testedVector);
    }

    // @Test
    // public void Test_For_Slope_RK2(){
    //     Function golfcourse = new Function("h(x,y)=0.1*x");
    //     StateVector v1 = new StateVector(0, 0, 2, 0);
    //     RK2Solver solve = new RK2Solver(0.05, 0.2, 0.1, golfcourse);
    //     StateVector vector = new StateVector(2.21,0,-0.6377,0);
    //     assertEquals(vector, solve.runge_kutta_two(v1));
    // }

    // @Test
    // public void Test_For_Exponential_Slope_RK2(){
    //     Function golfcourse = new Function("0.4*(0.9 -e^(-(x^2+y^2)/8))");
    //     StateVector v1 = new StateVector(0, 0, 3, 0);
    //     RK2Solver solve = new RK2Solver(0.08, 0.2, 0.1, golfcourse);
    //     StateVector vector = new StateVector(3.275,0,-1.6550,0.4585);
    //     assertEquals(vector, solve.runge_kutta_two(v1));
    // }

}