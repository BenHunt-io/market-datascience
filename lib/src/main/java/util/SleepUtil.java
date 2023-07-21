package util;


import java.util.function.Predicate;

public class SleepUtil {

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> void sleepUntil(Predicate<T> sleepCondition, T objectToTest, long timeoutMillis){
        long sleepCount = 0;
        while(!sleepCondition.test(objectToTest)){
            sleep(1000);
            sleepCount+=1000;
            if(sleepCount >= timeoutMillis){
                throw new RuntimeException("Timed out in " + timeoutMillis + " millis sleeping");
            }
        }

    }
    
}
