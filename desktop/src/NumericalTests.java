

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rock.golf.Physics.Engine.StateVector;

import org.junit.jupiter.api.Test;
import test.RKMethods;

public class NumericalTests {

    RKMethods computationEngine = new RKMethods();

    @Test
    public void Test_For_Flat_Surface_RK2(){
        StateVector vector = new StateVector(0.095,0,0.901,0);
        StateVector testedVector = computationEngine.Test_For_Flat_Surface_RK2();
        boolean isEqual = StateVector.isEqual(vector, testedVector);
        assertTrue(isEqual);
    }

    @Test
    public void Test_For_Flat_Surface_RK4(){
        StateVector vector = new StateVector(0.095095,0,0.9019,0);
        StateVector testedVector = computationEngine.Test_For_Flat_Surface_RK4();
        boolean isEqual = StateVector.isEqual(vector, testedVector);
        assertTrue(isEqual);
    }
}