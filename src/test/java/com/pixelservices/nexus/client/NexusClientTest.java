package com.pixelservices.nexus.client;

import com.pixelservices.nexus.client.exception.NexusClientAuthenticationException;
import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class NexusClientTest {

    private static NexusClient client;

    @BeforeAll
    static void setUp() {
        client = NexusClient
                .withToken("vendor123")
                .withVendorId("vendorsecret123?")
                .withBaseUrl("http://localhost:8080")
                .build();

        assertNotNull(client);
    }

    @Test
    void testGetUser() {
        User user = client.userRepository.getUser("b699523a-d185-432a-a58e-2ddbebecc8e3");
        assertNotNull(user);
    }

    @Test
    void testGetUsers() {
        User[] users = client.userRepository.getUsers();
        assertNotNull(users);
        assertTrue(users.length > 0);
    }

    @Test
    void testClientExceptions() {
        // Invalid URL
        {
            // Arrange
            NexusClientException exception = assertThrows(NexusClientAuthenticationException.class, () -> {
                NexusClient
                        .withToken("testtoken123?")
                        .withVendorId("testvendor123")
                        .withVerifyAttempts(5)
                        .withVerifyDelay(Duration.ofSeconds(1))
                        .withBaseUrl("http://invalid-url")
                        .build();
            });

            // Assert
            assertTrue(exception.getMessage().contains("Failed to verify connection"));
        }
        // Invalid Credentials
        {
            // Arrange
            NexusClientException exception = assertThrows(NexusClientAuthenticationException.class, () -> {
                NexusClient
                        .withToken("testtoken123?")
                        .withVendorId("testvendor123")
                        .withVerifyAttempts(1)
                        .withBaseUrl("http://localhost:8080")
                        .build();
            });

            // Assert
            assertTrue(exception.getMessage().contains("Failed to verify connection"));
        }
    }
}