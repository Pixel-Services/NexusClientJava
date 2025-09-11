package com.pixelservices.nexus.client.config;

import java.time.Duration;

/**
 * Configuration class for the Nexus client.
 * Provides default values and allows customization of client behavior.
 */
public class NexusClientConfig {
    private final String baseUrl;
    private final String token;
    private final String vendorId;
    private final int verifyAttempts;
    private final Duration verifyDelay;
    private final Duration connectionTimeout;
    private final Duration readTimeout;
    private final boolean enableLogging;
    private final int maxRetries;
    private final Duration retryDelay;

    private NexusClientConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.token = builder.token;
        this.vendorId = builder.vendorId;
        this.verifyAttempts = builder.verifyAttempts;
        this.verifyDelay = builder.verifyDelay;
        this.connectionTimeout = builder.connectionTimeout;
        this.readTimeout = builder.readTimeout;
        this.enableLogging = builder.enableLogging;
        this.maxRetries = builder.maxRetries;
        this.retryDelay = builder.retryDelay;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getToken() {
        return token;
    }

    public String getVendorId() {
        return vendorId;
    }

    public int getVerifyAttempts() {
        return verifyAttempts;
    }

    public Duration getVerifyDelay() {
        return verifyDelay;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public boolean isLoggingEnabled() {
        return enableLogging;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public Duration getRetryDelay() {
        return retryDelay;
    }

    public static class Builder {
        private String baseUrl = "https://nexus.pixel-services.com/api/";
        private String token;
        private String vendorId;
        private int verifyAttempts = 5;
        private Duration verifyDelay = Duration.ofSeconds(30);
        private Duration connectionTimeout = Duration.ofSeconds(30);
        private Duration readTimeout = Duration.ofSeconds(60);
        private boolean enableLogging = true;
        private int maxRetries = 3;
        private Duration retryDelay = Duration.ofSeconds(5);

        public Builder(String token, String vendorId) {
            this.token = token;
            this.vendorId = vendorId;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder verifyAttempts(int verifyAttempts) {
            this.verifyAttempts = verifyAttempts;
            return this;
        }

        public Builder verifyDelay(Duration verifyDelay) {
            this.verifyDelay = verifyDelay;
            return this;
        }

        public Builder connectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder enableLogging(boolean enableLogging) {
            this.enableLogging = enableLogging;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder retryDelay(Duration retryDelay) {
            this.retryDelay = retryDelay;
            return this;
        }

        public NexusClientConfig build() {
            return new NexusClientConfig(this);
        }
    }

    public static Builder builder(String token, String vendorId) {
        return new Builder(token, vendorId);
    }
}
