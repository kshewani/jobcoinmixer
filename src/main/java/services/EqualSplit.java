package services;

import interfaces.IMixingRequest;

/**
 * A class defining logic of how the coins will be split when the split is to performed equally across
 * addresses specified in mixing request.
 */
public class EqualSplit extends CoinSplitAlgo {
    /**
     * Gets the equal split value.
     * @param mixingRequest the mixing request.
     * @param bound the bound value for equal number generation.
     * @return
     */
    public int getSplitValue(IMixingRequest mixingRequest, int bound) {
        return 100 / mixingRequest.getAccounts().size();
    }
}
