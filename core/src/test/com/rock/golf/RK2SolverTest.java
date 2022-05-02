package test;

import main.com.rock.golf.StateVector;
import main.com.rock.golf.Math.RK2Solver;
import org.junit.Test;
import static org.junit.Assert.*;

class RK2SolverTest {

    // Statevector is [0 0 0 0]
    @Test
    public void testEmpty() {
        StateVector current = new StateVector(0, 0, 0, 0);
        assertEquals(current, RK2Solver.runge_kutta_two(current));
    }
}
