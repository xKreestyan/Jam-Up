package org.jamup.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    //non-instantiable class
    private Encryptor() {}

    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available in this environment", e);
        }
    }

}