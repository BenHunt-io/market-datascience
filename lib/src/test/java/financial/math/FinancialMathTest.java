package financial.math;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class FinancialMathTest {


    @Test
    void testReturnsCalculation(){

        double[] yearlyPrices = new double[]{100, 110, 120, 130};
        double[] returns = FinancialMath.calculateReturns(yearlyPrices);
        double[] expectedReturns = new double[]{10.000000000000009, 9.090909090909083, 8.333333333333325};
    
        assertArrayEquals(expectedReturns, returns, "Returns: " +  Arrays.toString(returns));
    }
    
}
