import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datamodels.MixingRequest;
import interfaces.IMixingRequestService;
import interfaces.IMixingService;
import interfaces.IRestClient;
import interfaces.ITransactionService;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.MixingRequestService;
import services.MixingService;
import services.RestClient;
import services.TransactionService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Properties props;
        try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream("app.properties")) {
            props = new Properties();
            props.load(resourceAsStream);
        }
        // Sample mixing requests:
        // "{\"sourceAddress\":\"Kam\",\"accounts\":[{\"address\":\"Alice\",\"amount\":0.0},{\"address\":\"Bob\",\"amount\":0.0},{\"address\":\"Hazy\",\"amount\":0.0}],\"amount\":\"15\",\"distributionType\":\"Equal\"}";
        // "{\"sourceAddress\":\"Kam\",\"accounts\":[{\"address\":\"Alice\",\"amount\":0.0},{\"address\":\"Bob\",\"amount\":0.0},{\"address\":\"Hazy\",\"amount\":0.0}],\"amount\":\"15\",\"distributionType\":\"Random\"}";
        System.out.println("Mixing request: " + args[0]);
        run(props, args);
    }

    private static IMixingRequestService bootstrap(Properties appProps) {
        // this should actually be done using some IOC framework. For eg. Spring
        // "https://jobcoin.gemini.com/anymore-dose/api/transactions";
        IRestClient client = new RestClient(appProps.get("jobcoin.url").toString());
        IMixingService mixingService = new MixingService(appProps.get("jobcoin.house.address").toString(),
                appProps.get("jobcoin.mixer.address").toString(),
                Double.parseDouble(appProps.get("jobcoin.mixing.fee.factor").toString()),
                client);
        ITransactionService transactionService = new TransactionService(t -> mixingService.onNewTransaction(t),
                "KAM_MIXER", appProps.get("jobcoin.house.address").toString(), client, new ObjectMapper());
        transactionService.pollTransactions(Integer.valueOf(appProps.get("jobcoin.transactions.polling.interval").toString()));
        return new MixingRequestService(transactionService, mixingService);
    }

    private static void run(Properties props, String... args) throws JsonProcessingException {
        LOGGER.info("Starting jobcoinmixer application.");
        ObjectMapper objectMapper = new ObjectMapper();
        MixingRequest mixingRequest = objectMapper.readValue(args[0], MixingRequest.class);

        IMixingRequestService mixingRequestService = bootstrap(props);
        mixingRequestService.submitMixingRequest(mixingRequest);
    }
}
