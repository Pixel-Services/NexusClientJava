package com.pixelservices.nexus.client.service;

import com.pixelservices.nexus.client.exception.NexusClientException;

public interface ServiceRepository {
    Service[] getServices() throws NexusClientException;
    Service getService(String serviceId) throws NexusClientException;
}
