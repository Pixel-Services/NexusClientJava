package com.pixelservices.nexus.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelservices.logger.Logger;
import com.pixelservices.logger.LoggerFactory;
import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.monitoring.ClientMetrics;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the HttpClient interface using Apache HTTP Client.
 * Handles authentication, request/response processing, and error handling.
 */
public class DefaultHttpClient implements HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpClient.class);
    
    private final String baseUrl;
    private final String token;
    private final String vendorId;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ClientMetrics metrics;

    public DefaultHttpClient(String baseUrl, String token, String vendorId) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.vendorId = vendorId;
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.metrics = new ClientMetrics();
    }

    @Override
    public <T> ApiResponse<T> get(String endpoint, Class<T> responseType) throws NexusClientException {
        HttpGet request = new HttpGet(baseUrl + endpoint);
        return executeRequest(request, responseType);
    }

    @Override
    public <T> ApiResponse<T> post(String endpoint, Object body, Class<T> responseType) throws NexusClientException {
        HttpPost request = new HttpPost(baseUrl + endpoint);
        return executeRequestWithBody(request, body, responseType);
    }

    @Override
    public <T> ApiResponse<T> put(String endpoint, Object body, Class<T> responseType) throws NexusClientException {
        HttpPut request = new HttpPut(baseUrl + endpoint);
        return executeRequestWithBody(request, body, responseType);
    }

    @Override
    public <T> ApiResponse<T> delete(String endpoint, Class<T> responseType) throws NexusClientException {
        HttpDelete request = new HttpDelete(baseUrl + endpoint);
        return executeRequest(request, responseType);
    }

    private <T> ApiResponse<T> executeRequest(HttpRequestBase request, Class<T> responseType) throws NexusClientException {
        addAuthenticationHeaders(request);
        
        long startTime = System.currentTimeMillis();
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            long responseTime = System.currentTimeMillis() - startTime;
            ApiResponse<T> apiResponse = processResponse(response, responseType);
            
            if (apiResponse.isSuccess()) {
                metrics.recordSuccess(responseTime);
                logger.debug("Request to " + request.getURI() + " completed successfully in " + responseTime + "ms");
            } else {
                metrics.recordFailure(responseTime);
                logger.warn("Request to " + request.getURI() + " failed with status " + apiResponse.getStatusCode() + " in " + responseTime + "ms");
            }
            
            return apiResponse;
        } catch (IOException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            metrics.recordFailure(responseTime);
            logger.error("Failed to execute HTTP request to " + request.getURI() + " in " + responseTime + "ms", e);
            throw new NexusClientException("HTTP request failed", e);
        }
    }

    private <T> ApiResponse<T> executeRequestWithBody(HttpEntityEnclosingRequestBase request, Object body, Class<T> responseType) throws NexusClientException {
        addAuthenticationHeaders(request);
        
        try {
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                request.setEntity(new StringEntity(jsonBody, "UTF-8"));
                request.setHeader("Content-Type", "application/json");
            }
            return executeRequest(request, responseType);
        } catch (IOException e) {
            logger.error("Failed to serialize request body", e);
            throw new NexusClientException("Failed to serialize request body", e);
        }
    }

    private void addAuthenticationHeaders(HttpRequestBase request) {
        request.addHeader("X-Vendor-Id", vendorId);
        request.addHeader("X-Vendor-Access-Token", token);
    }

    private <T> ApiResponse<T> processResponse(CloseableHttpResponse response, Class<T> responseType) throws NexusClientException {
        int statusCode = response.getStatusLine().getStatusCode();
        String rawResponse = null;
        T data = null;
        boolean success = statusCode >= 200 && statusCode < 300;
        
        try {
            rawResponse = EntityUtils.toString(response.getEntity());
            
            if (success && rawResponse != null && !rawResponse.trim().isEmpty()) {
                if (responseType != Void.class) {
                    data = objectMapper.readValue(rawResponse, responseType);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to parse response body", e);
            if (success) {
                throw new NexusClientException("Failed to parse response body", e);
            }
        }

        Map<String, String> headers = new HashMap<>();
        for (org.apache.http.Header header : response.getAllHeaders()) {
            headers.put(header.getName(), header.getValue());
        }

        return ApiResponse.<T>builder()
                .data(data)
                .success(success)
                .statusCode(statusCode)
                .message(response.getStatusLine().getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .headers(headers)
                .rawResponse(rawResponse)
                .build();
    }

    /**
     * Gets the client metrics.
     *
     * @return the client metrics
     */
    public ClientMetrics getMetrics() {
        return metrics;
    }

    /**
     * Closes the underlying HTTP client and releases resources.
     */
    public void close() {
        try {
            httpClient.close();
            logger.info("HTTP client closed. Final metrics: " + metrics);
        } catch (IOException e) {
            logger.error("Failed to close HTTP client", e);
        }
    }
}
