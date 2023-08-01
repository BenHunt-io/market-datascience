package market.datascience.calculator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.MinMaxPriorityQueue;

import market.datascience.model.LocalDatePrice;
import market.datascience.model.WorstPrice;

// calculates what the best and worst times to buy an asset were
public class BestBuyCalculator {


    // Worst time to buy is defined as the longest time to break even
    public static List<WorstPrice> calculateWorstTimesToBuy(List<LocalDatePrice> priceOnDates, int numWorstTimes){

        MinMaxPriorityQueue<WorstPrice> worstBuys = MinMaxPriorityQueue.maximumSize(numWorstTimes).create();

        int i = 0;
        while(i < priceOnDates.size()){
            LocalDatePrice startPriceOnDate = priceOnDates.get(i);
            i++;
            for(int j = i; j<priceOnDates.size(); j++){
                i=j;
                // either we found a break even point, or we reached the last price data
                if(priceOnDates.get(j).getPrice() >= startPriceOnDate.getPrice() || j == priceOnDates.size()-1){
                    WorstPrice worstPrice = new WorstPrice();
                    worstPrice.setPurchaseDate(startPriceOnDate.getLocalDate());
                    worstPrice.setBreakEvenDate(priceOnDates.get(j).getLocalDate());
                    worstPrice.setPrice(startPriceOnDate.getPrice());

                    worstBuys.add(worstPrice);
                    // System.out.println("Found Break Even: " + worstPrice + " i : " + i);
                    break;
                }
            }


        }



        

        return worstBuys.stream().collect(Collectors.toList());
    }
    
}
