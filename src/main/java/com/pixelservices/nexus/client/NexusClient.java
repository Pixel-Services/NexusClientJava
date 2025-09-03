package com.pixelservices.nexus.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelservices.logger.Logger;
import com.pixelservices.logger.LoggerFactory;
import com.pixelservices.nexus.client.exception.NexusClientAuthenticationException;
import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.user.UserRepository;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NexusClient {
    private static NexusClient instance;

    private final String baseUrl;

    // Authentication
    private final String token;
    private final String  vendorId;

    // Client and Mapper
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    // Logger
    private final Logger logger = LoggerFactory.getLogger(NexusClient.class);

    private NexusClient(@NotNull String baseUrl, @NotNull String token, @NotNull String vendorId) {
        this.baseUrl = baseUrl;

        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();

        this.token = token;
        this.vendorId = vendorId;

        instance = this;


    }

    final <T> T get(String endpoint, Class<T> responseType) {
        return executeRequest(new HttpGet(baseUrl + endpoint), responseType);
    }

    final <T> T post(String endpoint, Object body, Class<T> responseType) {
        HttpPost postRequest = new HttpPost(baseUrl + endpoint);
        try {
            if (body != null) {
                postRequest.setEntity(new StringEntity(objectMapper.writeValueAsString(body)));
            }
            postRequest.setHeader("Content-Type", "application/json");
            return executeRequest(postRequest, responseType);
        } catch (IOException e) {
            logger.error("Failed to create or execute POST request", e);
        }
        return null;
    }

    final <T> T put(String endpoint, Object body, Class<T> responseType) {
        HttpPut putRequest = new HttpPut(baseUrl + endpoint);
        try {
            if (body != null) {
                putRequest.setEntity(new StringEntity(objectMapper.writeValueAsString(body)));
            }
            putRequest.setHeader("Content-Type", "application/json");
            return executeRequest(putRequest, responseType);
        } catch (IOException e) {
            logger.error("Failed to create or execute PUT request", e);
        }

        return null;
    }

    private <T> T executeRequest(HttpRequestBase request, Class<T> responseType) {
        request.addHeader("X-Vendor-Id", vendorId);
        request.addHeader("X-Vendor-Access-Token", token);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                return objectMapper.readValue(response.getEntity().getContent(), responseType);
            } else {
                throw new RuntimeException("HTTP error: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            logger.error("Failed to execute request", e);
        }

        return null;
    }

    public static NexusClientBuilder withToken(String token) {
        return new NexusClientBuilder(token);
    }

    static NexusClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("NexusClient is not initialized. Please create an instance using the builder.");
        }
        return instance;
    }

    public static class NexusClientBuilder {
        private final Logger logger = LoggerFactory.getLogger(NexusClientBuilder.class);
        private final CloseableHttpClient httpClient = HttpClients.createDefault();
        private String baseUrl = "https://nexus.pixel-services.com/api/";
        private final String token;
        private String vendorId;
        private int verifyAttempts = 5;
        private int verifyDelayMs = 30000;

        public NexusClientBuilder(String token) {
            this.token = token;
        }

        public NexusClientBuilder withVendorId(String vendorId) {
            this.vendorId = vendorId;
            return this;
        }

        public NexusClientBuilder withBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NexusClientBuilder withVerifyAttempts(int attempts) {
            this.verifyAttempts = attempts;
            return this;
        }

        public NexusClientBuilder withVerifyDelay(int delayMs) {
            this.verifyDelayMs = delayMs;
            return this;
        }

        /**
         * Builds the NexusClient instance after verifying the connection.
         *
         * @throws NexusClientAuthenticationException if the connection cannot be verified.
         * @return NexusClient instance
         */
        public NexusClient build() {
            while (verifyAttempts-- > 0) {
                if (verifyConnection()) {
                    break;
                } else {
                    logger.warn("Trying to verify connection again in " + (verifyDelayMs / 1000) + " seconds... (" + (verifyAttempts + 1) + " attempts left)");
                }
                if (verifyAttempts == 0) {
                    throw new NexusClientAuthenticationException("Failed to verify connection after multiple attempts.");
                }
                try {
                    Thread.sleep(verifyDelayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new NexusClientException("Verification interrupted", e);
                }
            }

            return new NexusClient(baseUrl, token, vendorId);
        }

        /**
         * This method verifies the BaseUrl, token, and vendorId by making a test request
         * to the Nexus API.
         *
         * @return true if the connection is verified, false otherwise.
         */
        private boolean verifyConnection() {
            HttpGet request = new HttpGet(baseUrl + "/ping");
            request.addHeader("X-Vendor-Id", vendorId);
            request.addHeader("X-Vendor-Access-Token", token);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 404 || statusCode == 525) {
                    logger.error("Unable to verify connection — Invalid base URL: " + baseUrl);
                    return false;
                } else if (statusCode != 200) {
                    logger.error("Unable to verify connection — Authentication failed: Invalid token or vendor ID.");
                    return false;
                } else {
                    logger.info("Connection verified successfully.");
                    return true;
                }
            } catch (IOException e) {
                logger.error("Unable to verify connection — An unexpected error occurred while verifying the connection.", e);
                return false;
            }
        }
    }
}
