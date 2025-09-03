package com.pixelservices.nexus.client.user;

import com.pixelservices.nexus.client.NexusClient;

public interface UserRepository {
    User[] getUsers();
    User getUser(String userId);
}
