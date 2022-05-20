

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import test.RKMethods;

import com.rock.golf.StateVector;

public class NumericalTests {

    RKMethods computationEngine = new RKMethods();

    @Test
    public void Test_For_Flat_Surface_RK2(){
        StateVector vector = new StateVector(0.01715,0,1.049,0);
        StateVector testedVector = computationEngine.Test_For_Flat_Surface_RK2();
        assertEquals(vector, testedVector);
    }



}