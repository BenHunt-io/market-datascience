package market.datascience.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import market.datascience.model.Bar;

public class YoYCalculator {


    public static List<YoYReturn> calculateYearOverYearReturn(List<Bar> dailyPriceBars, Month month){

            List<YoYReturn> annualReturns = new ArrayList<>();

            Set<Integer> visitedYear = new HashSet<>();
            Bar previousBar = null;
            for(Bar bar : dailyPriceBars){

                LocalDate dateOfPrice = bar.getLocalDate();
                if(!visitedYear.contains(dateOfPrice.getYear()) && dateOfPrice.getMonth() == month){
                    double closePrice = bar.getClose();
                    visitedYear.add(dateOfPrice.getYear());

                    if(previousBar != null){
                        double calculatedYoYReturn = ((closePrice / previousBar.getClose()) - 1) * 100;
                        YoYReturn yoyReturn = new YoYReturn(previousBar.getLocalDate(), bar.getLocalDate(),
                                previousBar.getClose(), bar.getClose(), calculatedYoYReturn);
                        annualReturns.add(yoyReturn);
                    }
                    previousBar = bar;
                }
            }

        return annualReturns;
    }

    public static BigDecimal calculateReturn(double startingPrice, double endingPrice){
        return BigDecimal.valueOf(((endingPrice / startingPrice) - 1) * 100)
            .setScale(2, RoundingMode.HALF_DOWN);
    }

    
}
