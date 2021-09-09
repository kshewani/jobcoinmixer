package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datamodels.MixingRequest;
import interfaces.IMixingRequest;
import interfaces.IMixingRequestService;
import interfaces.IMixingService;
import interfaces.ITransactionService;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class MixingRequestServiceTest extends TestCase {
    IMixingRequestService classUnderTest;
    @Mock
    ITransactionService mockTransactionService;
    @Mock
    IMixingService mockMixingService;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        classUnderTest = new MixingRequestService(mockTransactionService, mockMixingService);
    }

    public void tearDown() throws Exception {
        classUnderTest = null;
        mockMixingService = null;
        mockTransactionService = null;
    }

    @Test(expected = Exception.class)
    public void testSubmitMixingRequest_when_submitted_mixing_service_throws_an_error() throws JsonProcessingException, InterruptedException {
        String mixingRequestStr = "{\"sourceAddress\":\"Kam\",\"accounts\":[{\"address\":\"Alice\",\"amount\":0.0},{\"address\":\"Bob\",\"amount\":0.0},{\"address\":\"Hazy\",\"amount\":0.0}],\"amount\":\"15\",\"splitType\":\"Random\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        IMixingRequest mixingRequest = objectMapper.readValue(mixingRequestStr, MixingRequest.class);
        Mockito.doThrow(Exception.class).when(mockMixingService).submitMixingRequest(mixingRequest);
        classUnderTest.submitMixingRequest(mixingRequest);
        Thread.currentThread().sleep(1000);
    }

    public void testSubmitMixingRequest_when_submitted_a_valid_mixing_request_should_send_request_to_mixing_service_and_initialize_equal_distribution_of_coins() throws JsonProcessingException, InterruptedException {
        String mixingRequestStr = "{\"sourceAddress\":\"Kam\",\"accounts\":[{\"address\":\"Alice\",\"amount\":0.0},{\"address\":\"Bob\",\"amount\":0.0},{\"address\":\"Hazy\",\"amount\":0.0}],\"amount\":\"15\",\"splitType\":\"Equal\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        IMixingRequest mixingRequest = objectMapper.readValue(mixingRequestStr, MixingRequest.class);
        Mockito.doNothing().when(mockMixingService).submitMixingRequest(mixingRequest);
        classUnderTest.submitMixingRequest(mixingRequest);
        Thread.currentThread().sleep(1000);
        verify(mockMixingService, Mockito.times(1)).submitMixingRequest(mixingRequest);
    }

    public void testSubmitMixingRequest_when_submitted_a_valid_mixing_request_should_send_request_to_mixing_service_and_initialize_random_distribution_of_coins() throws JsonProcessingException, InterruptedException {
        String mixingRequestStr = "{\"sourceAddress\":\"Kam\",\"accounts\":[{\"address\":\"Alice\",\"amount\":0.0},{\"address\":\"Bob\",\"amount\":0.0},{\"address\":\"Hazy\",\"amount\":0.0}],\"amount\":\"15\",\"splitType\":\"Random\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        IMixingRequest mixingRequest = objectMapper.readValue(mixingRequestStr, MixingRequest.class);
        Mockito.doNothing().when(mockMixingService).submitMixingRequest(mixingRequest);
        classUnderTest.submitMixingRequest(mixingRequest);
        Thread.currentThread().sleep(1000);
        verify(mockMixingService, Mockito.times(1)).submitMixingRequest(mixingRequest);
    }

}