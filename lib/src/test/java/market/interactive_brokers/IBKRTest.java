package market.interactive_brokers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;

import TestJavaClient.SampleFrame;
import ib.client.ApiConsumer;
import ib.client.IbkrApiMethod;
import ib.client.IbkrCsvPrinter;
import ib.client.QueueConsumerThread;
import util.SleepUtil;

class IBKRTest {

    Logger logger = Logger.getLogger(IBKRTest.class.getName());
    private static final DateTimeFormatter ZONE_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss VV");

    @Test
    void testTWSClientConnectionWithSampleCode(){
        EReaderSignal readerSignal = new EJavaSignal();
        EWrapper messageConsumer = new SampleFrame();
        EClientSocket client = new EClientSocket(messageConsumer, readerSignal);

        String twsHost = "localhost";
        int twsPaperTradingListenPort = 7497;
        int clientId = 0;

        client.eConnect(twsHost, twsPaperTradingListenPort, clientId);
        assertTrue(client.isConnected());
    }

    // Does not pass
    @Test
    void testIbkrGatewayClientConnectionWithSampleCode(){
        EJavaSignal m_signal = new EJavaSignal();
        EClientSocket socketClient = new EClientSocket(new SampleFrame(), m_signal);
        socketClient.eConnect("127.0.0.1", 5000, 0);
        assertTrue(socketClient.isConnected());
    }

    @Test
    void testGetDateofEarliestSPYData(){

        EJavaSignal signal = new EJavaSignal();
        ApiConsumer apiConsumer = new ApiConsumer();
        EClientSocket client = new EClientSocket(apiConsumer, signal);
        client.eConnect("127.0.0.1", 7497, 0);

        assertTrue(client.isConnected());

        // Start producing thread 
        EReader queueProducingThread = new EReader(client, signal);
        queueProducingThread.start();

        // Start consuming thread
        QueueConsumerThread queueConsumerThread = new QueueConsumerThread(client, signal, queueProducingThread);
        queueConsumerThread.start();

        Contract contract = createSPYContract();

        client.reqHeadTimestamp(4003, contract, "TRADES", 1, 1);

        SleepUtil.sleep(3000);
        client.eDisconnect();

        assertEquals(LocalDateTime.of(1993,1,29,14,30,0,0),
            (apiConsumer.apiResponsesByName.get(IbkrApiMethod.HEAD_TIMESTAMP).get(0)));
    }

    private EClientSocket setupAPIConnection(ApiConsumer apiConsumer){
        EJavaSignal signal = new EJavaSignal();
        EClientSocket client = new EClientSocket(apiConsumer, signal);
        client.eConnect("127.0.0.1", 7497, 0);

        assertTrue(client.isConnected());

        // Start producing thread 
        EReader queueProducingThread = new EReader(client, signal);
        queueProducingThread.start();

        // Start consuming thread
        QueueConsumerThread queueConsumerThread = new QueueConsumerThread(client, signal, queueProducingThread);
        queueConsumerThread.start();

        return client;
    }

    private Contract createSPYContract(){
        Contract contract = new Contract();
        contract.symbol("SPY");
        contract.secType(SecType.STK);
        contract.exchange("SMART");
        contract.primaryExch("ARCA");
        contract.currency("USD");
        return contract;
    }

    @Test
    void getHistoricalSPYDataFor1Year(){

        ApiConsumer apiConsumer = new ApiConsumer();
        EClientSocket client = setupAPIConnection(apiConsumer);
        
        Contract spyContract = createSPYContract();

        ZonedDateTime endTime = ZonedDateTime.parse("20210101 00:00:00 America/Chicago", ZONE_DATE_TIME_FORMAT);
        String duration = "1 Y";
        String barSizeSetitng = "1 day";
        String whatToShow = WhatToShow.TRADES.name();
        int regularTradingHours = 1; // only show data within regular trading hours
        int formatBarDate = 1; // 1 indicates we should format as yyyyMMdd HH:mm:ss
        boolean keepUpToDate = false;
    
        client.reqHistoricalData(0, spyContract, ZONE_DATE_TIME_FORMAT.format(endTime), duration,
            barSizeSetitng, whatToShow, regularTradingHours, formatBarDate, keepUpToDate, null);

        SleepUtil.sleep(5000);

        List<Bar> historicalDataResponse = apiConsumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA);

        assertEquals(253, historicalDataResponse.size());

        historicalDataResponse.forEach(bar -> {
            System.out.println(bar.time() + " price: " + bar.close());
        });
    }

    @Test
    void getHistoricalSPYDataSinceInception(){
        
        ApiConsumer apiConsumer = new ApiConsumer();
        EClientSocket client = setupAPIConnection(apiConsumer);
        
        Contract spyContract = createSPYContract();

        ZonedDateTime endTime = ZonedDateTime.parse("20230601 00:00:00 America/Chicago", ZONE_DATE_TIME_FORMAT);
        String duration = "58 Y";
        String barSizeSetitng = "1 day";
        String whatToShow = WhatToShow.TRADES.name();
        int regularTradingHours = 1; // only show data within regular trading hours
        int formatBarDate = 1; // 1 indicates we should format as yyyyMMdd HH:mm:ss
        boolean keepUpToDate = false;
    
        client.reqHistoricalData(0, spyContract, ZONE_DATE_TIME_FORMAT.format(endTime), duration,
            barSizeSetitng, whatToShow, regularTradingHours, formatBarDate, keepUpToDate, null);

        SleepUtil.sleepUntil(consumer -> consumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA) != null &&
            consumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA).size() == 7637, apiConsumer, 30_000);

        List<Bar> historicalDataResponse = apiConsumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA);

        for(int i = 0; i<100; i++){
            System.out.println(historicalDataResponse.get(i).time() + " price: " + historicalDataResponse.get(i).close());
        }
    }

    @Test
    void writeHistoricalSPYDataSinceInceptionToCSV() throws FileNotFoundException, IOException{
        
        ApiConsumer apiConsumer = new ApiConsumer();
        EClientSocket client = setupAPIConnection(apiConsumer);
        
        Contract spyContract = createSPYContract();

        ZonedDateTime endTime = ZonedDateTime.parse("20230601 00:00:00 America/Chicago", ZONE_DATE_TIME_FORMAT);
        String duration = "58 Y";
        String barSizeSetitng = "1 day";
        String whatToShow = WhatToShow.TRADES.name();
        int regularTradingHours = 1; // only show data within regular trading hours
        int formatBarDate = 1; // 1 indicates we should format as yyyyMMdd HH:mm:ss
        boolean keepUpToDate = false;
    
        client.reqHistoricalData(0, spyContract, ZONE_DATE_TIME_FORMAT.format(endTime), duration,
            barSizeSetitng, whatToShow, regularTradingHours, formatBarDate, keepUpToDate, null);

        SleepUtil.sleepUntil(consumer -> consumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA) != null &&
            consumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA).size() == 7637, apiConsumer, 30_000);

        List<Bar> historicalDataResponse = apiConsumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA);

        IbkrCsvPrinter.writeHistoricalData(historicalDataResponse, spyContract.symbol());

        CSVFormat CSV_FORMAT = CSVFormat.Builder.create()
            .setHeader()
            .build();

        String expectedFileName = String.format("/market/ibkr/output/historical/stocks/SPY.csv");
        File stockPriceHistoryFile = new File(IBKRTest.class.getResource(expectedFileName).getPath());

        try(FileReader fileReader = new FileReader(stockPriceHistoryFile)){
            CSVParser csvParser = new CSVParser(fileReader, CSV_FORMAT);

            assertEquals(8, csvParser.getHeaderMap().size());
            assertEquals(7637, csvParser.getRecords().size());
        }
    }

    @Test
    void testCsvPrinter() throws FileNotFoundException, IOException{
        
        List<Bar> historicalData = Arrays.asList(
            new Bar("", 0, 0, 0, 0, null, 0, null)
        );

        IbkrCsvPrinter.writeHistoricalData(historicalData, "SPY");

        CSVFormat CSV_FORMAT = CSVFormat.Builder.create()
            .setHeader()
            .build();

        String expectedFileName = String.format("/market/ibkr/output/historical/stocks/SPY.csv");
        File stockPriceHistoryFile = new File(IBKRTest.class.getResource(expectedFileName).getPath());

        try(FileReader fileReader = new FileReader(stockPriceHistoryFile)){
            CSVParser csvParser = new CSVParser(fileReader, CSV_FORMAT);

            assertEquals(8, csvParser.getHeaderMap().size());
            assertEquals(1, csvParser.getRecords().size());
        }
    }
}
