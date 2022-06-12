package test;

import org.mariuszgromada.math.mxparser.Function;
import com.rock.golf.Physics.Engine.StateVector;
import com.rock.golf.Physics.Solvers.EulerSolver;
import com.rock.golf.Physics.Solvers.RK2Solver;
import com.rock.golf.Physics.Solvers.RK4Solver;

public class PlotTests {
    double bestH = 0.000001;
    double correctX = getAccurateValue().getXPos();
    double[] test = new double[]{1, 0.5, 0.1, 0.01, 0.001, 0.0001, 0.00001};
    public static void main(String[] args) {
        PlotTests test = new PlotTests();
        test.Test_RK4();
        System.out.println();
        test.Test_RK2();
        System.out.println();
        test.Test_Euler();
    }
    
    public void Test_RK4(){
        // X Position: 0.9509499999938991  Y Position: 0.0 || X Velocity: 0.9018999999877977  Y Velocity: 0.0
        Function golfcourse = new Function("h(x,y)=0");
        for(int i = 0; i < test.length; i++ ) {
            double h = test[i];
            StateVector v1 = new StateVector(0, 0, 1, 0);
            RK4Solver solve = new RK4Solver(0.01, 0.2, h, golfcourse);
            double t = (int) (1 / h);
            while (t != 0) {
                v1 = solve.computeStep(v1);
                t--;
            }
            System.out.print(Math.abs(correctX-v1.getXPos()) +",");
        }
    }

    public void Test_RK2(){
        // X Position: 0.9509499999938991  Y Position: 0.0 || X Velocity: 0.9018999999877977  Y Velocity: 0.0
        Function golfcourse = new Function("h(x,y)=0");
        for(int i = 0; i < test.length; i++ ) {
            double h = test[i];
            StateVector v1 = new StateVector(0, 0, 1, 0);
            RK2Solver solve = new RK2Solver(0.01, 0.2, h, golfcourse);
            double t = (int) (1 / h);
            while (t != 0) {
                v1 = solve.computeStep(v1);
                t--;
            }
            System.out.print(Math.abs(correctX-v1.getXPos()) +",");
        }
    }

    public void Test_Euler(){
        // X Position: 0.9509499999938991  Y Position: 0.0 || X Velocity: 0.9018999999877977  Y Velocity: 0.0
        
        Function golfcourse = new Function("h(x,y)=0");
        
        for(int i = 0; i < test.length; i++ ) {
            double h = test[i];
            StateVector v1 = new StateVector(0, 0, 1, 0);
            EulerSolver solve = new EulerSolver(0.01, 0.2, h, golfcourse);
            double t = (int) (1 / h);
            while (t != 0) {
                v1 = solve.computeStep(v1);
                t--;
            }
            System.out.print(Math.abs(correctX-v1.getXPos()) +",");
        }
    }

    public StateVector getAccurateValue(){
        Function golfcourse = new Function("h(x,y)=0");
        
        StateVector v1 = new StateVector(0, 0, 1, 0);
        RK4Solver solve = new RK4Solver(0.01, 0.2, bestH, golfcourse);
        int t = (int) (1 / bestH);
        while (t != 0) {
            v1 = solve.computeStep(v1);
            t--;
        }
        return v1;
    }

}
