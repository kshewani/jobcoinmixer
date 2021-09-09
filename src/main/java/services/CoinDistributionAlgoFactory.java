package services;

import datamodels.DistributionType;
import interfaces.ICoinDistributionAlgo;

public class CoinDistributionAlgoFactory {
    public static ICoinDistributionAlgo createCoinDistributionAlgo(DistributionType distributionType) {
        ICoinDistributionAlgo coinDistributionAlgo;
        switch (distributionType) {
            case Equal:
                coinDistributionAlgo = new EqualDistribution();
                break;
            case Random:
                coinDistributionAlgo = new RandomDistribution();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + distributionType);
        }

        return coinDistributionAlgo;
    }
}
