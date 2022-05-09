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

    // Statevector is [0 0 0 0] cannot be tested, since the speed cannot be 0
    // if the speed is zero, we would be dividing by zero when using the eulers method

    @Test
    public void testEmpty1() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + s);
        Function input = InputModule.get_profile();
        RK2Solver solver = new RK2Solver(0, 0, input);
        StateVector current = new StateVector(0, 0, 1, 1);

        StateVector newVec = new StateVector(0.01, 0.010005, 99.99019, 1.0);
        //assertEquals();
    }

    @Test
    public void testJavaIsShit() {
        assertTrue(true);
    }
}
