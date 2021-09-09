package datamodels;

import interfaces.ITransaction;
import junit.framework.TestCase;

public class TransactionTest extends TestCase {
    ITransaction classUnderTest;

    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = new Transaction("FROM_TEST", "TO_TEST", "10", "CURRENT_TIME");
    }

    public void tearDown() throws Exception {
        classUnderTest = null;
    }

    public void testGetFromAddress() {
        assertEquals("FROM_TEST", classUnderTest.getFromAddress());
    }

    public void testGetToAddress() {
        assertEquals("TO_TEST", classUnderTest.getToAddress());
    }

    public void testGetAmount() {
        assertEquals("10", classUnderTest.getAmount());
    }

    public void testGetTimestamp() {
        assertEquals("CURRENT_TIME", classUnderTest.getTimestamp());
    }

    public void testGetIsRefunded() {
        assertFalse(classUnderTest.getIsRefunded());
    }

    public void testSetIsRefunded() {
        classUnderTest.setIsRefunded(true);
        assertTrue(classUnderTest.getIsRefunded());
    }
}