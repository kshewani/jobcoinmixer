package interfaces;

/**
 * An interface defining skeleton of how the coins will be split across addresses provided in a mixing request.
 */
public interface ICoinSplitAlgo {
    /**
     * Computes transfer amount for each address in mixing request.
     * @param mixingRequest the mixing request.
     */
    void computeTransferAmount(IMixingRequest mixingRequest);
}
