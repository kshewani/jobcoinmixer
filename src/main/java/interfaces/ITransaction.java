package interfaces;

public interface ITransaction {
    String getFromAddress();
    String getToAddress();
    String getAmount();
    String getTimestamp();
    boolean getIsRefunded();
    void setIsRefunded(boolean refunded);
}

