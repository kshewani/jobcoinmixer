package services;

import interfaces.ICoinSplitAlgo;
import interfaces.IMixingRequest;

/**
 * An abstract class defining logic of how the coins will be split across addresses provided in a mixing request.
 */
public abstract class CoinSplitAlgo implements ICoinSplitAlgo {
    /**
     * Computes transfer amount for each address in mixing request.
     * @param mixingRequest the mixing request.
     */
    @Override
    public void computeTransferAmount(IMixingRequest mixingRequest) {
        var boundWrapper = new Object() { int upperBound = 100; };
        var remainingAmountWrapper =
                new Object() { double remainingAmount = Double.parseDouble(mixingRequest.getAmount()); };
        var countWrapper = new Object() { int count = mixingRequest.getAccounts().size(); };

        mixingRequest.getAccounts().forEach(a -> {
            if (countWrapper.count == 1) {
                a.setSplitPercentage(boundWrapper.upperBound);
                a.setAmount(remainingAmountWrapper.remainingAmount);
                countWrapper.count--;
                return;
            }

            // call the template method to compute split value.
            int splitValue = this.getSplitValue(mixingRequest, boundWrapper.upperBound);
            boundWrapper.upperBound -= splitValue;
            double amountToTransfer = Double.parseDouble(mixingRequest.getAmount()) * splitValue / 100;
            remainingAmountWrapper.remainingAmount -= amountToTransfer;
            a.setSplitPercentage(splitValue);
            a.setAmount(amountToTransfer);
            countWrapper.count--;
        });
    }

    protected abstract int getSplitValue(IMixingRequest mixingRequest, int bound);
}
