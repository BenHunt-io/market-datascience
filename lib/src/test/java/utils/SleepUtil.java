package utils;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class SleepUtil {

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static <T> void sleepUntil(Predicate<T> sleepCondition, T objectToTest, long timeoutMillis){
        long sleepCount = 0;
        while(!sleepCondition.test(objectToTest)){
            sleep(1000);
            sleepCount+=1000;
            if(sleepCount >= timeoutMillis){
                fail("Timed out in " + timeoutMillis + " millis sleeping");
            }
        }

    }
    
}
