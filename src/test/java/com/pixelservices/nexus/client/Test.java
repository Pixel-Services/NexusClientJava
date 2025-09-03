package com.pixelservices.nexus.client;

public class Test {
    public static void main(String[] args) {
        NexusClient client = NexusClient.withToken("your_token_here")
                .withVendorId("your_vendor_id_here")
                .build();
    }
}
