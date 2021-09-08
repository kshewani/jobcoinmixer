package datamodels;

import interfaces.IAccount;
import interfaces.IMixingRequest;
import interfaces.IMixingTransaction;

public class MixingTransaction extends Transaction implements IMixingTransaction {
    final IMixingRequest mixingRequest;
    final IAccount account;

    public MixingTransaction(String fromAddress,
                             String timestamp,
                             IMixingRequest mixingRequest,
                             IAccount account) {
        super(fromAddress, account.getAddress(), String.valueOf(account.getAmount()), timestamp);
        this.mixingRequest = mixingRequest;
        this.account = account;
    }

    @Override
    public int getMixingRequestId() {
        return mixingRequest.getMixingRequestId();
    }

    @Override
    public IAccount getAccount() {
        return account;
    }

    @Override
    public IMixingRequest getMixingRequest() {
        return mixingRequest;
    }
}
