package market.datascience.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MarketRepository {

    private static final CSVFormat CSV_FORMAT = CSVFormat.Builder.create()
        .setHeader()
        .build();


    public List<Map<String, String>> getStockPriceHistory(String stockSymbol){

        String fileName = String.format("%s.csv", stockSymbol);
        File stockPriceHistoryFile = new File(MarketRepository.class.getResource(fileName).getPath());


        try(FileReader fileReader = new FileReader(stockPriceHistoryFile)){
            CSVParser csvParser = new CSVParser(fileReader, CSV_FORMAT);
            return csvParser.getRecords()
                .stream()
                .map(CSVRecord::toMap)
                .collect(Collectors.toList());

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    
}