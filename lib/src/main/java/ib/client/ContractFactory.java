package ib.client;

import com.ib.client.Contract;
import com.ib.client.Types.SecType;

public class ContractFactory {

    public static Contract createContract(String tickerSymbol){
        Contract contract = new Contract();
        contract.symbol(tickerSymbol);
        contract.secType(SecType.STK);
        contract.exchange("SMART");
        contract.primaryExch("ARCA");
        contract.currency("USD");
        return contract;
    }
    
}
