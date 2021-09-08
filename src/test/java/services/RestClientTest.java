package services;

import interfaces.IRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestClientTest {
    IRestClient classToTest;

    @BeforeEach
    void setUp() {
        classToTest = new RestClient("any_url");
    }

    @Test
    void setUrl() {
    }

    @Test
    void getTransactionsAsync() {
    }

    @Test
    void sendTransaction() {
    }
}