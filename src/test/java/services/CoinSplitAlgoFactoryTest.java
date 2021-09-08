package services;

import datamodels.SplitType;
import junit.framework.TestCase;

public class CoinSplitAlgoFactoryTest extends TestCase {
    CoinSplitAlgoFactory classUnderTest;
    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = new CoinSplitAlgoFactory();
    }

    public void tearDown() throws Exception {
    }

    public void testCreateCoinSplitAlgo_when_split_type_is_equal_should_create_EqualSplit_object() {
        assertTrue(new EqualSplit().getClass() == CoinSplitAlgoFactory.createCoinSplitAlgo(SplitType.Equal).getClass());
    }

    public void testCreateCoinSplitAlgo_when_split_type_is_random_should_create_RandomSplit_object() {
        assertTrue(new RandomSplit().getClass() == CoinSplitAlgoFactory.createCoinSplitAlgo(SplitType.Random).getClass());
    }
}