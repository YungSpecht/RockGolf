package test;

import org.mariuszgromada.math.mxparser.Function;
import com.rock.golf.Physics.Engine.StateVector;
import com.rock.golf.Physics.Solvers.AB2Solver;
import com.rock.golf.Physics.Solvers.AdamsBashforth2;
import com.rock.golf.Physics.Solvers.EulerSolver;
import com.rock.golf.Physics.Solvers.RK2Solver;
import com.rock.golf.Physics.Solvers.RK4Solver;

public class PlotTests {
    double tMax = 1.5;
    double bestH = 0.00001;
    double correctX = getAccurateValue().getXPos();
    double[] test = new double[]{0.5, 0.3, 0.25, 0.125, 0.1, 0.05, 0.01, 0.005, 0.001};
    public static void main(String[] args) {
        PlotTests test = new PlotTests();
        test.Test_AB();
    }
    
    public void Test_RK4(){

        Function golfcourse = new Function("h(x,y)=e^((-x^2+y^2)/(40))");
        for(int i = 0; i < test.length; i++ ) {
            double h = test[i];
            StateVector v1 = new StateVector(0, 0, 1, 0);
            RK4Solver solve = new RK4Solver(0.01, 0.2, h, golfcourse);
            double t = (int) (tMax / h);
            while (t != 0) {
                v1 = solve.computeStep(v1);
                t--;
            }
            System.out.print(Math.abs((correctX-v1.getXPos())/correctX) +",");
        }
    }

    public void Test_RK2(){

        Function golfcourse = new Function("h(x,y)=e^((-x^2+y^2)/(40))");
        for(int i = 0; i < test.length; i++ ) {
            double h = test[i];
            StateVector v1 = new StateVector(0, 0, 1, 0);
            RK2Solver solve = new RK2Solver(0.01, 0.2, h, golfcourse);
            double t = (int) (tMax / h);
            while (t != 0) {
                v1 = solve.computeStep(v1);
                t--;
            }
            System.out.print(Math.abs((correctX-v1.getXPos())/correctX) +",");
        }
    }

    public void Test_Euler(){
        
        Function golfcourse = new Function("h(x,y)=e^((-x^2+y^2)/(40))");
        
        for(int i = 0; i < test.length; i++ ) {
            double h = test[i];
            StateVector v1 = new StateVector(0, 0, 1, 0);
            EulerSolver solve = new EulerSolver(0.01, 0.2, h, golfcourse);
            double t = (int) (tMax / h);
            while (t != 0) {
                v1 = solve.computeStep(v1);
                t--;
            }
            System.out.print(Math.abs((correctX-v1.getXPos())/correctX) +",");
        }
    }

    public void Test_AB(){
        
        Function golfcourse = new Function("h(x,y)=e^((-x^2+y^2)/(40))");
        
        for(int i = 0; i < test.length; i++ ) {
            double h = test[i];
            StateVector v1 = new StateVector(0, 0, 1, 0);
            AB2Solver solve = new AB2Solver(0.01, 0.2, h, golfcourse);
            double t = (int) (tMax / h);
            while (t != 0) {
                v1 = solve.computeStep(v1);
                t--;
            }
            System.out.print(Math.abs((correctX-v1.getXPos())/correctX) +",");
        }
    }

    public StateVector getAccurateValue(){
        Function golfcourse = new Function("h(x,y)=e^((-x^2+y^2)/(40))");
        
        StateVector v1 = new StateVector(0, 0, 1, 0);
        RK4Solver solve = new RK4Solver(0.01, 0.2, bestH, golfcourse);
        int t = (int) (tMax / bestH);
        while (t != 0) {
            v1 = solve.computeStep(v1);
            t--;
        }
        return v1;
    }

}
