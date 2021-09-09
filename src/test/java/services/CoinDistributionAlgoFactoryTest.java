package services;

import datamodels.DistributionType;
import junit.framework.TestCase;

public class CoinDistributionAlgoFactoryTest extends TestCase {
    CoinDistributionAlgoFactory classUnderTest;
    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = new CoinDistributionAlgoFactory();
    }

    public void tearDown() throws Exception {
    }

    public void testCreateCoinDistributionAlgo_when_distribution_type_is_equal_should_create_EqualDistribution_object() {
        assertTrue(new EqualDistribution().getClass() == CoinDistributionAlgoFactory.createCoinDistributionAlgo(DistributionType.Equal).getClass());
    }

    public void testCreateCoinDistributionAlgo_when_distribution_type_is_random_should_create_RandomDistribution_object() {
        assertTrue(new RandomDistribution().getClass() == CoinDistributionAlgoFactory.createCoinDistributionAlgo(DistributionType.Random).getClass());
    }
}