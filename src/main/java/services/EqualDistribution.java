package services;

import interfaces.IMixingRequest;

/**
 * A class defining logic of how the coins will be distributed when the distribution is to performed equally across
 * addresses specified in mixing request.
 */
public class EqualDistribution extends CoinDistributionAlgo {
    /**
     * Gets the equal distribution value.
     * @param mixingRequest the mixing request.
     * @param bound the bound value for equal number generation.
     * @return
     */
    public int getDistributionValue(IMixingRequest mixingRequest, int bound) {
        return 100 / mixingRequest.getAccounts().size();
    }
}
