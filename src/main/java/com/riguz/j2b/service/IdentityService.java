package com.riguz.j2b.service;

import java.util.UUID;

import com.riguz.j2b.util.EncryptUtil;

public class IdentityService {

    public static String getNewId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getNewToken() {
        UUID uuid = UUID.randomUUID();
        return EncryptUtil.encrypt("SHA-1", uuid.toString());
    }
}
