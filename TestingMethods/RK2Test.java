import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RK2Test {

    @Test
    void input(){
        Function golfcourse = InputModule.get_profile;
        RK2Solver solve = new RK2Solver(0.1, 0.2, 0.1, golfCourse);
        StateVector vector = new StateVector(1.825, 0, -1.7903, 0);
        assertEquals(vector, solve.runge_kutta_two(vector));
    }
}
