package financial.math;

public class StandardDeviationCalculator {

    public static double stdDev(double[] nums){
        double mean = FinancialMath.mean(nums);
        double[] distanceFromMean = distancesFrom(nums, mean);
        // variance
        double averageOfSquares = averageOfSquares(distanceFromMean);

        return Math.sqrt(averageOfSquares);
    }

    private static double averageOfSquares(double[] nums){
        double total = 0;
        for(int i = 0; i<nums.length; i++){
            total += Math.pow(nums[i], 2);
        }
        return total/nums.length;
    }

    private static double[] distancesFrom(double[] nums, double fromNum){

        double[] distancesFromNum = new double[nums.length];
        for(int i = 0; i<nums.length; i++){
            distancesFromNum[i] = nums[i] - fromNum;
        }
        return distancesFromNum;

    }
}
