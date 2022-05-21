

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.rock.golf.Physics.Engine.StateVector;

import org.junit.jupiter.api.Test;
import test.RKMethods;

public class NumericalTests {

    RKMethods computationEngine = new RKMethods();

    @Test
    public void Test_For_Flat_Surface_RK2(){
        StateVector vector = new StateVector(0.01715,0,1.049,0);
        StateVector testedVector = computationEngine.Test_For_Flat_Surface_RK2();
        assertEquals(vector, testedVector);
    }

    @Test
    public void Test_For_Flat_Surface_RK4(){
        StateVector vector = new StateVector(0.23038,0,0.9019,0);
        StateVector testedVector = computationEngine.Test_For_Flat_Surface_RK4();
        assertEquals(vector, testedVector);
    }
}