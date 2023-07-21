package ib.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

// Consumes IBKR API messages put in the queue. 
// Decodes the message and calls "EWrapper" implementation to collect the API responses by name
public class QueueConsumerThread extends Thread {

    Logger logger = Logger.getLogger(QueueConsumerThread.class.getName());

    private EClientSocket client;
    private EReaderSignal signal;
    private EReader reader;

    public QueueConsumerThread(EClientSocket client, EReaderSignal signal, EReader reader) {
        this.client = client;
        this.signal = signal;
        this.reader = reader;
    }

    @Override
    public void run(){

        while(client.isConnected()){
            // wait till the api response queue is not empty
            signal.waitForSignal();
            try {
                // process api response queue
                reader.processMsgs();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error reading api response queue", e);
            }
        }
    }
    
}
