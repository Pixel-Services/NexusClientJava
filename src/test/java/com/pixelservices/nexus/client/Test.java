package com.pixelservices.nexus.client;

import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.user.UserRepository;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        try {
            NexusClient client = NexusClient
                    .withToken("vendorsecret123?")
                    .withVendorId("vendor123")
                    .withBaseUrl("http://localhost:8080")
                    .build();

            UserRepository ur = client.userRepository;

            System.out.println(ur.getUser("b699523a-d185-432a-a58e-2ddbebecc8e3"));
            
            // Example of getting all users
            System.out.println(Arrays.toString(ur.getUsers()));
            
            // Close the client when done
            client.close();
            
        } catch (NexusClientException e) {
            System.err.println("Nexus client error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
