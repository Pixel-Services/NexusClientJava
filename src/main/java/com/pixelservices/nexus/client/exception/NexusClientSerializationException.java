package com.pixelservices.nexus.client.exception;

/**
 * Exception thrown when serialization or deserialization fails.
 * This typically occurs when converting objects to/from JSON.
 */
public class NexusClientSerializationException extends NexusClientException {
    private final Class<?> targetType;
    private final String jsonData;

    public NexusClientSerializationException(String message, Class<?> targetType, String jsonData) {
        super(message);
        this.targetType = targetType;
        this.jsonData = jsonData;
    }

    public NexusClientSerializationException(String message, Class<?> targetType, String jsonData, Throwable cause) {
        super(message, cause);
        this.targetType = targetType;
        this.jsonData = jsonData;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public String getJsonData() {
        return jsonData;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (Target type: " + targetType.getSimpleName() + ")";
    }
}
