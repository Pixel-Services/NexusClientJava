package com.pixelservices.nexus.client.exception;

/**
 * Exception thrown when an HTTP request fails.
 * Contains information about the HTTP status code and response.
 */
public class NexusClientHttpException extends NexusClientException {
    private final int statusCode;
    private final String responseBody;

    public NexusClientHttpException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public NexusClientHttpException(String message, int statusCode, String responseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (Status: " + statusCode + ")";
    }
}
