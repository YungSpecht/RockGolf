import org.junit.jupiter.api.Test;

import test.TestDer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DerivativeTests {
    @Test public void test_for_XDerivative() {
        double calculated = TestDer.firstTest();
        assertEquals(0.1, calculated);
    }

    @Test public void test_error_for_XDerivative() {
        double calculated = TestDer.firstTest();
        assertEquals(0, calculated - 0.1);
    }
}
