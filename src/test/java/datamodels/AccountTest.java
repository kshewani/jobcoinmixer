package datamodels;

import interfaces.IAccount;
import junit.framework.TestCase;

public class AccountTest extends TestCase {
    IAccount classUnderTest;

    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = new Account();
    }

    public void tearDown() throws Exception {
        classUnderTest = null;
    }

    public void testSetDistributionPercentage() {
        classUnderTest.setDistributionPercentage(50);
    }

    public void testSetAmount() {
        classUnderTest.setAmount(10);
        assertEquals(10.0, classUnderTest.getAmount());
    }

    public void testGetAddress() {
        assertEquals(null, classUnderTest.getAddress());
    }

    public void testGetAmount() {
        classUnderTest.setAmount(10);
        assertEquals(10.0, classUnderTest.getAmount());
    }

    public void testGetIsAmountTransferred() {
        assertFalse(classUnderTest.getIsAmountTransferred());
    }

    public void testSetIsAmountTransferred() {
        classUnderTest.setIsAmountTransferred(true);
        assertTrue(classUnderTest.getIsAmountTransferred());
    }
}