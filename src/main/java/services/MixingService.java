package services;

import datamodels.Account;
import datamodels.MixingTransaction;
import datamodels.Transaction;
import interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class to control all the mixing logic.
 */
public class MixingService implements IMixingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MixingService.class);

    private final String houseAddress;
    private final String mixerAddress;
    private final double mixingFeeFactor;
    private final IRestClient client;
    private final IEventQueue<IMixingTransaction> mixingQueue;
    private final Random random = new Random();
    private final Map<String, IMixingRequest> mixingRequestMap;
    private double houseAccountBalance;
    private double mixingAccountBalance;

    /**
     * Constructs a new MixingService object.
     * @param houseAddress the house address.
     * @param mixerAddress the mixer address.
     * @param mixingFeeFactor the mixing fee factor.
     * @param client the rest api client.
     */
    public MixingService(String houseAddress, String mixerAddress, double mixingFeeFactor, IRestClient client) {
        this.houseAddress = houseAddress;
        this.mixerAddress = mixerAddress;
        this.mixingFeeFactor = mixingFeeFactor;
        this.client = client;
        mixingQueue = new EventQueue<>("MixingQueue", m -> onMixer(m));
        mixingRequestMap = new ConcurrentHashMap<>();
    }

    private void refund(ITransaction transaction) {
        try {
            LOGGER.info(String.format("Refunding %s coins to house account.", transaction.getAmount()));
            ITransaction refundTransaction = new Transaction(mixerAddress,
                    transaction.getFromAddress(),
                    String.valueOf(transaction.getAmount()),
                    Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))));
            client.sendTransaction(refundTransaction);
            LOGGER.info(String.format("Refunded %s to house account.", transaction.getAmount()));
        } catch (Exception e) {
            LOGGER.error("An error occurred while refunding transaction. Error details: ");
            e.printStackTrace();
        }
    }

    private void transferToHouseAccount(ITransaction transaction) {
        try {
            LOGGER.info(String.format("Transferring %s to house account.", transaction.getAmount()));
            synchronized (this) {
                this.houseAccountBalance += Double.parseDouble(transaction.getAmount()); //TODO: thread synchronization
            }

            // transfer the coins to house account.
            client.sendTransaction(new Transaction(mixerAddress,
                    houseAddress,
                    transaction.getAmount(),
                    Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")))));
            LOGGER.info(String.format("Transferred %s coins to house account.", transaction.getAmount()));
        } catch (Exception e) {
            LOGGER.error("An error occurred while transferring to house account. Error details: ");
            e.printStackTrace();
        }
    }

    private boolean isValidTransactionForMixing(IMixingRequest mixingRequest, ITransaction transaction) {
        /* Transaction not valid to proceed for mixing if:
              - transaction was not refunded back already.
              - there is no corresponding mixing request found for the from address in transaction.
              - mixing request is not complete.
              - there is no transaction already attached to the mixing request. If there is, then this is a duplicate
                transaction.
              - the transaction was booked after booking the mixing request.
              - the amount transferred is not same as mentioned in mixing request.*/
        return (!transaction.getIsRefunded()) &&
               (mixingRequest != null) &&
               (!mixingRequest.isComplete()) &&
               (!mixingRequest.isTransactionAttached()) &&
               (Utils.stringToDate(transaction.getTimestamp()).compareTo(mixingRequest.getRequestBookingTime()) > 0) ||
               (Double.parseDouble(mixingRequest.getAmount()) == Double.parseDouble(transaction.getAmount()));
    }

    /*private void sendTransactionToMixingQueue(IMixingRequest mixingRequest, Account account) {
        LOGGER.info("Adding transaction to mixing queue.");
        mixingQueue.addEvent(new MixingTransaction(houseAddress,
                account.getAddress(),
                String.valueOf(account.getAmount()),
                Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))),
                mixingRequest,
                mixingRequest.getMixingRequestId()));
        LOGGER.info("Added transaction to mixing queue.");
    }*/

    private void sendTransactionToMixingQueue(IMixingRequest mixingRequest, Account account) {
        LOGGER.info("Adding transaction to mixing queue.");
        mixingQueue.addEvent(new MixingTransaction(houseAddress,
                /*account.getAddress(),
                String.valueOf(account.getAmount()),*/
                Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))),
                mixingRequest,
                account));
        // LOGGER.info("Added transaction to mixing queue.");
    }

    private void onMixer(IMixingTransaction mixingTransaction) {
        try {
            /*
            1. Compute mixing fee.
            2. Deduct mixing fee from amount to be transferred.
            3. Add mixing fee to mixer account.
            4. Send coins to one of the desired recipient.
             */
            IMixingRequest mixingRequest = mixingTransaction.getMixingRequest();
            int mixingRequestId = mixingTransaction.getMixingRequestId();
            LOGGER.info(String.format("Performing mixing transaction for mixing id: %d",
                    mixingRequestId));
            double amountToTransfer = Double.parseDouble(mixingTransaction.getAmount());

            // compute mixing fee rounded off to 4 decimal places.
            var mixingFee = (double)Math.round(mixingFeeFactor * amountToTransfer * 10000d) / 10000d;

            // TODO: synchronization
            synchronized (this) {
                mixingAccountBalance += mixingFee;
                houseAccountBalance -= amountToTransfer;
            }

            amountToTransfer -= mixingFee;

            LOGGER.info(String.format("Transferring %s coins to mixer account to pay mixing fee.", mixingFee));
            client.sendTransaction(new Transaction(houseAddress,
                    mixerAddress,
                    String.valueOf(mixingFee),
                    Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")))));
            LOGGER.info(String.format("Transferred %s coins to mixer account to pay mixing fee.", mixingFee));

            String toAddress = mixingTransaction.getToAddress();

            LOGGER.info(String.format("Mixing request id: %d, Transferring %s to %s after deducting %4f mixing fee.",
                    mixingRequestId, amountToTransfer, toAddress, mixingFee));
            client.sendTransaction(new Transaction(houseAddress,
                    toAddress,
                    String.valueOf(amountToTransfer),
                    Utils.dateToString(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")))));
            LOGGER.info(String.format("Mixing request id: %d, Transferred %s to %s after deducting %4f mixing fee.",
                    mixingRequestId, amountToTransfer, toAddress, mixingFee));

            mixingTransaction.getAccount().setIsAmountTransferred(true);

            if (mixingRequest.isComplete()) {
                LOGGER.info(String.format("Mixing request with ID %s successfully completed...",
                        mixingRequest.getMixingRequestId()));
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while performing mixing. Error details: ");
            e.printStackTrace();
        }
    }

    /**
     * Submits a mixing request to mixing service.
     * @param mixingRequest the mixing request.
     */
    @Override
    public void submitMixingRequest(IMixingRequest mixingRequest) {
        mixingRequestMap.put(mixingRequest.getSourceAddress(), mixingRequest);
    }

    /**
     * Performs all required actions when a new transaction is received.
     * @param transaction the transaction.
     */
    @Override
    public void onNewTransaction(ITransaction transaction) {
        try {
            /*
            1. Map transaction to a mixing request.
            2. Validate the transaction.
            3. If not valid, refund the amount to sender.
            4. Attach transaction to mixing request
            5. If valid, transfer the coins to house address.
            6. Send to mixing queue for processing
            * */
            String transactionSource = transaction.getFromAddress();
            IMixingRequest mixingRequest = mixingRequestMap.get(transactionSource);

            // Refund coins if transaction is not valid to proceed for mixing
            if (!isValidTransactionForMixing(mixingRequest, transaction)) {
                this.refund(transaction);
                transaction.setIsRefunded(true);
                return;
            }

            // attach transaction to mixing request.
            synchronized (this) {
                mixingRequest.attachTransaction(transaction);
            }

            // transfer the coins to house account.
            this.transferToHouseAccount(transaction);

            // send to mixing queue for processing
            mixingRequest.getAccounts().
                    forEach(a -> this.sendTransactionToMixingQueue(mixingRequest, a));
        } catch (Exception e) {
            LOGGER.error("An error occurred while processing transaction. Error details: ");
            e.printStackTrace();
        }
    }
}
