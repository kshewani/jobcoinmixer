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

    /*
    @Override
    public void computeTransferAmount(IMixingRequest mixingRequest) {
        var boundWrapper = new Object() { int bound = mixingRequest.getWallets().size(); };
        var remainingAmountWrapper =
                new Object() { double remainingAmount = Double.parseDouble(mixingRequest.getAmount()); };
        var countWrapper = new Object() { int count = mixingRequest.getWallets().size(); };
        mixingRequest.getWallets().forEach(a -> {
            if (countWrapper.count == 1) {
                a.setSplitPercentage(boundWrapper.bound);
                a.setWalletAmount(remainingAmountWrapper.remainingAmount);
                countWrapper.count--;
                return;
            }

            int splitValue = 100 / mixingRequest.getWallets().size();
            boundWrapper.bound -= splitValue;
            a.setSplitPercentage(splitValue);
            double amountToTransfer = Double.parseDouble(mixingRequest.getAmount()) * splitValue / 100;
            remainingAmountWrapper.remainingAmount -= amountToTransfer;
            a.setWalletAmount(amountToTransfer);
            countWrapper.count--;
        });
    }
    * */
}
