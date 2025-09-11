package com.pixelservices.nexus.client.service;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@RequiredArgsConstructor
@SuperBuilder
public class ServiceData {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
}
