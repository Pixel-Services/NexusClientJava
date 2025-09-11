package com.pixelservices.nexus.client;

import com.pixelservices.nexus.client.data.EMail;
import com.pixelservices.nexus.client.exception.NexusClientException;

public class Test {
    public static void main(String[] args) {
        try {
            NexusClient client = NexusClient.withToken("your_token_here")
                    .withVendorId("your_vendor_id_here")
                    .build();

            // Example usage with proper exception handling
            client.userRepository.getUser("some_user_id").sendEmail(new EMail("Subject", "Body"));
            
            // Example of getting all users
            var users = client.userRepository.getUsers();
            System.out.println("Found " + users.length + " users");
            
            // Example of getting all services
            var services = client.serviceRepository.getServices();
            System.out.println("Found " + services.length + " services");
            
            // Close the client when done
            client.close();
            
        } catch (NexusClientException e) {
            System.err.println("Nexus client error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
