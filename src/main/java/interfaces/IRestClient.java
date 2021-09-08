package interfaces;

import java.util.concurrent.CompletableFuture;

/**
 * An interface representing a rest api client.
 */
public interface IRestClient {
    /**
     * Gets transactions by calling the transaction api.
     * @return A CompletableFuture containing transactions json string.
     * @throws Exception
     */
    CompletableFuture<String> getTransactionsAsync() throws Exception;

    /**
     * Sends a new transaction by calling transaction api.
     * @param newTransaction a new transaction
     * @throws Exception
     */
    void sendTransaction(ITransaction newTransaction) throws Exception;
}
