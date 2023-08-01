package financial.math;

public class RealizedVolatilityCalculator {

    // Need to test
    private static double realizedVariance(double[] nums){
        
        double sumOfSquaredYields = 0;
        for(int i = 1; i<nums.length; i++){
            double squaredYield = Math.pow(nums[i] / nums[i-1], 2);
            sumOfSquaredYields += squaredYield;
        }

        return sumOfSquaredYields;
    }

    public static double realizedVolatility(double[] nums){
        double[] returns = FinancialMath.calculateReturns(nums);
        return StandardDeviationCalculator.stdDev(returns);
    }

}
