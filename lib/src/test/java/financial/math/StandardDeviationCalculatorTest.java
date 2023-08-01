package financial.math;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StandardDeviationCalculatorTest {

    @Test
    void standardDeviationCalculatorTest(){
        double[] nums = new double[]{7, 5, 6, 7, 7, 5, 5};
        double stdDev = StandardDeviationCalculator.stdDev(nums);
        assertEquals(0.9258200997725514, stdDev);
    }
    
}
