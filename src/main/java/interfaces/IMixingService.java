package interfaces;

/**
 * An interface representing MixingService.
 */
public interface IMixingService {
    /**
     * Submits a mixing request to mixing service.
     * @param mixingRequest the mixing request.
     */
    void submitMixingRequest(IMixingRequest mixingRequest);

    /**
     * Performs all required actions when a new transaction is received.
     * @param transaction the transaction.
     */
    void onNewTransaction(ITransaction transaction);
}
