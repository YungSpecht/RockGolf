// package com.rock.golf.Testing;

// import static org.junit.Assert.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.Test;
// import java.util.function.Function;
// import com.rock.golf.StateVector;
// import com.rock.golf.Input.InputModule;
// import com.rock.golf.Math.RK2Solver;

// public class RKMethods {
    
//     @Test
//     public void RK2_Method_Test(){
//         Function golfcourse = (Function) InputModule.get_profile();
//         RK2Solver solve = new RK2Solver(0.1, 0.2, 0.1, (org.mariuszgromada.math.mxparser.Function) golfcourse);
//         StateVector vector = new StateVector(1.825, 0, -1.7903, 0);
//         assertEquals(vector, solve.runge_kutta_two(vector));
//     }
// }
