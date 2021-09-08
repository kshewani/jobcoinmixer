package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datamodels.MixingRequest;
import datamodels.Transaction;
import interfaces.IMixingRequest;
import interfaces.IMixingService;
import interfaces.IRestClient;
import junit.framework.TestCase;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;

public class MixingServiceTest extends TestCase {
    private IMixingService classUnderTest;
    final String MIXER_ADDRESS = "TEST_MIXER_ADDRESS";
    final String HOUSE_ADDRESS = "TEST_HOUSE_ADDRESS";
    final double MIXING_FEE = 0.01;
    @Mock
    IRestClient mockRestClient;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        classUnderTest = new MixingService(HOUSE_ADDRESS, MIXER_ADDRESS, MIXING_FEE, mockRestClient);
    }

    public void tearDown() throws Exception {
        classUnderTest = null;
        mockRestClient = null;
    }

    private MixingRequest submitMixingRequest() throws JsonProcessingException {
        String mixingRequestStr = "{\"sourceAddress\":\"Kam\",\"accounts\":[{\"address\":\"Alice\",\"amount\":0.0},{\"address\":\"Bob\",\"amount\":0.0},{\"address\":\"Hazy\",\"amount\":0.0}],\"amount\":\"15\",\"splitType\":\"Equal\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        MixingRequest mixingRequest = objectMapper.readValue(mixingRequestStr, MixingRequest.class);
        classUnderTest.submitMixingRequest(mixingRequest);
        return mixingRequest;
    }

    public void testSubmitMixingRequest_when_a_mixing_submitted_then_should_accept_it() throws JsonProcessingException {
        this.submitMixingRequest();
    }

    public void testOnNewTransaction_when_transaction_matches_mixing_request_should_perform_mixing() throws Exception {
        IMixingRequest mixingRequest = this.submitMixingRequest();
        String transactionStr = "{\"timestamp\":\"2022-08-30T08:30:12.232Z\",\"fromAddress\":\"Kam\",\"toAddress\":\"" + MIXER_ADDRESS + "\",\"amount\":\"15\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transaction = objectMapper.readValue(transactionStr, Transaction.class);
        doNothing().when(mockRestClient).sendTransaction(Mockito.any(Transaction.class));
        classUnderTest.onNewTransaction(transaction);
        Thread.currentThread().sleep(2000);
        Mockito.verify(mockRestClient, Mockito.atLeast(1)).sendTransaction(Mockito.any(Transaction.class));
        assertTrue(mixingRequest.isComplete());
    }

    public void testOnNewTransaction_when_transaction_does_not_match_mixing_request_should_refund() throws Exception {
        IMixingRequest mixingRequest = this.submitMixingRequest();
        String transactionStr = "{\"timestamp\":\"2022-08-30T08:30:12.232Z\",\"fromAddress\":\"Kam\",\"toAddress\":\"" + MIXER_ADDRESS + "\",\"amount\":\"12.5\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transaction = objectMapper.readValue(transactionStr, Transaction.class);
        doNothing().when(mockRestClient).sendTransaction(Mockito.any(Transaction.class));
        classUnderTest.onNewTransaction(transaction);
        Thread.currentThread().sleep(2000);
        Mockito.verify(mockRestClient, Mockito.atLeast(1)).sendTransaction(Mockito.any(Transaction.class));
        assertTrue(transaction.getIsRefunded());
        assertFalse(mixingRequest.isComplete());
    }
}