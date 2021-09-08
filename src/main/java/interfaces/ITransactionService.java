package interfaces;

/**
 * An interface representing transaction processing logc.
 */
public interface ITransactionService {
    /**
     * Periodically polls for transactions.
     * @param interval the polling interval.
     */
    void pollTransactions(int interval);

    /**
     * Stops the polling of transactions.
     */
    void stopPolling();
}
