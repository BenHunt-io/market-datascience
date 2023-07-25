package market.interactive_brokers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;

import ib.service.HistoricalDataService;
import market.datascience.etl.TickerExtractor;

class HistoricalDataServiceTest {

    private static final CSVFormat CSV_FORMAT = CSVFormat.Builder.create()
        .setHeader()
        .build();

    @Test
    void downloadHistoricalData10Tickers() throws Exception {
        List<String> tickers = Arrays.asList("JPM", "JNJ", "HD", "CVX", "KO", "BAC", "ABT", "HON");
        
        HistoricalDataService historicalDataService = new HistoricalDataService();
        Path historicalDataDir = historicalDataService.downloadHistoricalDataToCSV(tickers, "10Tickers");

        List<File> historicalDataFiles = Files.list(historicalDataDir)
            .map(filePath -> new File(filePath.toString()))
            .collect(Collectors.toList());

        assertEquals(10, historicalDataFiles.size());
    }


    @Test
    void downloadHistoricalDataForAllNyseETFs() throws Exception {

        List<String> nyseEtfTickers = getNyseEtfTickers();
        assertEquals(1377, nyseEtfTickers.size());

        HistoricalDataService historicalDataService = new HistoricalDataService();
        Path historicalDataDir = historicalDataService.downloadHistoricalDataToCSV(nyseEtfTickers, "NYSE-ETFS");
        
        List<File> historicalDataFiles = Files.list(historicalDataDir)
            .map(filePath -> new File(filePath.toString()))
            .collect(Collectors.toList());

        assertEquals(10, historicalDataFiles.size());

    }


    public Integer addAndDoSomethingElse(int num, int numOther, Function<Integer, Integer> anotherOperation){
        
        Integer additionResult = num+numOther;
        return anotherOperation.apply(additionResult);
    }


    private List<String> getNyseEtfTickers(){


        Path relativePath = Paths.get(TickerExtractor.OUTPUT_DIR_FILE_PATH, "NYSE-ETFS.csv");
        File nyseEtfsFile = new File(HistoricalDataServiceTest.class.getResource(relativePath.toString()).getPath());
        try(CSVParser csvParser = new CSVParser(new FileReader(nyseEtfsFile), CSV_FORMAT)){

            return csvParser.getRecords().stream()
                .map(csvRecord -> csvRecord.get("Code"))
                .collect(Collectors.toList());

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
