package com.riguz.jb.web.service;

import java.util.UUID;

import com.riguz.jb.util.EncryptUtil;

public class IdentityService {

    public static String getNewId(String tableName) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getNewToken() {
        UUID uuid = UUID.randomUUID();
        return EncryptUtil.encrypt("SHA-1", uuid.toString());
    }
}
