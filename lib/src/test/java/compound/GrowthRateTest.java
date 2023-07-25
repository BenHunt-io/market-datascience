package compound;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GrowthRateTest {

    Logger log = Logger.getLogger(GrowthRateTest.class.getName());

    @BeforeEach()
    void beforeEach(){


        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {

            @Override
            public String format(LogRecord record) {
                return record.getMessage() + System.lineSeparator();
            }
        });

        Stream.of(log.getHandlers()).forEach(handler -> log.removeHandler(handler));
        log.setUseParentHandlers(false);
        log.addHandler(consoleHandler);

    }

    @ParameterizedTest
    @CsvSource({
        "0.01",
        "0.03",
        "0.05",
        "0.07",
        "0.09",
        "0.11",
        "0.13",
        "0.15",


    })
    void testTimeToDouble(double growthRate){


        double startingAmount = 100d;
        double currentAmount = startingAmount;
        int doubleAmount = 200;
        Map<Integer, Integer> doublingTimesByIterations = new LinkedHashMap<>();
        int iterations = 0;
        while(currentAmount < (startingAmount*128)){
            currentAmount = currentAmount * (1+growthRate);

            iterations++;

            if(currentAmount > doubleAmount){

                doublingTimesByIterations.put(doubleAmount, iterations);
                doubleAmount*=2;
            }
        }

        System.out.println("Starting Amount: " + startingAmount);
        System.out.println("Growth Rate: " + growthRate);

        doublingTimesByIterations.forEach((amount, timesCompounded) -> {
            log.info("Amount: " + amount + " Iterations  : " + timesCompounded);
        });
        
    }
    
}
