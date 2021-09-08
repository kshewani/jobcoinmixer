package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.IRestClient;
import interfaces.ITransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * The rest api client to interact with server.
 */
public class RestClient implements IRestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
    String url;

    /**
     * Constructs a new Client.
     */
    public RestClient() {
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets transactions by calling the transaction api.
     * @return A CompletableFuture containing transactions json string.
     * @throws Exception
     */
    @Override
    public CompletableFuture<String> getTransactionsAsync() {
        try {
            LOGGER.info("Fetching transactions from REST server.");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.url))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body);
        }
        catch (Exception e) {
            LOGGER.error("An error occurred while getting transactions. Error details: ");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * Sends a new transaction by calling transaction api.
     * @param newTransaction a new transaction
     * @throws Exception
     */
    @Override
    public void sendTransaction(ITransaction newTransaction) throws IOException, InterruptedException {
        try {
            LOGGER.info("Sending transaction to REST.");
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(newTransaction);
            LOGGER.info("Transaction details: " + json);
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.url))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .setHeader("Content-Type", "application/json")
                    .build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Unable to send transaction:  Response code: " + response);
            }
            LOGGER.info("Transaction successfully sent");
        } catch (Exception e) {
            LOGGER.error("An error occurred while sending transaction. Error details: ");
            e.printStackTrace();
            throw e;
        }
    }
}
