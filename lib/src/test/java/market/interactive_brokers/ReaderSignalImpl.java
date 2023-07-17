package market.interactive_brokers;

import com.ib.client.EReaderSignal;

public class ReaderSignalImpl implements EReaderSignal {

    @Override
    public void issueSignal() {
        throw new UnsupportedOperationException("Unimplemented method 'issueSignal'");
    }

    @Override
    public void waitForSignal() {
        throw new UnsupportedOperationException("Unimplemented method 'waitForSignal'");
    }
    
}
