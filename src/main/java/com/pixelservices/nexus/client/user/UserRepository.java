package com.pixelservices.nexus.client.user;

import com.pixelservices.nexus.client.data.EMail;
import com.pixelservices.nexus.client.exception.NexusClientException;

public interface UserRepository {
    User[] getUsers() throws NexusClientException;
    User getUser(String userId) throws NexusClientException;
    boolean sendEmail(String userId, EMail email) throws NexusClientException;
}
