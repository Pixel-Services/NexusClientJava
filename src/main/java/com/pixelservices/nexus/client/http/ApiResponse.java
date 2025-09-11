package com.pixelservices.nexus.client.http;

import com.pixelservices.nexus.client.exception.NexusClientException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Wrapper class for API responses that includes metadata and error handling.
 * Provides a consistent way to handle both successful and failed responses.
 *
 * @param <T> the type of data contained in the response
 */
public class ApiResponse<T> {
    private final T data;
    private final boolean success;
    private final int statusCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, String> headers;
    private final String rawResponse;

    private ApiResponse(Builder<T> builder) {
        this.data = builder.data;
        this.success = builder.success;
        this.statusCode = builder.statusCode;
        this.message = builder.message;
        this.timestamp = builder.timestamp;
        this.headers = builder.headers;
        this.rawResponse = builder.rawResponse;
    }

    /**
     * Gets the response data.
     *
     * @return the response data, or null if the request failed
     */
    public T getData() {
        return data;
    }

    /**
     * Checks if the request was successful.
     *
     * @return true if the request was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the response message.
     *
     * @return the response message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the timestamp when the response was received.
     *
     * @return the response timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the response headers.
     *
     * @return a map of response headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Gets the raw response body.
     *
     * @return the raw response body as a string
     */
    public String getRawResponse() {
        return rawResponse;
    }

    /**
     * Throws an exception if the response indicates an error.
     *
     * @throws NexusClientException if the response indicates an error
     */
    public void throwIfError() throws NexusClientException {
        if (!success) {
            throw new NexusClientException("API request failed: " + message + " (Status: " + statusCode + ")");
        }
    }

    /**
     * Gets the data or throws an exception if the request failed.
     *
     * @return the response data
     * @throws NexusClientException if the request failed
     */
    public T getDataOrThrow() throws NexusClientException {
        throwIfError();
        return data;
    }

    public static class Builder<T> {
        private T data;
        private boolean success;
        private int statusCode;
        private String message;
        private LocalDateTime timestamp = LocalDateTime.now();
        private Map<String, String> headers;
        private String rawResponse;

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder<T> headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder<T> rawResponse(String rawResponse) {
            this.rawResponse = rawResponse;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(this);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
}
