package datamodels;

import interfaces.IMixingRequest;
import interfaces.ITransaction;
import junit.framework.TestCase;

public class MixingRequestTest extends TestCase {
    IMixingRequest classUnderTest;

    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = new MixingRequest();
    }

    public void tearDown() throws Exception {
        classUnderTest = null;
    }

    public void testGetRequestBookingTime() {
    }

    public void testAttachTransaction() {
        ITransaction transaction =  new Transaction();
        classUnderTest.attachTransaction(transaction);
        assertTrue(classUnderTest.isTransactionAttached());
    }

    public void testIsTransactionAttached() {
        ITransaction transaction =  new Transaction();
        classUnderTest.attachTransaction(transaction);
        assertTrue(classUnderTest.isTransactionAttached());
    }

    public void testGetSourceAddress() {
        assertEquals(null, classUnderTest.getSourceAddress());
    }

    public void testGetAmount() {
        assertEquals(null, classUnderTest.getAmount());
    }

    public void testInitialize() {
    }

    public void testSetMixingRequestId() {
        classUnderTest.setMixingRequestId(1);
        assertEquals(1, classUnderTest.getMixingRequestId());
    }

    public void testGetMixingRequestId() {
        classUnderTest.setMixingRequestId(1);
        assertEquals(1, classUnderTest.getMixingRequestId());
    }

    public void testIsComplete() {
    }

    public void testGetAccounts() {
        assertEquals(null, classUnderTest.getAccounts());
    }

    public void testGetSplitType() {
    }

    public void testTestToString() {
        assertEquals(null, classUnderTest.toString());
    }
}