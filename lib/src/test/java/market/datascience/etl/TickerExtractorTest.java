package market.datascience.etl;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class TickerExtractorTest {

    @Test
    void testETFTickerExtraction(){

        Set<String> exchangeNames = Stream.of("NYSE", "NYSEARCA", "NYSE ARCA")
            .collect(Collectors.toSet());

        TickerExtractor.extractTickersToCSV(csvRecords -> 
            csvRecords.filter(csvRecord -> csvRecord.get("Type").equals("ETF") &&
                exchangeNames.contains(csvRecord.get("Exchange")))
        , "NYSE-ETFS");
    }

    @Test
    void testNasdaqStockExtraction(){

        Set<String> exchangeNames = Stream.of("NASDAQ")
            .collect(Collectors.toSet());

        TickerExtractor.extractTickersToCSV(csvRecords -> 
            csvRecords.filter(csvRecord -> csvRecord.get("Type").equals("Common Stock") &&
                exchangeNames.contains(csvRecord.get("Exchange")))
        , "NASDAQ-STOCKS");
    }


    
}
