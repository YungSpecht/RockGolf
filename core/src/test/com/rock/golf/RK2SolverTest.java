package com.rock.golf;

import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.rock.golf.StateVector;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Math.RK2Solver;

public class RK2SolverTest {

    // Statevector is [0 0 0 0]
    @Test
    public void testEmpty() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + s);
        Function input = InputModule.get_profile();
        RK2Solver solver = new RK2Solver(0, 0, input);
        StateVector current = new StateVector(0, 0, 0, 0);
        assertEquals(current, solver.runge_kutta_two(current));
    }

    @Test
    public void testJavaIsShit() {
        assertTrue(true);
    }
}
