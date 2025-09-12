package com.pixelservices.nexus.client;

import com.pixelservices.nexus.client.http.DefaultHttpClient;
import com.pixelservices.nexus.client.http.HttpClient;
import com.pixelservices.nexus.client.monitoring.ClientMetrics;
import com.pixelservices.nexus.client.user.UserRepository;
import com.pixelservices.nexus.client.service.ServiceRepository;
import org.jetbrains.annotations.NotNull;

public class NexusClient implements AutoCloseable {
    private final HttpClient httpClient;

    // Repositories
    public final UserRepository userRepository;
    public final ServiceRepository serviceRepository;


    NexusClient(@NotNull String baseUrl, @NotNull String token, String vendorId) {
        this.httpClient = new DefaultHttpClient(
                baseUrl,
                token,
                vendorId
        );

        // Initialize repositories
        this.userRepository = new InternalUserRepository(httpClient);
        this.serviceRepository = new InternalServiceRepository(httpClient);
    }

    /**
     * Gets the HTTP client instance for advanced usage.
     * 
     * @return the HTTP client instance
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Gets the client metrics for monitoring and debugging.
     *
     * @return the client metrics
     */
    public ClientMetrics getMetrics() {
        if (httpClient instanceof DefaultHttpClient) {
            return ((DefaultHttpClient) httpClient).getMetrics();
        }
        return null;
    }

    /**
     * Closes the client and releases resources.
     */
    public void close() {
        if (httpClient instanceof DefaultHttpClient) {
            ((DefaultHttpClient) httpClient).close();
        }
    }

    public static NexusClientBuilder withToken(String token) {
        return new NexusClientBuilder(token);
    }
}
