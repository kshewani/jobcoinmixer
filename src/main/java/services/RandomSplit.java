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

    /*@Override
    public void computeTransferAmount(IMixingRequest mixingRequest) {
        var boundWrapper = new Object() { int bound = mixingRequest.getWallets().size(); };
        var remainingAmountWrapper =
                new Object() { double remainingAmount = Double.parseDouble(mixingRequest.getAmount()); };
        var countWrapper = new Object() { int count = mixingRequest.getWallets().size(); };
        Random randomizer = new Random();
        mixingRequest.getWallets().forEach(a -> {
            if (countWrapper.count == 1) {
                a.setSplitPercentage(boundWrapper.bound);
                a.setWalletAmount(remainingAmountWrapper.remainingAmount);
                countWrapper.count--;
                return;
            }
            int splitValue = randomizer.nextInt(boundWrapper.bound);
            boundWrapper.bound -= splitValue;
            double amountToTransfer = Double.parseDouble(mixingRequest.getAmount()) * splitValue / 100;
            remainingAmountWrapper.remainingAmount -= amountToTransfer;
            a.setSplitPercentage(splitValue);
            countWrapper.count--;
        });
    }*/
}
