package financial.math;

public class FinancialMath {

    public static double mean(double[] nums){

        double total = 0;
        for(int i = 0; i<nums.length; i++){
            total += nums[i];
        }

        return total/nums.length;
    }
    
    public static double[] calculateReturns(double[] nums){
        double[] returns = new double[nums.length-1];
        for(int i = 1; i<nums.length; i++){
            returns[i-1] = ((nums[i] / nums[i-1])-1)*100;
        }
        return returns;
    }
}
