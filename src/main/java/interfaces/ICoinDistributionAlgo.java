package interfaces;

/**
 * An interface defining skeleton of how the coins will be distributed across addresses provided in a mixing request.
 */
public interface ICoinDistributionAlgo {
    /**
     * Computes transfer amount for each address in mixing request.
     * @param mixingRequest the mixing request.
     */
    void computeTransferAmount(IMixingRequest mixingRequest);
}
