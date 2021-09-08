package interfaces;

public interface IMixingTransaction extends ITransaction {
    int getMixingRequestId();
    IAccount getAccount();
    IMixingRequest getMixingRequest();
    // boolean isFullAmountTransferred();
}
