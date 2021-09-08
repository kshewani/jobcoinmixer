package services;

import datamodels.SplitType;
import interfaces.ICoinSplitAlgo;

public class CoinSplitAlgoFactory {
    public static ICoinSplitAlgo createCoinSplitAlgo(SplitType splitType) {
        switch (splitType) {
            case Equal:
                return new EqualSplit();
            case Random:
                return new RandomSplit();
        }

        return null;
    }
}
