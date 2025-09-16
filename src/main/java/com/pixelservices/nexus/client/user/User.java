package com.pixelservices.nexus.client.user;

import com.pixelservices.nexus.client.data.EMail;
import com.pixelservices.nexus.client.exception.NexusClientException;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class User extends UserData {
    private final UserRepository userRepository;

    User(UserData userData, UserRepository userRepository) {
        super(userData.getId(), userData.getUsername(), userData.getEmail(), userData.getFirstName(), userData.getLastName(), userData.getAvatar());
        this.userRepository = userRepository;
    }

    /**
     * Sends an email to this user.
     *
     * @param email the email to send
     * @throws NexusClientException if the email sending fails
     */
    public void sendEmail(EMail email) throws NexusClientException {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        userRepository.sendEmail(getId(), email);
    }
}
