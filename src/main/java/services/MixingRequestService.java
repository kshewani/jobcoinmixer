package services;

import interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class to serve new mixing requests.
 */
public class MixingRequestService implements IMixingRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MixingRequestService.class);

    private final IEventQueue<IMixingRequest> mixingQueue;
    private final ITransactionService transactionService;
    private final IMixingService mixingService;
    private final Map<String, IMixingRequest> mixingRequestMap;
    private static int mixingRequestId = 1;

    public MixingRequestService(ITransactionService transactionService, IMixingService mixingService) {
        this.transactionService = transactionService;
        this.mixingService = mixingService;
        this.mixingQueue =  new EventQueue<>("MixingRequestQueue", m -> onMixingRequest(m));
        mixingRequestMap = new ConcurrentHashMap<>();
    }

    private void onMixingRequest(IMixingRequest mixingRequest) {
        try {
            LOGGER.info("Initializing mixing request: " + mixingRequest.getMixingRequestId());
            mixingRequest.initialize();
            LOGGER.info("Mixing request post initialization. Id: " + mixingRequest.getMixingRequestId());
            LOGGER.info(mixingRequest.toString());
            mixingService.submitMixingRequest(mixingRequest);
            LOGGER.info("Fetching transactions");
        } catch (Exception e) {
            LOGGER.error("An error occurred while processing the mixing request. Error details: ");
            e.printStackTrace();
        }
    }

    /**
     * Submits a mixing request to mixing service.
     * @param mixingRequest the mixing request.
     */
    @Override
    public void submitMixingRequest(IMixingRequest mixingRequest) {
        LOGGER.info("Received mixing request:");
        LOGGER.info(mixingRequest.toString());
        LOGGER.info("Setting mixing request id to: " + mixingRequestId);
        mixingRequest.setMixingRequestId(mixingRequestId++);
        mixingQueue.addEvent(mixingRequest);
    }
}
