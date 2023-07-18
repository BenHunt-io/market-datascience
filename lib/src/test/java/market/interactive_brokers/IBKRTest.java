package market.interactive_brokers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.Types.SecType;

import TestJavaClient.SampleFrame;
import market.interactive_brokers.client.ApiConsumer;
import market.interactive_brokers.client.IbkrApiMethod;
import market.interactive_brokers.client.QueueConsumerThread;
import utils.SleepUtil;

class IBKRTest {

    Logger logger = Logger.getLogger(IBKRTest.class.getName());

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

        String endTime = "20210101 00:00:00 America/Chicago";
        String duration = "1 Y";
        String barSizeSetitng = "1 day";
        String whatToShow = "TRADES";
        int regularTradingHours = 1; // only show data within regular trading hours
        int formatBarDate = 1; // 1 indicates we should format as yyyyMMdd HH:mm:ss
        boolean keepUpToDate = false;
    
        client.reqHistoricalData(0, spyContract, endTime, duration,
            barSizeSetitng, whatToShow, regularTradingHours, formatBarDate, keepUpToDate, null);

        SleepUtil.sleep(5000);

        List<Bar> historicalDataResponse = apiConsumer.getApiResponses(IbkrApiMethod.HISTORICAL_DATA);

        assertEquals(253, historicalDataResponse.size());

        historicalDataResponse.forEach(bar -> {
            System.out.println(bar.time() + " price: " + bar.close());
        });
    }

    @Test
    void writeHistoricalSPYDateFor1YearToCSV(){

    }
}
