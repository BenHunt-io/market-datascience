package financial.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

class RealizedVolatilityCalculatorTest {
    
    @Test
    void volatilityCalculator_realizedVolatilityTest(){

        double[] yearOverYearPrices = new double[]{100, 110, 120, 130};
        double realizedVolatility = RealizedVolatilityCalculator.realizedVolatility(yearOverYearPrices);

        // 0.68% average deviation from the average return
        assertEquals(0.6813503819814234, realizedVolatility);
    }

    @Test
    void volatilityCalculator_SameReturnTest(){

        double[] yearOverYearPrices = new double[]{100, 110, 121, 133.1};
        double realizedVolatility = RealizedVolatilityCalculator.realizedVolatility(yearOverYearPrices);

        // no change in the average return
        assertEquals(0, BigDecimal.valueOf(realizedVolatility).setScale(10, RoundingMode.HALF_DOWN).doubleValue());
    }
    

    @Test
    void volatilityCalculator_StartPriceSameAsEndPrice(){

        // 10%, ~ -9%, 10%, ~ -9%
        double[] yearOverYearPrices = new double[]{100, 110, 100, 110, 100};
        double realizedVolatility = RealizedVolatilityCalculator.realizedVolatility(yearOverYearPrices);

        // .68% change in the average return
        assertEquals(9.545, BigDecimal.valueOf(realizedVolatility).setScale(3, RoundingMode.HALF_DOWN).doubleValue());
    }

}
