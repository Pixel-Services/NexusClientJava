package com.pixelservices.nexus.client.user;

import com.pixelservices.nexus.client.data.EMail;
import com.pixelservices.nexus.client.exception.NexusClientException;
import com.pixelservices.nexus.client.http.HttpClient;
import com.pixelservices.nexus.client.repository.BaseRepository;

public abstract class UserRepository extends BaseRepository {

    protected UserRepository(HttpClient httpClient) {
        super(httpClient);
    }

    public abstract User[] getUsers() throws NexusClientException;
    public abstract User getUser(String userId) throws NexusClientException;
    public abstract boolean sendEmail(String userId, EMail email) throws NexusClientException;

    protected User constructUser(UserData userData) {
        return new User(userData, this);
    }
}
