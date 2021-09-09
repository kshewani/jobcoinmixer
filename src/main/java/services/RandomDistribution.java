package services;

import interfaces.IMixingRequest;

import java.util.Random;

/**
 * A class defining logic of how the coins will be distributed when the distribution is to performed randomly across
 * addresses specified in mixing request.
 */
public class RandomDistribution extends CoinDistributionAlgo {
    /**
     * Gets the random distribution value.
     * @param mixingRequest the mixing request.
     * @param bound the bound value for random number generation.
     * @return
     */
    public int getDistributionValue(IMixingRequest mixingRequest, int bound) {
        Random randomizer = new Random();
        return randomizer.nextInt(bound);
    }
}
