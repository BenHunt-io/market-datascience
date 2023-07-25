package ib.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.ib.client.Bar;

public class IbkrCsvPrinter {

    public static void writeHistoricalData(List<Bar> barData, String tickerSymbol, Path dirPath){

        CSVFormat CSV_FORMAT = CSVFormat.Builder.create()
            .setHeader("time", "open", "high", "low", "close", "volume", "count", "wap")
            .build();

        String fileName = String.format(tickerSymbol + ".csv");
        File stockPriceHistoryFile = createFileIfNotExist(dirPath, fileName);

        try(FileWriter fileWriter = new FileWriter(stockPriceHistoryFile)){
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSV_FORMAT);

            for(Bar bar: barData){
                Object[] recordValues = new Object[]{
                    bar.time(),
                    bar.open(),
                    bar.high(),
                    bar.low(),
                    bar.close(),
                    bar.volume(),
                    bar.count(),
                    bar.wap()
                };
                
                csvPrinter.printRecord(recordValues);
            }
        }catch(Exception e){
            System.out.println("error writing historical data");
        }
    }

    private static File createFileIfNotExist(Path dirPath, String fileName){

        try {
            Files.createDirectories(dirPath);
            Path fullFilePath = Paths.get(dirPath.toString(), fileName);
            Files.deleteIfExists(fullFilePath);
            return Files.createFile(fullFilePath).toFile();
        } catch (IOException e) {
            System.out.println("error creating directory: " + dirPath);
            throw new RuntimeException(e);
        }
    
    }

}
