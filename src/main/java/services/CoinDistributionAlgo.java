package services;

import interfaces.ICoinDistributionAlgo;
import interfaces.IMixingRequest;

/**
 * An abstract class defining logic of how the coins will be distribution across addresses provided in a mixing request.
 */
public abstract class CoinDistributionAlgo implements ICoinDistributionAlgo {
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
                a.setDistributionPercentage(boundWrapper.upperBound);
                a.setAmount(remainingAmountWrapper.remainingAmount);
                countWrapper.count--;
                return;
            }

            // call the template method to compute distribution value.
            int distributionValue = this.getDistributionValue(mixingRequest, boundWrapper.upperBound);
            boundWrapper.upperBound -= distributionValue;
            double amountToTransfer = Double.parseDouble(mixingRequest.getAmount()) * distributionValue / 100;
            remainingAmountWrapper.remainingAmount -= amountToTransfer;
            a.setDistributionPercentage(distributionValue);
            a.setAmount(amountToTransfer);
            countWrapper.count--;
        });
    }

    protected abstract int getDistributionValue(IMixingRequest mixingRequest, int bound);
}
