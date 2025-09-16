package com.pixelservices.nexus.client;

import com.pixelservices.logger.Logger;
import com.pixelservices.logger.LoggerFactory;
import com.pixelservices.nexus.client.exception.NexusClientAuthenticationException;
import com.pixelservices.nexus.client.exception.NexusClientException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.time.Duration;

public class NexusClientBuilder {
    private final Logger logger = LoggerFactory.getLogger(NexusClientBuilder.class);
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final String token;
    private String vendorId;

    private String baseUrl = "https://nexus.pixel-services.com";
    private int verifyAttempts = 5;
    private Duration verifyDelay = Duration.ofSeconds(30);

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

    public NexusClientBuilder withVerifyAttempts(int verifyAttempts) {
        this.verifyAttempts = verifyAttempts;
        return this;
    }

    public NexusClientBuilder withVerifyDelay(Duration verifyDelay) {
        this.verifyDelay = verifyDelay;
        return this;
    }

    /**
     * Builds the NexusClient instance after verifying the connection.
     *
     * @throws NexusClientAuthenticationException if the connection cannot be verified.
     * @return NexusClient instance
     */
    public NexusClient build() {
        if (vendorId == null) {
            throw new IllegalArgumentException("VendorId is required");
        }

        verifyConnection();

        return new NexusClient(baseUrl, token, vendorId);
    }

    /**
     * This method verifies the BaseUrl, token, and vendorId by making a test request
     * to the Nexus API.
     *
     * @throws NexusClientAuthenticationException if verification fails
     */
    private void verifyConnection() {
        int attempts = verifyAttempts;
        while (attempts-- > 0) {
            if (performConnectionTest()) {
                logger.info("Connection verified successfully.");
                return;
            } else {
                logger.warn("Trying to verify connection again in " + verifyDelay.getSeconds() + " seconds... (" + (attempts + 1) + " attempts left)");
            }
            if (attempts == 0) {
                throw new NexusClientAuthenticationException("Failed to verify connection after multiple attempts.");
            }
            try {
                Thread.sleep(verifyDelay.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NexusClientException("Verification interrupted", e);
            }
        }
    }

    /**
     * Performs a single connection test.
     *
     * @return true if the connection is verified, false otherwise
     */
    private boolean performConnectionTest() {
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
                return true;
            }
        } catch (IOException e) {
            logger.error("Unable to verify connection — An unexpected error occurred while verifying the connection.", e);
            return false;
        }
    }
}
