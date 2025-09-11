package com.pixelservices.nexus.client.http;

import com.pixelservices.nexus.client.exception.NexusClientException;

/**
 * Generic HTTP client interface for making REST API calls.
 * Provides type-safe methods for different HTTP operations.
 */
public interface HttpClient {
    
    /**
     * Performs a GET request to the specified endpoint.
     *
     * @param endpoint the API endpoint to call
     * @param responseType the expected response type
     * @param <T> the response type
     * @return ApiResponse containing the response data and metadata
     * @throws NexusClientException if the request fails
     */
    <T> ApiResponse<T> get(String endpoint, Class<T> responseType) throws NexusClientException;
    
    /**
     * Performs a POST request to the specified endpoint.
     *
     * @param endpoint the API endpoint to call
     * @param body the request body
     * @param responseType the expected response type
     * @param <T> the response type
     * @return ApiResponse containing the response data and metadata
     * @throws NexusClientException if the request fails
     */
    <T> ApiResponse<T> post(String endpoint, Object body, Class<T> responseType) throws NexusClientException;
    
    /**
     * Performs a PUT request to the specified endpoint.
     *
     * @param endpoint the API endpoint to call
     * @param body the request body
     * @param responseType the expected response type
     * @param <T> the response type
     * @return ApiResponse containing the response data and metadata
     * @throws NexusClientException if the request fails
     */
    <T> ApiResponse<T> put(String endpoint, Object body, Class<T> responseType) throws NexusClientException;
    
    /**
     * Performs a DELETE request to the specified endpoint.
     *
     * @param endpoint the API endpoint to call
     * @param responseType the expected response type
     * @param <T> the response type
     * @return ApiResponse containing the response data and metadata
     * @throws NexusClientException if the request fails
     */
    <T> ApiResponse<T> delete(String endpoint, Class<T> responseType) throws NexusClientException;
}
