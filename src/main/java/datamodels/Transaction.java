package datamodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import interfaces.ITransaction;

public class Transaction implements ITransaction {
    String fromAddress;
    String toAddress;
    String amount;
    String timestamp;
    boolean isRefunded;

    public Transaction() {
    }

    public Transaction(String fromAddress, String toAddress, String amount, String timestamp) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @JsonProperty("fromAddress")
    public String getFromAddress() {
        return fromAddress;
    }

    @JsonProperty("toAddress")
    public String getToAddress() {
        return toAddress;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean getIsRefunded() {
        return isRefunded;
    }

    @Override
    public void setIsRefunded(boolean refunded) {
        this.isRefunded = refunded;
    }


}
