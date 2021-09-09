package datamodels;

import interfaces.IAccount;
import interfaces.IMixingRequest;
import interfaces.IMixingTransaction;
import junit.framework.TestCase;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import services.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class MixingTransactionTest extends TestCase {
    IMixingTransaction classUnderTest;
    @Mock
    IMixingRequest mockMixingRequest;
    @Mock
    IAccount mockAccount;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        classUnderTest = new MixingTransaction("TEST", Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))),
                mockMixingRequest,
                mockAccount);
    }

    public void tearDown() throws Exception {
    }

    public void testGetMixingRequestId() {
        Mockito.when(mockMixingRequest.getMixingRequestId()).thenReturn(1);
        assertEquals(1, classUnderTest.getMixingRequestId());
    }

    public void testGetAccount() {
        assertEquals(mockAccount, classUnderTest.getAccount());
    }

    public void testGetMixingRequest() {
        assertEquals(mockMixingRequest, classUnderTest.getMixingRequest());
    }
}