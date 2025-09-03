package com.pixelservices.nexus.client.user;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class User extends UserData {
    public User(UserData userData) {
        super(userData.getId(), userData.getUsername(), userData.getEmail(), userData.getFirstName(), userData.getLastName(), userData.getAvatar());
    }
}
