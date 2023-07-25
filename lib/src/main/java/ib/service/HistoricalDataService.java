package ib.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.Types.WhatToShow;

import ib.client.ApiConsumer;
import ib.client.ContractFactory;
import ib.client.IbkrApiMethod;
import ib.client.IbkrCsvPrinter;
import ib.client.QueueConsumerThread;
import util.MemoryUtil;
import util.SleepUtil;

public class HistoricalDataService {

    private ApiConsumer apiConsumer;
    private EClientSocket client;
    private static final DateTimeFormatter ZONE_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss VV");

    public HistoricalDataService(){
        apiConsumer = new ApiConsumer();
        client = setupAPIConnection(apiConsumer);
    }

    public Path downloadHistoricalDataToCSV(List<String> tickers, String outputDir){

        String baseOutputDir = String.format("/market/ibkr/output/historical/stocks");
        Path fullDirPath = Paths.get(HistoricalDataService.class.getResource(baseOutputDir).getPath(), outputDir);

        for(int i = 0; i<tickers.size(); i++){
            MemoryUtil.printMemoryUsage();

            ZonedDateTime endTime = ZonedDateTime.now();
            String duration = "58 Y";
            String barSizeSetitng = "1 day";
            String whatToShow = WhatToShow.TRADES.name();
            int regularTradingHours = 1; // only show data within regular trading hours
            int formatBarDate = 1; // 1 indicates we should format as yyyyMMdd HH:mm:ss
            boolean keepUpToDate = false;

            Contract stockContract = ContractFactory.createContract(tickers.get(i));

            apiConsumer.setHistoricalDataEnd(false);
            client.reqHistoricalData(i, stockContract, ZONE_DATE_TIME_FORMAT.format(endTime), duration,
                barSizeSetitng, whatToShow, regularTradingHours, formatBarDate, keepUpToDate, null);

            SleepUtil.sleepUntil(ApiConsumer::isHistoricalDataEnd, apiConsumer, 90_000);
            List<Bar> historicalDataResponse = apiConsumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA);


            IbkrCsvPrinter.writeHistoricalData(historicalDataResponse, tickers.get(i), fullDirPath);
            System.out.println("Done downloading historical data for: " + tickers.get(i));
        }

        client.eDisconnect();

        return fullDirPath;

    }

    private EClientSocket setupAPIConnection(ApiConsumer apiConsumer){
        EJavaSignal signal = new EJavaSignal();
        EClientSocket client = new EClientSocket(apiConsumer, signal);
        client.eConnect("127.0.0.1", 7497, 0);

        // Start producing thread 
        EReader queueProducingThread = new EReader(client, signal);
        queueProducingThread.start();

        // Start consuming thread
        QueueConsumerThread queueConsumerThread = new QueueConsumerThread(client, signal, queueProducingThread);
        queueConsumerThread.start();

        return client;
    }


    
}
