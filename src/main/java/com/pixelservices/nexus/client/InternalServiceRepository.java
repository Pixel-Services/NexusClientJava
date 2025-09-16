package com.pixelservices.nexus.client;

import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.http.HttpClient;
import com.pixelservices.nexus.client.service.Service;
import com.pixelservices.nexus.client.service.ServiceData;
import com.pixelservices.nexus.client.service.ServiceRepository;

class InternalServiceRepository extends ServiceRepository {
    
    public InternalServiceRepository(HttpClient httpClient) {
        super(httpClient);
    }

    @Override
    public Service[] getServices() throws NexusClientException {
        ServiceData[] servicesData = get("/api/vendor/services", ServiceData[].class);
        Service[] services = new Service[servicesData.length];
        for (int i = 0; i < servicesData.length; i++) {
            services[i] = constructService(servicesData[i]);
        }
        return services;
    }

    @Override
    public Service getService(String serviceId) throws NexusClientException {
        if (serviceId == null || serviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Service ID cannot be null or empty");
        }
        return constructService(get("/api/vendor/services/" + serviceId, ServiceData.class));
    }

    private Service constructService(ServiceData serviceData) {
        return new Service(serviceData, this);
    }
}
