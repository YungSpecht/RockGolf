package test;

import com.rock.golf.Physics.Engine.StateVector;
import com.rock.golf.Physics.Solvers.RK2Solver;

import org.mariuszgromada.math.mxparser.Function;

public class RKMethods {

    public StateVector Test_For_Flat_Surface_RK2(){
        Function golfcourse = new Function("h(x,y)=0");
        StateVector v1 = new StateVector(0, 0, 1, 0);
        RK2Solver solve = new RK2Solver(0.1, 0.2, 0.1, golfcourse);
        return solve.compute_step(v1);
    }

    public StateVector Test_For_Flat_Surface_RK4(){
        Function golfcourse = new Function("h(x,y)=0");
        StateVector v1 = new StateVector(0, 0, 1, 0);
        RK2Solver solve = new RK2Solver(0.1, 0.2, 0.1, golfcourse);
        return solve.compute_step(v1);
    }
}
