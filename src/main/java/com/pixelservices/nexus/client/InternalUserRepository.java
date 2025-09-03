package com.pixelservices.nexus.client;

import com.pixelservices.nexus.client.user.User;
import com.pixelservices.nexus.client.user.UserData;
import com.pixelservices.nexus.client.user.UserRepository;

class InternalUserRepository implements UserRepository {
    private final NexusClient nexusClient;

    public InternalUserRepository(NexusClient nexusClient) {
        this.nexusClient = nexusClient;
    }

    @Override
    public User[] getUsers() {
        return null;
    }

    @Override
    public User getUser(String userId) {
        return new User(nexusClient.get("/users/" + userId, UserData.class));
    }
}
