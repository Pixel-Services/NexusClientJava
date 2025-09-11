package com.pixelservices.nexus.client.service;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Service extends ServiceData {
    private final ServiceRepository serviceRepository;

    public Service(ServiceData serviceData, ServiceRepository serviceRepository) {
        super(serviceData.getId(), serviceData.getName());
        this.serviceRepository = serviceRepository;
    }
}
