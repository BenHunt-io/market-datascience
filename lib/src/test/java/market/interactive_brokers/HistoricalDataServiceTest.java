package market.interactive_brokers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import ib.service.HistoricalDataService;

class HistoricalDataServiceTest {

    @Test
    void downloadHistoricalData10Tickers() throws Exception {
        List<String> tickers = Arrays.asList("JPM", "JNJ", "HD", "CVX", "KO", "BAC", "ABT", "HON");
        
        HistoricalDataService historicalDataService = new HistoricalDataService();
        Path historicalDataDir = historicalDataService.downloadHistoricalDataToCSV(tickers);

        List<File> historicalDataFiles = Files.list(historicalDataDir)
            .map(filePath -> new File(filePath.toString()))
            .collect(Collectors.toList());

        assertEquals(10, historicalDataFiles.size());
    }
}
