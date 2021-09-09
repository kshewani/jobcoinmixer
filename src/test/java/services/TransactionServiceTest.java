package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import datamodels.Transaction;
import interfaces.IRestClient;
import interfaces.ITransactionService;
import junit.framework.TestCase;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;

public class TransactionServiceTest extends TestCase {
    final String MIXER_ADDRESS = "TEST_MIXER_ADDRESS";
    final String HOUSE_ADDRESS = "TEST_HOUSE_ADDRESS";

    ITransactionService classUnderTest;
    @Mock
    IRestClient mockRestClient;
    @Mock
    ObjectMapper mockObjectMapper;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        classUnderTest = new TransactionService((t)-> {},
                "TEST_MIXER_ADDRESS",
                "TEST_HOUSE_ADDRESS",
                mockRestClient,
                mockObjectMapper);
    }

    public void tearDown() throws Exception {
        classUnderTest = null;
    }

    public void testPollTransactions_when_there_are_some_transactions_received_from_server_then_should_process_transactions() throws Exception {
        String transactions = "[{\"timestamp\":\"2021-08-30T08:30:12.232Z\",\"fromAddress\":\"Alice\",\"toAddress\":\"Bob\",\"amount\":\"12.5\"}]";
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction[] transactionsArray = objectMapper.readValue(transactions, Transaction[].class);
        CompletableFuture<String> transactionsFuture = CompletableFuture.completedFuture(transactions);
        Mockito.when(mockRestClient.getTransactionsAsync()).thenReturn(transactionsFuture);
        Mockito.when(mockObjectMapper.readValue(transactions, Transaction[].class)).thenReturn(transactionsArray);
        classUnderTest.pollTransactions(10);
        Thread.currentThread().sleep(1000);
        classUnderTest.stopPolling();
        Mockito.verify(mockRestClient, Mockito.atLeast(1)).getTransactionsAsync();
        Mockito.verify(mockObjectMapper, Mockito.atLeast(1)).readValue(transactions, Transaction[].class);
    }

    public void testPollTransactions_when_there_are_no_transactions_received_from_server_then_should_not_continue_processing() throws Exception {
        String transactions = null;
        CompletableFuture<String> transactionsFuture = CompletableFuture.completedFuture(transactions);
        Mockito.when(mockRestClient.getTransactionsAsync()).thenReturn(transactionsFuture);
        Mockito.when(mockObjectMapper.readValue(transactions, Transaction[].class)).thenReturn(new Transaction[0]);
        classUnderTest.pollTransactions(10);
        Thread.currentThread().sleep(1000);
        classUnderTest.stopPolling();
        Mockito.verify(mockRestClient, Mockito.atLeast(1)).getTransactionsAsync();
    }

    public void testPollTransactions_when_there_is_a_transaction_to_mixer_account_then_should_add_transaction_to_mixing_queue() throws Exception {
        String timestamp = Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")));
        // String transactions = "[{\"timestamp\":\"" + timestamp + "\",\"fromAddress\":\" + Alice + \",\"toAddress\":\"" + MIXER_ADDRESS + "\",\"amount\":\"12.5\"}]";
        String transactions = "[{\"timestamp\":\"2022-08-30T08:30:12.232Z\",\"fromAddress\":\"Alice\",\"toAddress\":\"" + MIXER_ADDRESS + "\",\"amount\":\"12.5\"}]";
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction[] transactionsArray = objectMapper.readValue(transactions, Transaction[].class);
        CompletableFuture<String> transactionsFuture = CompletableFuture.completedFuture(transactions);
        Mockito.when(mockRestClient.getTransactionsAsync()).thenReturn(transactionsFuture);
        Mockito.when(mockObjectMapper.readValue(transactions, Transaction[].class)).thenReturn(transactionsArray);
        classUnderTest.pollTransactions(10);
        Thread.currentThread().sleep(1000);
        //classUnderTest.setCancelPolling(true);
        Mockito.verify(mockRestClient, Mockito.atLeast(1)).getTransactionsAsync();
        Mockito.verify(mockObjectMapper, Mockito.atLeast(1)).readValue(transactions, Transaction[].class);
    }
}