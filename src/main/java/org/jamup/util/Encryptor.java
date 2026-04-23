package org.jamup.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    //non-instantiable class
    private Encryptor() {}

    /**
     * Hashes a string using the SHA-256 algorithm and returns the result as a hexadecimal string.
     *
     * @param password the string to be hashed
     * @return the hexadecimal representation of the SHA-256 hash
     * @throws IllegalStateException if the SHA-256 algorithm is not available
     */
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