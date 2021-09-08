package services;

import interfaces.IMixingRequest;

import java.util.Random;

/**
 * A class defining logic of how the coins will be split when the split is to performed randomly across
 * addresses specified in mixing request.
 */
public class RandomSplit extends CoinSplitAlgo {
    /**
     * Gets the random split value.
     * @param mixingRequest the mixing request.
     * @param bound the bound value for random number generation.
     * @return
     */
    public int getSplitValue(IMixingRequest mixingRequest, int bound) {
        Random randomizer = new Random();
        return randomizer.nextInt(bound);
    }
}
