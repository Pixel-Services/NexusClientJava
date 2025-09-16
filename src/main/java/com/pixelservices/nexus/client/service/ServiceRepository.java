package com.pixelservices.nexus.client.service;

import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.http.HttpClient;
import com.pixelservices.nexus.client.repository.BaseRepository;

public abstract class ServiceRepository extends BaseRepository {
    protected ServiceRepository(HttpClient httpClient) {
        super(httpClient);
    }

    public abstract Service[] getServices() throws NexusClientException;
    public abstract Service getService(String serviceId) throws NexusClientException;
}
