package datamodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.ICoinSplitAlgo;
import interfaces.IMixingRequest;
import interfaces.ITransaction;
import services.CoinSplitAlgoFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * The class to hold mixing request details.
 */
public class MixingRequest implements IMixingRequest {
    String sourceAddress;
    List<Account> accounts;
    String amount;
    final LocalDateTime requestBookingTime;
    SplitType splitType;
    int count;
    ITransaction transaction;
    ICoinSplitAlgo coinSplitAlgo;
    int id;

    /**
     * Creates a mixing request object.
     */
    public MixingRequest() {
        requestBookingTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Creates a mixing request object.
     */
    public MixingRequest(String sourceAddress, List<Account> accounts, String amount, SplitType splitType) {
        this.sourceAddress = sourceAddress;
        this.accounts = accounts;
        this.amount = amount;
        this.splitType = splitType;
        requestBookingTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Gets request booking time.
     * @return the booking request time.
     */
    @JsonIgnore
    @Override
    public LocalDateTime getRequestBookingTime() {
        return requestBookingTime;
    }

    /**
     * Attaches transaction to mixing request.
     * @param transaction the transaction to attach.
     */
    @Override
    public void attachTransaction(ITransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Indicates whether a transaction is already attached to this mixing request.
     * @return true if transaction attached, false otherwise.
     */
    @JsonIgnore
    @Override
    public boolean isTransactionAttached() {
        return this.transaction != null;
    }

    /**
     * Gets source address of the transaction.
     * @return the source address.
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Gets the amount in transaction.
     * @return the amount.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Initializes the mixing request.
     */
    @Override
    public void initialize() {
        coinSplitAlgo = CoinSplitAlgoFactory.createCoinSplitAlgo(splitType);
        this.coinSplitAlgo.computeTransferAmount(this);
    }

    /**
     * Assigns an identity number to mixing request.
     * @param id the id number.
     */
    @Override
    public void setMixingRequestId(int id) {
        this.id = id;
    }

    /**
     * Gets the identity number of mixing request.
     * @return the id number.
     */
    @JsonIgnore
    @Override
    public int getMixingRequestId() {
        return id;
    }

    /**
     * Indicates whether the coins are transferred to all destination addresses.
     * @return true if coins are transferred to all destination addresses, false otherwise.
     */
    @Override
    public boolean isComplete() {
        // request is complete if amount is transferred to all destination accounts.
        return !this.accounts.stream().anyMatch(a -> !a.getIsAmountTransferred());
    }

    /**
     * Gets the list of destination accounts.
     * @return the list of accounts.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Gets the split type. Equal, Random etc.
     * @return the split type.
     */
    public SplitType getSplitType() {
        return splitType;
    }

    /**
     * Returns the string representation of the mixing request.
     * @return the string representation of the mixing request
     */
    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
