package com.pixelservices.nexus.client.exception;

/**
 * Exception thrown when request validation fails.
 * This typically occurs when required parameters are missing or invalid.
 */
public class NexusClientValidationException extends NexusClientException {
    private final String field;
    private final String value;

    public NexusClientValidationException(String message) {
        super(message);
        this.field = null;
        this.value = null;
    }

    public NexusClientValidationException(String message, String field, String value) {
        super(message);
        this.field = field;
        this.value = value;
    }

    public NexusClientValidationException(String message, String field, String value, Throwable cause) {
        super(message, cause);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        if (field != null) {
            return super.getMessage() + " (Field: " + field + ", Value: " + value + ")";
        }
        return super.getMessage();
    }
}
