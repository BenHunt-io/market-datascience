package market.datascience.etf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import ib.client.ApiConsumer;
import market.datascience.calculator.YoYCalculator;
import market.datascience.calculator.YoYReturn;
import market.datascience.model.Bar;

class RiskAdjustedReturnTest {

    private CSVFormat CSV_FORMAT = CSVFormat.Builder.create()
        .setHeader()
        .build();

    @Test
    void calculateSP500AnnualReturn(){

        String expectedFileName = String.format("/market/ibkr/output/historical/stocks/SPY.csv");
        File spyCSVFile = new File(RiskAdjustedReturnTest.class.getResource(expectedFileName).getPath());

        try(CSVParser csvParser = new CSVParser(new FileReader(spyCSVFile), CSV_FORMAT)){
            
            List<CSVRecord> records = csvParser.getRecords();
            assertEquals(8, csvParser.getHeaderMap().size());
            assertEquals(7637, records.size());

            List<Bar> dailyPriceBars = records.stream()
                .map(csvRecord -> {
                    Bar bar = new Bar();
                    bar.setLocalDate(LocalDate.parse(csvRecord.get("time"), ApiConsumer.LOCAL_DATE_FORMAT));
                    bar.setClose(Double.parseDouble(csvRecord.get("close")));
                    return bar;
                })
                .collect(Collectors.toList());
            List<YoYReturn> yoyReturns = YoYCalculator.calculateYearOverYearReturn(dailyPriceBars, Month.FEBRUARY);

            assertEquals(30, yoyReturns.size());
            yoyReturns.forEach(System.out::println);

            BigDecimal startingPrice = yoyReturns.get(0).getFromPrice();
            BigDecimal endingPrice = yoyReturns.get(yoyReturns.size()-1).getToPrice();

            BigDecimal totalReturn = YoYCalculator.calculateReturn(startingPrice.doubleValue(), endingPrice.doubleValue());

            System.out.println("Start Price: " + startingPrice + " End Price: " + endingPrice);
            System.out.println("Total Return: " + totalReturn + "%");
            
            double averageReturn = yoyReturns.stream()
                .map(yoyReturn -> yoyReturn.getYoyReturn().doubleValue())
                .collect(Collectors.summingDouble(Double::intValue)) / yoyReturns.size();

            System.out.println("Average Return: " + averageReturn);

        }catch(Exception e){
            System.out.println("Error reading CSV");
        }
    }
    
}
