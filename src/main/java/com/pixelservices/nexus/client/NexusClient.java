package com.pixelservices.nexus.client;

import com.pixelservices.logger.Logger;
import com.pixelservices.logger.LoggerFactory;
import com.pixelservices.nexus.client.config.NexusClientConfig;
import com.pixelservices.nexus.client.exception.NexusClientAuthenticationException;
import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.http.DefaultHttpClient;
import com.pixelservices.nexus.client.http.HttpClient;
import com.pixelservices.nexus.client.monitoring.ClientMetrics;
import com.pixelservices.nexus.client.user.UserRepository;
import com.pixelservices.nexus.client.service.ServiceRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;

public class NexusClient implements AutoCloseable {
    private final NexusClientConfig config;
    private final HttpClient httpClient;

    // Repositories
    public final UserRepository userRepository;
    public final ServiceRepository serviceRepository;

    private NexusClient(@NotNull NexusClientConfig config) {
        this.config = config;
        this.httpClient = new DefaultHttpClient(
            config.getBaseUrl(), 
            config.getToken(), 
            config.getVendorId()
        );


        // Initialize repositories
        this.userRepository = new InternalUserRepository(httpClient);
        this.serviceRepository = new InternalServiceRepository(httpClient);
    }

    /**
     * Gets the HTTP client instance for advanced usage.
     * 
     * @return the HTTP client instance
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Gets the client configuration.
     * 
     * @return the client configuration
     */
    public NexusClientConfig getConfig() {
        return config;
    }

    /**
     * Gets the client metrics for monitoring and debugging.
     *
     * @return the client metrics
     */
    public ClientMetrics getMetrics() {
        if (httpClient instanceof DefaultHttpClient) {
            return ((DefaultHttpClient) httpClient).getMetrics();
        }
        return null;
    }

    /**
     * Closes the client and releases resources.
     */
    public void close() {
        if (httpClient instanceof DefaultHttpClient) {
            ((DefaultHttpClient) httpClient).close();
        }
    }

    public static NexusClientBuilder withToken(String token) {
        return new NexusClientBuilder(token);
    }

    public static class NexusClientBuilder {
        private final Logger logger = LoggerFactory.getLogger(NexusClientBuilder.class);
        private final CloseableHttpClient httpClient = HttpClients.createDefault();
        private final String token;
        private String vendorId;
        private NexusClientConfig config;

        public NexusClientBuilder(String token) {
            this.token = token;
        }

        public NexusClientBuilder withVendorId(String vendorId) {
            this.vendorId = vendorId;
            return this;
        }

        public NexusClientBuilder withConfig(NexusClientConfig config) {
            this.config = config;
            return this;
        }

        /**
         * Builds the NexusClient instance after verifying the connection.
         *
         * @throws NexusClientAuthenticationException if the connection cannot be verified.
         * @return NexusClient instance
         */
        public NexusClient build() {
            if (config == null) {
                if (vendorId == null) {
                    throw new IllegalArgumentException("VendorId is required");
                }
                config = NexusClientConfig.builder(token, vendorId).build();
            }

            // Verify connection
            verifyConnection(config);

            return new NexusClient(config);
        }

        /**
         * This method verifies the BaseUrl, token, and vendorId by making a test request
         * to the Nexus API.
         *
         * @param config the client configuration
         * @throws NexusClientAuthenticationException if verification fails
         */
        private void verifyConnection(NexusClientConfig config) {
            int attempts = config.getVerifyAttempts();
            Duration delay = config.getVerifyDelay();

            while (attempts-- > 0) {
                if (performConnectionTest(config)) {
                    logger.info("Connection verified successfully.");
                    return;
                } else {
                    logger.warn("Trying to verify connection again in " + delay.getSeconds() + " seconds... (" + (attempts + 1) + " attempts left)");
                }
                if (attempts == 0) {
                    throw new NexusClientAuthenticationException("Failed to verify connection after multiple attempts.");
                }
                try {
                    Thread.sleep(delay.toMillis());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new NexusClientException("Verification interrupted", e);
                }
            }
        }

        /**
         * Performs a single connection test.
         *
         * @param config the client configuration
         * @return true if the connection is verified, false otherwise
         */
        private boolean performConnectionTest(NexusClientConfig config) {
            HttpGet request = new HttpGet(config.getBaseUrl() + "/ping");
            request.addHeader("X-Vendor-Id", config.getVendorId());
            request.addHeader("X-Vendor-Access-Token", config.getToken());

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 404 || statusCode == 525) {
                    logger.error("Unable to verify connection — Invalid base URL: " + config.getBaseUrl());
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
}
