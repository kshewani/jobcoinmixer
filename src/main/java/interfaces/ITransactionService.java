package interfaces;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * An interface representing transaction processing logc.
 */
public interface ITransactionService {
    /**
     * Processes the received transactions.
     * @param transactions The CompletableFuture containing transactions in json format.
     */
    void processTransactions(CompletableFuture<String> transactions) throws ExecutionException, InterruptedException;

    /**
     * Gets transactions using the rest client.
     * @return A CompletableFuture containing transactions json string.
     * @throws Exception
     */
    CompletableFuture<String> getTransactionsAsync() throws Exception;

    /**
     * Periodically polls for transactions.
     * @param interval the polling interval.
     */
    void pollTransactions(int interval);
}
