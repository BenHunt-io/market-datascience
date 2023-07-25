package market.datascience.etl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import util.FileUtil;

// Processes lists of ticker symbols and related meta data from EODHistoricaData.com
public class TickerExtractor {

    private static final CSVFormat CSV_FORMAT = CSVFormat.Builder.create()
        .setHeader("Code","Name","Country","Exchange","Currency","Type","Isin")
        .build();

    public static final String TICKERS_FILE_PATH = "/market/eodhistorical/us-tickers/US_LIST_OF_SYMBOLS.csv";
    public static final String OUTPUT_DIR_FILE_PATH = "/market/eodhistorical/output";
    

    public static Path extractTickersToCSV(Function<Stream<CSVRecord>, Stream<CSVRecord>> filterFunction, String fileName){

        List<CSVRecord> csvRecords = extractTickers(filterFunction);
        return writeToCSVFile(csvRecords, fileName);
    }

    private static List<CSVRecord> extractTickers(Function<Stream<CSVRecord>, Stream<CSVRecord>> filterFunction){
        
        File tickersFile = new File(TickerExtractor.class.getResource(TICKERS_FILE_PATH).getPath());
        try(CSVParser csvParser = new CSVParser(new FileReader(tickersFile), CSV_FORMAT)){
            return filterFunction.apply(csvParser.getRecords().stream())
                .collect(Collectors.toList());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static Path writeToCSVFile(List<CSVRecord> csvRecords, String fileName){

        Path outputFilePath = Paths.get(TickerExtractor.class.getResource(OUTPUT_DIR_FILE_PATH).getPath(), fileName + ".csv");
        FileUtil.createFileIfNotExist(outputFilePath);

        try(CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFilePath.toFile()), CSV_FORMAT)){

            csvRecords.forEach(csvRecord -> {
                printRecord(csvPrinter, csvRecord);
            });

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return outputFilePath;   
    }


    private static void printRecord(CSVPrinter csvPrinter, CSVRecord csvRecord){
        try {
            csvPrinter.printRecord(csvRecord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    
}
