package com.pixelservices.nexus.client.data;

public record EMail(String subject, String body, boolean isHtml) {
    public EMail(String subject, String body) {
        this(subject, body, false);
    }
}