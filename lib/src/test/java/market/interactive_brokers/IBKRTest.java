package market.interactive_brokers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ib.client.Contract;
import com.ib.client.EClient;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.controller.ApiConnection;

import TestJavaClient.SampleFrame;

public class IBKRTest {


    @Test
    void testConnectionUsingSampleCode(){

    
        EJavaSignal m_signal = new EJavaSignal();
        EClientSocket m_client = new EClientSocket(new SampleFrame(), m_signal);
        m_client.eConnect("127.0.0.1", 7496, 0);


        EClientSocket client2 = new EClientSocket(new SampleFrame(), new ReaderSignalImpl());
        client2.eConnect("127.0.0.1", 7496, 1);
        assertTrue(client2.isConnected());  
    }

    @Test
    void testGetHistoricalData(){
        EJavaSignal m_signal = new EJavaSignal();
        EClientSocket m_client = new EClientSocket(new SampleFrame(), m_signal);
        m_client.eConnect("127.0.0.1", 7496, 0);


        Contract contract = new Contract();
        contract.symbol("AAPL");
        m_client.reqHeadTimestamp(4003, contract, "TRADES", 1, 1);

    }
    
}
