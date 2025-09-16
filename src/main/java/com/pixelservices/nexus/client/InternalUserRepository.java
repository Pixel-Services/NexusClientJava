package com.pixelservices.nexus.client;

import com.pixelservices.nexus.client.data.EMail;
import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.http.HttpClient;
import com.pixelservices.nexus.client.user.User;
import com.pixelservices.nexus.client.user.UserData;
import com.pixelservices.nexus.client.user.UserRepository;

class InternalUserRepository extends UserRepository {
    
    public InternalUserRepository(HttpClient httpClient) {
        super(httpClient);
    }

    @Override
    public User[] getUsers() throws NexusClientException {
        UserData[] usersData = get("/api/nexus/users", UserData[].class);
        User[] users = new User[usersData.length];
        for (int i = 0; i < usersData.length; i++) {
            users[i] = constructUser(usersData[i]);
        }
        return users;
    }

    @Override
    public User getUser(String userId) throws NexusClientException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return constructUser(get("/api/nexus/users/" + userId, UserData.class));
    }

    @Override
    public boolean sendEmail(String userId, EMail email) throws NexusClientException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return post("/api/nexus/users/" + userId + "/send-email", email, Boolean.class);
    }
}
