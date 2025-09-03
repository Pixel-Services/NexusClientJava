package com.pixelservices.nexus.client.exception;

import java.io.IOException;

public class NexusClientException extends RuntimeException {
    public NexusClientException(String message) {
        super(message);
    }

    public NexusClientException(String message, Throwable e) {
        super(message, e);
    }
}
