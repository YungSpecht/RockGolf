import org.junit.jupiter.api.Test;

import test.TestDer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DerivativeTests {
    @Test public void test_for_XDerivative() {
        double calculated = TestDer.firstTest();
        boolean isEqual = TestDer.isEqual(0.1,calculated);
        assertTrue(isEqual);
    }

    @Test public void test_error_for_XDerivative() {
        double calculated = TestDer.firstTest();
        boolean isEqual = TestDer.isEqual(0, calculated - 0.1);
        assertTrue(isEqual);
    }
}
