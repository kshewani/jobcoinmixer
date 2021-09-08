package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datamodels.Transaction;
import interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class controlling all the transaction processing logic.
 */
public class TransactionService implements ITransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private LocalDateTime lastTransactionDateTime;
    private final IEventQueue<ITransaction> transactionQueue;
    private final String mixerAddress;
    private final String houseAddress;
    private final IRestClient client;

    /**
     * Constructs a new TransactionService.
     * @param onNewTransaction the action to be performed when a new transaction is received.
     * @param mixerAddress the mixer address.
     * @param houseAddress the house address.
     * @param client the rest api client.
     */
    public TransactionService(IAction<ITransaction> onNewTransaction,
                              String mixerAddress,
                              String houseAddress,
                              IRestClient client) {
        this.transactionQueue = new EventQueue("TransactionQueue", onNewTransaction);
        this.mixerAddress = mixerAddress;
        this.houseAddress = houseAddress;
        this.client = client;
        lastTransactionDateTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    private Optional<ITransaction[]> parseTransactions(String transactions) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.of(objectMapper.readValue(transactions, Transaction[].class));
        } catch (JsonProcessingException e) {
            LOGGER.error("An error occurred while parsing the transaction. Error details: ");
            e.printStackTrace();
        }
        return null;
    }

    private boolean isMatchingTransaction(ITransaction t) {
        // Ignore transactions which are
        //  - not paid to mixer address.
        //  - paid by house address.
        //  - older than last recorded transaction time
        return t.getToAddress().equals(this.mixerAddress) &&
               !t.getFromAddress().equals(this.houseAddress) &&
                (Utils.stringToDate(t.getTimestamp()).compareTo(lastTransactionDateTime) > 0);
    }

    private void processTransaction(ITransaction transaction) {
        transactionQueue.addEvent(transaction);
        lastTransactionDateTime = Utils.stringToDate(transaction.getTimestamp());
    }

    private void pollTransactionsInternal(int interval) {
        while (true) {
            try {
                LOGGER.info("Polling for transactions.");
                CompletableFuture<String> transactionsFuture = this.getTransactionsAsync();
                this.processTransactions(transactionsFuture);
                Thread.sleep(interval);
            } catch (Exception e) {
                LOGGER.error("An error occurred while processing the mixing request. Error details: ");
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes the received transactions.
     * @param transactionsAsync The CompletableFuture containing transactions in json format.
     */
    @Override
    public void processTransactions(CompletableFuture<String> transactionsAsync) {
        try {
            Optional<ITransaction[]> transactionList = transactionsAsync.thenApply(this::parseTransactions).get();
            if (transactionList.isEmpty()) {
                LOGGER.info("No transactions received from server.");
            }

            ITransaction[] transactions = transactionList.get();
            LOGGER.info(String.format("Received %d transactions.", transactions.length));

            Arrays.stream(transactionList.get())
                    .parallel()
                    .filter(t -> isMatchingTransaction(t))
                    .forEach(this::processTransaction);
        } catch (Exception e) {
            LOGGER.error("An error occurred while processing the transaction. Error details: ");
            e.printStackTrace();
        }
    }

    /**
     * Gets transactions using the rest client.
     * @return A CompletableFuture containing transactions json string.
     * @throws Exception
     */
    @Override
    public CompletableFuture<String> getTransactionsAsync() throws Exception {
        return client.getTransactionsAsync();
    }

    /**
     * Periodically polls for transactions.
     */
    @Override
    public void pollTransactions(int interval) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> this.pollTransactionsInternal(interval));
    }
}
