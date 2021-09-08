package interfaces;

import datamodels.Account;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface representing mixing request.
 */
public interface IMixingRequest {
    /**
     * Gets source address of the transaction.
     * @return the source address.
     */
    String getSourceAddress();

    /**
     * Gets the amount in transaction.
     * @return the amount.
     */
    String getAmount();

    /**
     * Gets request booking time.
     * @return the booking request time.
     */
    LocalDateTime getRequestBookingTime();

    /**
     * Attaches transaction to mixing request.
     * @param transaction the transaction to attach.
     */
    void attachTransaction(ITransaction transaction);

    /**
     * Indicates whether a transaction is already attached to this mixing request.
     * @return true if transaction attached, false otherwise.
     */
    boolean isTransactionAttached();

    /**
     * Gets the amount in transaction.
     * @return the amount.
     */
    List<Account> getAccounts();

    /**
     * Initializes the mixing request.
     */
    void initialize();

    /**
     * Assigns an identity number to mixing request.
     * @param id the id number.
     */
    void setMixingRequestId(int id);

    /**
     * Gets the identity number of mixing request.
     * @return the id number.
     */
    int getMixingRequestId();

    /**
     * Indicates whether the coins are transferred to all destination addresses.
     * @return true if coins are transferred to all destination addresses, false otherwise.
     */
    boolean isComplete();
}
