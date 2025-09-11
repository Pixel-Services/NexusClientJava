package com.pixelservices.nexus.client.exception;

public class NexusClientException extends RuntimeException {
    public NexusClientException(String message) {
        super(message);
    }

    public NexusClientException(String message, Throwable e) {
        super(message, e);
    }
}
