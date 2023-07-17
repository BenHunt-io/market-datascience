package market.interactive_brokers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EWrapper;
import com.ib.client.Types.SecType;

import TestJavaClient.SampleFrame;

class IBKRTest {

    Logger logger = Logger.getLogger(IBKRTest.class.getName());

    @Test
    void testTWSClientConnectionWithSampleCode(){
        EJavaSignal m_signal = new EJavaSignal();
        EClientSocket m_client = new EClientSocket(new SampleFrame(), m_signal);
        m_client.eConnect("127.0.0.1", 7496, 0);
        assertTrue(m_client.isConnected());
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
        EWrapper wrapperImpl = new WrapperImpl();
        EClientSocket client = new EClientSocket(wrapperImpl, signal);
        client.eConnect("127.0.0.1", 7497, 0);

        assertTrue(client.isConnected());

        // Start producing thread 
        EReader eReader = new EReader(client, signal);
        eReader.start();

        // Start consuming thread
        new Thread(() -> {
            while(client.isConnected()){
                signal.waitForSignal();
                try {
                    System.out.println("Processing Messages");
                    eReader.processMsgs();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error reading messages", e);
                }
            }
        }).start();

        Contract contract = new Contract();
        contract.symbol("SPY");
        contract.secType(SecType.STK);
        contract.exchange("SMART");
        contract.primaryExch("ARCA");
        contract.currency("USD");
        client.reqHeadTimestamp(4003, contract, "TRADES", 1, 1);

        sleep(3000);
        client.eDisconnect();

        assertNotNull(WrapperImpl.headTimestamp);




    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
