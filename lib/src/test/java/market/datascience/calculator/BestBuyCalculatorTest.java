package market.datascience.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import market.datascience.model.LocalDatePrice;
import market.datascience.model.WorstPrice;

class BestBuyCalculatorTest {

    @Test
    void testBestBuyCalculator(){

        List<LocalDatePrice> priceOnDates = new ArrayList<>();
        priceOnDates.add(new LocalDatePrice(10d, LocalDate.of(2023, 1, 1)));
        priceOnDates.add(new LocalDatePrice(11d, LocalDate.of(2023, 1, 2)));
        priceOnDates.add(new LocalDatePrice(12d, LocalDate.of(2023, 1, 3)));
        priceOnDates.add(new LocalDatePrice(11d, LocalDate.of(2023, 1, 4)));
        priceOnDates.add(new LocalDatePrice(10d, LocalDate.of(2023, 1, 5)));
        priceOnDates.add(new LocalDatePrice(10d, LocalDate.of(2023, 1, 6)));
        priceOnDates.add(new LocalDatePrice(10d, LocalDate.of(2023, 1, 7)));
        priceOnDates.add(new LocalDatePrice(12d, LocalDate.of(2023, 1, 8)));
        priceOnDates.add(new LocalDatePrice(10d, LocalDate.of(2023, 1, 9)));
        priceOnDates.add(new LocalDatePrice(10d, LocalDate.of(2023, 1, 10)));

        List<WorstPrice> worstBuys = BestBuyCalculator.calculateWorstTimesToBuy(priceOnDates, 2);

        worstBuys.forEach(worstBuy -> System.out.println(worstBuy));
    }
    
}
