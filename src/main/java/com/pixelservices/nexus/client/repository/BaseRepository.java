package com.pixelservices.nexus.client.repository;

import com.pixelservices.logger.Logger;
import com.pixelservices.logger.LoggerFactory;
import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.http.ApiResponse;
import com.pixelservices.nexus.client.http.HttpClient;

/**
 * Base repository class that provides common functionality for all repositories.
 * Handles HTTP client operations and provides utility methods for data transformation.
 */
public abstract class BaseRepository {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected final HttpClient httpClient;

    protected BaseRepository(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Executes a GET request and returns the data or throws an exception.
     *
     * @param endpoint the API endpoint
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the response data
     * @throws NexusClientException if the request fails
     */
    protected <T> T get(String endpoint, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing GET request to endpoint: " + endpoint);
        ApiResponse<T> response = httpClient.get(endpoint, responseType);
        return response.getDataOrThrow();
    }

    /**
     * Executes a POST request and returns the data or throws an exception.
     *
     * @param endpoint the API endpoint
     * @param body the request body
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the response data
     * @throws NexusClientException if the request fails
     */
    protected <T> T post(String endpoint, Object body, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing POST request to endpoint: " + endpoint);
        ApiResponse<T> response = httpClient.post(endpoint, body, responseType);
        return response.getDataOrThrow();
    }

    /**
     * Executes a PUT request and returns the data or throws an exception.
     *
     * @param endpoint the API endpoint
     * @param body the request body
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the response data
     * @throws NexusClientException if the request fails
     */
    protected <T> T put(String endpoint, Object body, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing PUT request to endpoint: " + endpoint);
        ApiResponse<T> response = httpClient.put(endpoint, body, responseType);
        return response.getDataOrThrow();
    }

    /**
     * Executes a DELETE request and returns the data or throws an exception.
     *
     * @param endpoint the API endpoint
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the response data
     * @throws NexusClientException if the request fails
     */
    protected <T> T delete(String endpoint, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing DELETE request to endpoint: " + endpoint);
        ApiResponse<T> response = httpClient.delete(endpoint, responseType);
        return response.getDataOrThrow();
    }

    /**
     * Executes a GET request and returns the full response for custom handling.
     *
     * @param endpoint the API endpoint
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the full API response
     * @throws NexusClientException if the request fails
     */
    protected <T> ApiResponse<T> getResponse(String endpoint, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing GET request to endpoint: " + endpoint);
        return httpClient.get(endpoint, responseType);
    }

    /**
     * Executes a POST request and returns the full response for custom handling.
     *
     * @param endpoint the API endpoint
     * @param body the request body
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the full API response
     * @throws NexusClientException if the request fails
     */
    protected <T> ApiResponse<T> postResponse(String endpoint, Object body, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing POST request to endpoint: " + endpoint);
        return httpClient.post(endpoint, body, responseType);
    }

    /**
     * Executes a PUT request and returns the full response for custom handling.
     *
     * @param endpoint the API endpoint
     * @param body the request body
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the full API response
     * @throws NexusClientException if the request fails
     */
    protected <T> ApiResponse<T> putResponse(String endpoint, Object body, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing PUT request to endpoint: " + endpoint);
        return httpClient.put(endpoint, body, responseType);
    }

    /**
     * Executes a DELETE request and returns the full response for custom handling.
     *
     * @param endpoint the API endpoint
     * @param responseType the expected response type
     * @param <T> the response type
     * @return the full API response
     * @throws NexusClientException if the request fails
     */
    protected <T> ApiResponse<T> deleteResponse(String endpoint, Class<T> responseType) throws NexusClientException {
        logger.debug("Executing DELETE request to endpoint: " + endpoint);
        return httpClient.delete(endpoint, responseType);
    }
}
